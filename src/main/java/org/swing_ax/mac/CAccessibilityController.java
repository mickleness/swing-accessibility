package org.swing_ax.mac;

import javax.accessibility.*;
import java.awt.*;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class CAccessibilityController {
    private static final CAccessibilityController instance = new CAccessibilityController();

    private static boolean isInitialized;

    public static void initialize() {
        synchronized (CAccessibilityController.class) {
            if (isInitialized)
                return;
            isInitialized = true;
        }

        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
            @Override
            protected void dispatchEvent(AWTEvent event) {
                CAccessibilityController controller = CAccessibilityController.get();
                if (controller.dispatchEvent(event)) {
                    return;
                }
                super.dispatchEvent(event);
            }
        });
    }

    public static CAccessibilityController get() {
        return instance;
    }

    boolean isConstructed;
    static Field field_invocationEvent_runnable, field_callableWrapper_callable, field_callableWrapper_object, field_callableWrapper_e;
    static Method method_caccessible_getSwingAccessible, method_invocationEvent_finishedDispatching, method_callableWrapper_getResult;

    private CAccessibilityController() {
        try {
            Class invocationEventClass = InvocationEvent.class;
            field_invocationEvent_runnable = getField(invocationEventClass, "runnable");

            Class callableWrapperClass = Class.forName("sun.lwawt.macosx.LWCToolkit$CallableWrapper");
            field_callableWrapper_callable = getField(callableWrapperClass, "callable");
            field_callableWrapper_object = getField(callableWrapperClass, "object");
            field_callableWrapper_e = getField(callableWrapperClass, "e");

            method_callableWrapper_getResult = getMethod(callableWrapperClass, "getResult");

            Class caccessibleClass = Class.forName("sun.lwawt.macosx.CAccessible");
            method_caccessible_getSwingAccessible = getMethod(caccessibleClass, "getSwingAccessible", Accessible.class);

            method_invocationEvent_finishedDispatching = getMethod(InvocationEvent.class, "finishedDispatching", Boolean.TYPE);

            isConstructed = true;
        } catch(Throwable t) {
            // try adding "--add-opens java.desktop/java.awt.event=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED" to your VM arguments
            t.printStackTrace();
        }
    }

    private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        Field returnValue = clazz.getDeclaredField(fieldName);
        returnValue.setAccessible(true);
        return returnValue;
    }

    private static Method getMethod(Class clazz, String methodName, Class... argumentTypes) throws NoSuchMethodException {
        Method returnValue = clazz.getDeclaredMethod(methodName, argumentTypes);
        returnValue.setAccessible(true);
        return returnValue;
    }

//    public static boolean unregisterFromCocoaAXSystem(Accessible accessible) {
//        try {
//            Class z1 = Class.forName("sun.lwawt.macosx.CAccessible");
//            Method m = getMethod(z1, "getCAccessible", Accessible.class);
//            Object t = m.invoke(null, accessible);
//
//            Class z2 = Class.forName("sun.lwawt.macosx.CFRetainedResource");
//            Field f = getField(z2, "ptr");
//            Long ptr = (Long) f.get(t);
//
//            if (ptr == 0)
//                return false;
//            Method m2 = getMethod(z1, "unregisterFromCocoaAXSystem", Long.TYPE);
//            m2.invoke(null, ptr);
//            return true;
//        } catch(Throwable t) {
//            t.printStackTrace();;
//        }
//        return false;
//    }

    private boolean dispatchEvent(AWTEvent event) {
        if (!isConstructed || !(event instanceof InvocationEvent) || handlers.isEmpty())
            return false;

        InvocationEvent invocationEvent = (InvocationEvent) event;
        try {
            String paramString = event.paramString();
            Runnable runnable = null;
            Callable<?> callable = null;
            Method method = null;
            if (paramString.contains("sun.lwawt.macosx.CAccessibility$")) {
                // for Runnables with no return value:
                runnable = (Runnable) field_invocationEvent_runnable.get(event);
                method = runnable.getClass().getEnclosingMethod();
            } else if (paramString.contains("sun.lwawt.macosx.LWCToolkit$CallableWrapper")) {
                // for everything that uses a return value
                runnable = (Runnable) field_invocationEvent_runnable.get(event);
                callable = (Callable<?>) field_callableWrapper_callable.get(runnable);
                method = callable.getClass().getEnclosingMethod();
                if (method == null) {
                    // this can happen for lambdas, such as getAccessibleActionCount
                }
            }

            if (method != null) {
                Component comp = invocationEvent.getSource() instanceof Component ? (Component) invocationEvent.getSource() : null;
                InvocationEventHelper invocationHelper = new InvocationEventHelper(invocationEvent, method, runnable, callable, comp, handlers.iterator());
                invocationHelper.invokeMethod();
                return true;
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return false;
    }

    private static class InvocationEventHelper implements Supplier<Object>, Runnable {
        private final Method method;
        private final InvocationEvent invocationEvent;
        private final Runnable invocationEventRunnable;
        private final Callable<?> runnableCallable;
        private final Iterator<CAccessibilityHandler> handlerIterator;
        private final Component component;
        private final Object[] arguments;
        private Object originalResult;

        /**
         * @param runnableCallable this may be null, depending on whether the Runnable is a plain
         *                                Runnable (like when you request the focus), or if it's a
         *                                LWCToolkit$CallableWrapper (like when you need a return value)
         */
        private InvocationEventHelper(InvocationEvent invocationEvent, Method method,
                                      Runnable invocationEventRunnable, Callable<?> runnableCallable,
                                      Component component,
                                      Iterator<CAccessibilityHandler> handlerIterator) {
            this.method = Objects.requireNonNull(method);
            this.invocationEvent = Objects.requireNonNull(invocationEvent);
            this.invocationEventRunnable = Objects.requireNonNull(invocationEventRunnable);
            this.runnableCallable = runnableCallable;
            this.handlerIterator = handlerIterator;
            this.component = component;
            arguments = createArgumentArray();
        }

        private Object unwrap(Object obj) throws InvocationTargetException, IllegalAccessException {
            if (obj != null && "sun.lwawt.macosx.CAccessible".equals(obj.getClass().getName())) {
                obj = method_caccessible_getSwingAccessible.invoke(null, obj);
            }
            return obj;
        }

        Object[] createArgumentArray() {
            Object caccessibleObj = runnableCallable != null ? runnableCallable : invocationEventRunnable;

            Field[] fields = caccessibleObj.getClass().getDeclaredFields();
            try {
                List<Object> returnValue = new ArrayList<>(fields.length);
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object obj = field.get(caccessibleObj);
                    String name = field.getName();
                    if (name.startsWith("val$")) {
                        name = name.substring(4);
                        obj = unwrap(obj);
                        returnValue.add(obj);
                    }
                }
                return returnValue.toArray(new Object[0]);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        void invokeMethod() {
            Object returnValue = null;
            try {
                try {
                    returnValue = get();
                } catch (Exception e) {
                    // TODO: remove
                    e.printStackTrace();
                    if (runnableCallable != null) {
                        field_callableWrapper_e.set(invocationEventRunnable, e);
                    } else {
                        // TODO
                    }
                } catch (Throwable t) {
                    // TODO: remove
                    t.printStackTrace();
                    if (runnableCallable != null) {
                        // TODO
                    } else {
                        // TODO
                    }
                } finally {
                    if (runnableCallable != null && returnValue != originalResult) {
                        field_callableWrapper_object.set(runnableCallable, returnValue);
                    }
                    if (!invocationEvent.isDispatched())
                        method_invocationEvent_finishedDispatching.invoke(invocationEvent, Boolean.TRUE);
                }
            } catch(InvocationTargetException | IllegalAccessException e) {
                // this shouldn't happen
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            get();
        }

        @Override
        public Object get() {
            if (handlerIterator.hasNext()) {
                CAccessibilityHandler h = handlerIterator.next();
                switch (method.getName()) {
                    case "getAccessibleActionDescription":
                        return h.getAccessibleActionDescription((Supplier) this,
                                (AccessibleAction) arguments[0],
                                (Integer) arguments[1],
                                component);
                    case "doAccessibleAction":
                        h.doAccessibleAction(this,
                                (AccessibleAction) arguments[0],
                                (Integer) arguments[1],
                                component);
                        return null;
                    case "getSize":
                        return h.getSize((Supplier) this,
                                (AccessibleComponent) arguments[0],
                                component);
                    case "getAccessibleSelection":
                        return h.getAccessibleSelection((Supplier) this,
                                (AccessibleContext) arguments[0],
                                component);
                    case "ax_getAccessibleSelection":
                        return h.ax_getAccessibleSelection((Supplier) this,
                                (AccessibleContext) arguments[0],
                                (Integer) arguments[1],
                                component);
                    case "addAccessibleSelection":
                        h.addAccessibleSelection(this,
                                (AccessibleContext) arguments[0],
                                (Integer) arguments[1],
                                component);
                        return null;
                    case "getAccessibleContext":
                        return h.getAccessibleContext((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "isAccessibleChildSelected":
                        return h.isAccessibleChildSelected((Supplier) this,
                                (Accessible) arguments[0],
                                (Integer) arguments[1],
                                component);
                    case "getAccessibleStateSet":
                        return h.getAccessibleStateSet((Supplier) this,
                                (AccessibleContext) arguments[0],
                                component);
                    case "contains":
                        return h.contains((Supplier) this,
                                (AccessibleContext) arguments[0],
                                (AccessibleState) arguments[1],
                                component);
                    case "getAccessibleRole":
                        return h.getAccessibleRole((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getLocationOnScreen":
                        return h.getLocationOnScreen((Supplier) this,
                                (AccessibleComponent) arguments[0],
                                component);
                    case "getCharCount":
                        return h.getCharCount((Supplier) this,
                                (AccessibleText) arguments[0],
                                component);
                    case "getAccessibleParent":
                        return h.getAccessibleParent((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleIndexInParent":
                        return h.getAccessibleIndexInParent((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleComponent":
                        return h.getAccessibleComponent((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleValue":
                        return h.getAccessibleValue((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleName":
                        return h.getAccessibleName((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleText":
                        return h.getAccessibleText((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleDescription":
                        return h.getAccessibleDescription((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "isFocusTraversable":
                        return h.isFocusTraversable((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "accessibilityHitTest":
                        return h.accessibilityHitTest((Supplier) this,
                                (Container) arguments[0],
                                (Float) arguments[1],
                                (Float) arguments[2]);
                    case "getAccessibleAction":
                        return h.getAccessibleAction((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    // TODO: reevaluate getAccessibleActionCount (lambdas are a different case)
                    case "isEnabled":
                        return h.isEnabled((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "requestFocus":
                        h.requestFocus(this,
                                (Accessible) arguments[0],
                                component);
                        return null;
                    case "requestSelection":
                        h.requestSelection(this,
                                (Accessible) arguments[0],
                                component);
                        return null;
                    case "getMaximumAccessibleValue":
                        return h.getMaximumAccessibleValue((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getMinimumAccessibleValue":
                        return h.getMinimumAccessibleValue((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleRoleDisplayString":
                        return h.getAccessibleRoleDisplayString((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getCurrentAccessibleValue":
                        return h.getCurrentAccessibleValue((Supplier) this,
                                (AccessibleValue) arguments[0],
                                component);
                    case "getFocusOwner":
                        return h.getFocusOwner((Supplier) this,
                                component);
                    case "getInitialAttributeStates":
                        return h.getInitialAttributeStates((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "invokeGetChildrenAndRoles":
                        return h.invokeGetChildrenAndRoles((Supplier) this,
                                (Accessible) arguments[0],
                                (Component) arguments[1],
                                (Integer) arguments[2],
                                (Boolean) arguments[3],
                                arguments[4]);
                    case "getChildrenAndRolesRecursive":
                        return h.getChildrenAndRolesRecursive((Supplier) this,
                                (Accessible) arguments[0],
                                (Component) arguments[1],
                                (Integer) arguments[2],
                                (Boolean) arguments[3],
                                (Integer) arguments[4]);
                    case "getAccessibleCurrentAccessible":
                        return h.getAccessibleCurrentAccessible((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAccessibleComboboxValue":
                        return h.getAccessibleComboboxValue((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getCurrentAccessiblePopupMenu":
                        return h.getCurrentAccessiblePopupMenu((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getTableInfo":
                        return h.getTableInfo((Supplier) this,
                                (Accessible) arguments[0],
                                (Component) arguments[1],
                                (Integer) arguments[2]);
                    case "getTableSelectedInfo":
                        return h.getTableSelectedInfo((Supplier) this,
                                (Accessible) arguments[0],
                                (Component) arguments[1],
                                (Integer) arguments[2]);
                    case "getChildren":
                        return h.getChildren((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getAWTView":
                        return h.getAWTView((Supplier) this,
                                (Accessible) arguments[0]);
                    case "isTreeRootVisible":
                        return h.isTreeRootVisible((Supplier) this,
                                (Accessible) arguments[0],
                                component);

                    // from CAccessibleText:
                    case "getAccessibleEditableText":
                        return h.getAccessibleEditableText((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getSelectedText":
                        return h.getSelectedText((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "setSelectedText":
                        h.setSelectedText(this,
                                (Accessible) arguments[0],
                                component,
                                (String) arguments[1] );
                        return null;
                    case "setSelectedTextRange":
                        h.setSelectedTextRange(this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1],
                                (Integer) arguments[2] );
                        return null;
                    case "getTextRange":
                        return h.getTextRange((Supplier) this,
                                (AccessibleEditableText) arguments[0],
                                (Integer) arguments[1],
                                (Integer) arguments[2],
                                component);
                    case "getCharacterIndexAtPosition":
                        return h.getCharacterIndexAtPosition((Supplier) this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1],
                                (Integer) arguments[2] );
                    case "getSelectedTextRange":
                        return h.getSelectedTextRange((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getVisibleCharacterRange":
                        return h.getVisibleCharacterRange((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getLineNumberForIndex":
                        return h.getLineNumberForIndex((Supplier) this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1] );
                    case "getLineNumberForInsertionPoint":
                        return h.getLineNumberForInsertionPoint((Supplier) this,
                                (Accessible) arguments[0],
                                component);
                    case "getRangeForLine":
                        return h.getRangeForLine((Supplier) this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1] );
                    case "getRangeForIndex":
                        return h.getRangeForIndex((Supplier) this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1] );
                    case "getBoundsForRange":
                        return h.getBoundsForRange((Supplier) this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1],
                                (Integer) arguments[2] );
                    case "getStringForRange":
                        return h.getStringForRange((Supplier) this,
                                (Accessible) arguments[0],
                                component,
                                (Integer) arguments[1],
                                (Integer) arguments[2] );

                    default:
                        // TODO remove, or make a log.debug notification
                        System.err.println("Warning: unrecognized \"" + method.getName() + "\"");
                }
            }

            // either we're out of handlers, or the method name isn't supported

            invocationEvent.dispatch();
            if (invocationEvent.getThrowable() != null) {
                if (invocationEvent.getThrowable() instanceof RuntimeException)
                    throw (RuntimeException) invocationEvent.getThrowable();
                if (invocationEvent.getThrowable() instanceof Error)
                    throw (Error) invocationEvent.getThrowable();
                throw new RuntimeException(invocationEvent.getThrowable());
            }

            if (runnableCallable != null) {
                try {
                    originalResult = method_callableWrapper_getResult.invoke(invocationEventRunnable);
                } catch (IllegalAccessException e) {
                    // this shouldn't happen:
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    // we shouldn't reach this line. If there was an exception, then we should
                    // have handled it above when we checked invocationEvent.getThrowable()
                    throw new RuntimeException(e);
                }
                return originalResult;
            }
            return null;
        }
    }

    private List<CAccessibilityHandler> handlers = new LinkedList<>();

    public void addHandler(CAccessibilityHandler listener) {
        if (listener == null)
            return;
        initialize();
        handlers.add(0, listener);
    }

    public void removeHandler(CAccessibilityHandler listener) {
        handlers.remove(listener);
    }
}