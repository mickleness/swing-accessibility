package com.pump.ax.mac;

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
    static final String UNSUPPORTED_EXCEPTION_MESSAGE = "This feature is not supported because CAccessibilityController could not initialize correctly. This can usually be resolved by adding these VM arguments: `--add-opens java.desktop/java.awt.event=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED`";

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

    boolean isValid;
    static Field field_invocationEvent_runnable, field_callableWrapper_callable, field_callableWrapper_object, field_callableWrapper_e;
    static Method method_caccessible_getSwingAccessible, method_caccessible_getCAccessible, method_invocationEvent_finishedDispatching, method_callableWrapper_getResult;
    static Class caccessibleClass;

    private CAccessibilityController() {
        try {
            Class invocationEventClass = InvocationEvent.class;
            field_invocationEvent_runnable = getField(invocationEventClass, "runnable");

            Class callableWrapperClass = Class.forName("sun.lwawt.macosx.LWCToolkit$CallableWrapper");
            field_callableWrapper_callable = getField(callableWrapperClass, "callable");
            field_callableWrapper_object = getField(callableWrapperClass, "object");
            field_callableWrapper_e = getField(callableWrapperClass, "e");

            method_callableWrapper_getResult = getMethod(callableWrapperClass, "getResult");

            caccessibleClass = Class.forName("sun.lwawt.macosx.CAccessible");
            method_caccessible_getSwingAccessible = getMethod(caccessibleClass, "getSwingAccessible", Accessible.class);
            method_caccessible_getCAccessible = getMethod(caccessibleClass, "getCAccessible", Accessible.class);

            method_invocationEvent_finishedDispatching = getMethod(InvocationEvent.class, "finishedDispatching", Boolean.TYPE);

            isValid = true;
        } catch(Throwable t) {
            // try adding "--add-opens java.desktop/java.awt.event=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED" to your VM arguments
            t.printStackTrace();
        }
    }

    public boolean isValid() {
        return isValid;
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
        if (!isValid || !(event instanceof InvocationEvent) || handlers.isEmpty())
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

        private Object convertFromCAccessibleValue(Object obj) throws InvocationTargetException, IllegalAccessException {
            if (caccessibleClass.isInstance(obj)) {
                obj = method_caccessible_getSwingAccessible.invoke(null, obj);
            }
            return obj;
        }

        static Map<Method, Boolean> isMethodExpectingCAccessibleValue = new HashMap<>();
        private boolean shouldMethodReturnValueBeCAccessible() {
            // always check with a real-world example, if we can:
            if (originalResult != null)
                isMethodExpectingCAccessibleValue.put(method, caccessibleClass.isInstance(originalResult));
            Boolean b = isMethodExpectingCAccessibleValue.get(method);
            if (b != null)
                return b;

            // let's hope the methods / method names haven't changed over time:
            if (method.getName().equals("getFocusOwner") ||
                    method.getName().equals("accessibilityHitTest"))
                return true;

            return false;
        }

        private Object convertToCAccessibleValue(Object obj) throws InvocationTargetException, IllegalAccessException {
            if (obj instanceof Accessible && !caccessibleClass.isInstance(obj)) {
                obj = method_caccessible_getCAccessible.invoke(null, obj);
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
                        obj = convertFromCAccessibleValue(obj);
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
                    if (shouldMethodReturnValueBeCAccessible())
                        returnValue = convertToCAccessibleValue(returnValue);
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
                        field_callableWrapper_object.set(invocationEventRunnable, returnValue);
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
                Object returnValue = h.invoke(method, this, this, component, arguments);
                if (returnValue != CAccessibilityHandler.RETURN_VALUE_UNSUPPORTED) {
                    return returnValue;
                }

                // TODO remove, or make a log.debug notification
                System.err.println("Warning: unrecognized \"" + method.getName() + "\", arguments = " + Arrays.asList(arguments));
            }

            // either we're out of handlers, or the method name isn't supported

            // erg, I think this is associated with the AXLink feature not always working:
//            invocationEvent.dispatch();

            // this appears to work instead, though
            invocationEventRunnable.run();

            // TODO: so look in invocationEvent.dispatch() and see exactly
            // what we need to add to emulate its error handling

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
                    return convertFromCAccessibleValue(originalResult);
                } catch (IllegalAccessException e) {
                    // this shouldn't happen:
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    // we shouldn't reach this line. If there was an exception, then we should
                    // have handled it above when we checked invocationEvent.getThrowable()
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
    }

    private List<CAccessibilityHandler> handlers = new LinkedList<>();

    public void addHandler(CAccessibilityHandler listener) {
        if (listener == null)
            return;
        initialize();
        if (!handlers.contains(listener))
           handlers.add(0, listener);
    }

    public void removeHandler(CAccessibilityHandler listener) {
        handlers.remove(listener);
    }
}