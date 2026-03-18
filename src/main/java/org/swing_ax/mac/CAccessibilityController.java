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
import java.util.function.Function;

public class CAccessibilityController {
    private static final CAccessibilityController instance = new CAccessibilityController();

    public static void initialize() {
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
            @Override
            protected void dispatchEvent(AWTEvent event) {
                CAccessibilityController controller = CAccessibilityController.get();
                if (controller.hasListeners()) {
                    InvocationEventInfo invoInfo = controller.dispatchEvent(event);
                    if (invoInfo != null) {
                        if (!invoInfo.invocationEvent.isDispatched()) {
                            super.dispatchEvent(invoInfo.invocationEvent);
                        }
                        controller.fireNotificationListeners(invoInfo);
                        return;
                    }
                }
                super.dispatchEvent(event);
            }
        });
    }

    public static CAccessibilityController get() {
        return instance;
    }

    boolean isInitialized;
    static Field field_invocationEvent_runnable, field_callableWrapper_callable, field_callableWrapper_object;
    static Method method_callableWrapper_getResult, method_caccessible_getSwingAccessible, method_invocationEvent_finishedDispatching;

    private CAccessibilityController() {
        try {
            Class invocationEventClass = InvocationEvent.class;
            field_invocationEvent_runnable = getField(invocationEventClass, "runnable");

            Class callableWrapperClass = Class.forName("sun.lwawt.macosx.LWCToolkit$CallableWrapper");
            field_callableWrapper_callable = getField(callableWrapperClass, "callable");
            field_callableWrapper_object = getField(callableWrapperClass, "object");
            method_callableWrapper_getResult = getMethod(callableWrapperClass, "getResult");

            Class caccessibleClass = Class.forName("sun.lwawt.macosx.CAccessible");
            method_caccessible_getSwingAccessible = getMethod(caccessibleClass, "getSwingAccessible", Accessible.class);


            method_invocationEvent_finishedDispatching = getMethod(InvocationEvent.class, "finishedDispatching", Boolean.TYPE);

            isInitialized = true;
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

    private InvocationEventInfo dispatchEvent(AWTEvent event) {
        if (!isInitialized)
            return null;
        if (!(event instanceof InvocationEvent))
            return null;
        InvocationEvent invocationEvent = (InvocationEvent) event;
        try {
            String paramString = event.paramString();
            String anonClassname = null;
            Runnable runnable = null;
            Callable<?> callable = null;
            if (paramString.contains("sun.lwawt.macosx.CAccessibility$")) {
                runnable = (Runnable) field_invocationEvent_runnable.get(event);
                String runnable_classname = runnable.getClass().getName();
                if (runnable_classname.startsWith("sun.lwawt.macosx.CAccessibility$")) {
                    anonClassname = runnable_classname.substring(32);
                }
            } else if (paramString.contains("sun.lwawt.macosx.LWCToolkit$CallableWrapper")) {
                runnable = (Runnable) field_invocationEvent_runnable.get(event);
                callable = (Callable<?>) field_callableWrapper_callable.get(runnable);

                String callableWrapper_callable_classname = callable.getClass().getName();
                if (callableWrapper_callable_classname.startsWith("sun.lwawt.macosx.CAccessibility$")) {
                    anonClassname = callableWrapper_callable_classname.substring(32);
                }
            }

            if (anonClassname != null) {
                Method method = callable.getClass().getEnclosingMethod();
                // method may be null for getAccessibleActionCount, which uses a lambda (not an inner class)
                if (method != null) {
                    InvocationEventInfo invoInfo = new InvocationEventInfo(invocationEvent, method, runnable, callable);
                    RequestListener[] requestListenerArray = requestListeners.toArray(new RequestListener[0]);
                    for (RequestListener l : requestListenerArray) {
                        MethodInvocationRequest r = new MethodInvocationRequest(invoInfo);
                        l.request(r);
                        if (r.isIntercepted())
                            break;
                    }
                    return invoInfo;
                }
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    private void fireNotificationListeners(InvocationEventInfo invoInfo) {
        NotificationListener[] notificationListenersArray = notificationListeners.toArray(new NotificationListener[0]);
        MethodInvocationNotification notification = new MethodInvocationNotification(invoInfo);
        for (NotificationListener l : notificationListenersArray) {
            l.notify(notification);
        }
    }

    private static class InvocationEventInfo {
        private final Method method;
        private final InvocationEvent invocationEvent;
        private final Runnable invocationEventRunnable;
        private final Callable<?> runnableCallable;

        private Object returnValue;
        private boolean isReturnValueDefined;

        private Map<String, Object> arguments;
        private boolean isIntercepted;

        /**
         * @param runnableCallable this may be null, depending on whether the Runnable is a plain
         *                                Runnable (like when you request the focus), or if it's a
         *                                LWCToolkit$CallableWrapper (like when you need a return value)
         */
        private InvocationEventInfo(InvocationEvent invocationEvent, Method method,
                                    Runnable invocationEventRunnable, Callable<?> runnableCallable) {
            this.method = Objects.requireNonNull(method);
            this.invocationEvent = Objects.requireNonNull(invocationEvent);
            this.invocationEventRunnable = Objects.requireNonNull(invocationEventRunnable);
            this.runnableCallable = runnableCallable;
        }

        /**
         * Return the return value of the InvocationEvent if the invocation event represents a Callable.
         *
         * If the InvocationEvent is simply a Runnable, then this returns null.
         *
         * This will throw an IllegalStateException if you call it before {@link InvocationEvent#isDispatched()} returns true.
         */
        Object getReturnValue() throws Exception {
            if (!invocationEvent.isDispatched())
                throw new IllegalStateException("InvocationEvent.isDispatched() is false; this method should only be called after dispatching the event.");

            if (!isReturnValueDefined) {
                if (runnableCallable == null) {
                    // there's no return value here
                    returnValue = null;
                } else {
                    try {
                        returnValue = method_callableWrapper_getResult.invoke(invocationEventRunnable);

                        returnValue = unwrap(returnValue);
                    } catch(InvocationTargetException e) {
                        throw (Exception) e.getTargetException();
                    }
                }
                isReturnValueDefined = true;
            }
            return returnValue;
        }

        private Object unwrap(Object obj) throws InvocationTargetException, IllegalAccessException {
            if (obj != null && "sun.lwawt.macosx.CAccessible".equals(obj.getClass().getName())) {
                obj = method_caccessible_getSwingAccessible.invoke(null, obj);
            }
            return obj;
        }

        Map<String, Object> getArguments() {
            if (arguments == null) {
                Object caccessibleObj = runnableCallable != null ? runnableCallable : invocationEventRunnable;

                Field[] fields = caccessibleObj.getClass().getDeclaredFields();
                try {
                    Map<String, Object> m = new LinkedHashMap<>();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object obj = field.get(caccessibleObj);
                        String name = field.getName();
                        if (name.startsWith("val$")) {
                            name = name.substring(4);
                            obj = unwrap(obj);
                            m.put(name, obj);
                        }
                    }
                    // only assign after we're sure there are no exceptions
                    arguments = m;
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }
            }

            if (arguments == null)
                return Collections.emptyMap();

            return Collections.unmodifiableMap(arguments);
        }

        void intercept(Object returnValue) {
            // bounds check:
            if (method.getReturnType() == Void.class) {
                if (returnValue != null)
                    throw new IllegalArgumentException("The method " + method.getName() + " does not return a value; so we can't intercept it and return " + returnValue.getClass().getName());
            } else {
                if ( !(method.getReturnType().isInstance(returnValue) || (returnValue == null && !method.getReturnType().isPrimitive())))
                    throw new IllegalArgumentException("The method " + method.getName() + " expects a " + method.getReturnType().getName() + ", not " +
                            (returnValue == null ? "null" : "a " + returnValue.getClass().getName()));
            }

            try {
                if (runnableCallable != null) {
                    field_callableWrapper_object.set(invocationEventRunnable, returnValue);
                }
                if (!invocationEvent.isDispatched())
                    method_invocationEvent_finishedDispatching.invoke(invocationEvent, Boolean.TRUE);
                isIntercepted = true;

                this.returnValue = returnValue;
                isReturnValueDefined = true;
            } catch(Throwable t) {
                throw new RuntimeException(t);
            }
        }

        boolean isIntercepted() {
            return isIntercepted;
        }

        void filter(Function returnValueFilter) {
            if (!isReturnValueDefined) {
                if (!invocationEvent.isDispatched()) {
                    invocationEvent.dispatch();
                }
                try {
                    getReturnValue();
                } catch(Throwable t) {
                    throw new RuntimeException(t);
                }
            }

            returnValue = returnValueFilter.apply(returnValue);
            intercept(returnValue);
        }
    }

    public static class MethodInvocationRequest {
        private final InvocationEventInfo invoInfo;

        protected MethodInvocationRequest(InvocationEventInfo invoInfo) {
            this.invoInfo = Objects.requireNonNull(invoInfo);
        }

        public Map<String, Object> getArguments() {
            return invoInfo.getArguments();
        }

        public void intercept(Object returnValue) {
            invoInfo.intercept(returnValue);
        }

        public boolean isIntercepted() {
            return invoInfo.isIntercepted();
        }

        public Method getMethod() {
            return invoInfo.method;
        }

        public Object getArgument(int argIndex) {
            return getArguments().values().toArray(new Object[0])[argIndex];
        }

        /**
         * Filter the current return value and call {@link #intercept(Object)} to replace it.
         * <p>
         * If no return value has been injected yet, then this method will
         * call {@link InvocationEvent#dispatch()} to derive the default return value before
         * modifying ig.
         * </p>
         */
        public void filter(Function<Object[], Object[]> returnValueFilter) {
            invoInfo.filter(returnValueFilter);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(invoInfo.method.getName());
            sb.append("(");
            Object[] args = getArguments().values().toArray(new Object[0]);
            for (int a = 0; a < args.length; a++) {
                if (a != 0) {
                    sb.append(", ");
                }
                sb.append(args[a]);
            }
            sb.append(")");
            return sb.toString();
        }
    }

    public static class MethodInvocationNotification {
        private final InvocationEventInfo invoInfo;

        protected MethodInvocationNotification(InvocationEventInfo invoInfo) {
            this.invoInfo = Objects.requireNonNull(invoInfo);
        }

        public Method getMethod() {
            return invoInfo.method;
        }

        public Object getReturnValue() throws Exception {
            return invoInfo.getReturnValue();
        }

        public boolean isIntercepted() {
            return invoInfo.isIntercepted();
        }

        public Map<String, Object> getArguments() {
            return invoInfo.getArguments();
        }

        public Object getArgument(int argIndex) {
            return getArguments().values().toArray(new Object[0])[argIndex];
        }
    }

    /**
     * This listener is notified when CAccessibility requests something. This listener
     * has the opportunity to intercept (hijack) the request.
     */
    public interface RequestListener {
        void request(MethodInvocationRequest r);
    }

    /**
     * This listener is notified when CAccessibility finished a request. This
     * listener may inspect the return value, but it cannot change it.
     */
    public interface NotificationListener {
        void notify(MethodInvocationNotification notification);
    }

    private List<RequestListener> requestListeners = new LinkedList<>();
    private List<NotificationListener> notificationListeners = new LinkedList<>();

    public void addRequestListener(RequestListener listener) {
        requestListeners.add(listener);
    }

    public void removeRequestListener(RequestListener listener) {
        requestListeners.remove(listener);
    }

    public void addNotificationListener(NotificationListener listener) {
        notificationListeners.add(listener);
    }

    public void removeNotificationListener(NotificationListener listener) {
        notificationListeners.remove(listener);
    }

    private boolean hasListeners() {
        return !(notificationListeners.isEmpty() && requestListeners.isEmpty());
    }
}