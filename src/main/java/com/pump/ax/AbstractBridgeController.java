package com.pump.ax;

import java.awt.*;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * This is the shared parent class for CAccessibilityController and AccessBridgeController.
 */
public class AbstractBridgeController<H extends AbstractBridgeHandler> {
    protected static Field field_invocationEvent_runnable, field_callableWrapper_callable, field_callableWrapper_object, field_callableWrapper_e;
    protected static Method method_invocationEvent_finishedDispatching, method_callableWrapper_getResult;

    boolean isValid;

    protected AbstractBridgeController(String callableWrapperClassname) {
        try {
            Class invocationEventClass = InvocationEvent.class;
            field_invocationEvent_runnable = getField(invocationEventClass, "runnable");
            method_invocationEvent_finishedDispatching = getMethod(InvocationEvent.class, "finishedDispatching", Boolean.TYPE);

            Class callableWrapperClass = Class.forName(callableWrapperClassname);
            field_callableWrapper_callable = getField(callableWrapperClass, "callable");
            field_callableWrapper_object = getField(callableWrapperClass, "object");
            field_callableWrapper_e = getField(callableWrapperClass, "e");
            method_callableWrapper_getResult = getMethod(callableWrapperClass, "getResult");

            initializeFields();

            isValid = true;
        } catch(Throwable t) {
            // TODO: log once UNSUPPORTED_EXCEPTION_MESSAGE
            // try adding "--add-opens java.desktop/java.awt.event=ALL-UNNAMED --add-opens java.desktop/com.sun.java.accessibility.internal=ALL-UNNAMED" to your VM arguments
            t.printStackTrace();
        }

        if (isValid()) {
            Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
                @Override
                protected void dispatchEvent(AWTEvent event) {
                    if (event instanceof InvocationEvent) {
                        if (AbstractBridgeController.this.dispatchEvent( (InvocationEvent) event)) {
                            return;
                        }
                    }
                    super.dispatchEvent(event);
                }
            });
        }
    }

    protected static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        Field returnValue = clazz.getDeclaredField(fieldName);
        returnValue.setAccessible(true);
        return returnValue;
    }

    protected static Method getMethod(Class clazz, String methodName, Class... argumentTypes) throws NoSuchMethodException {
        Method returnValue = clazz.getDeclaredMethod(methodName, argumentTypes);
        returnValue.setAccessible(true);
        return returnValue;
    }

    /**
     * Subclasses can identify additional Fields and Methods here.
     * This method is only called once per runtime. If it throws
     * an exception then {@link #isValid} returns false.
     */
    protected void initializeFields() throws Throwable {}

    public boolean isValid() {
        return isValid;
    }

    /**
     * Return true if this AbstractBridgeController consumed the InvocationEvent.
     * <p>
     * Normally you do not need to call this method directly, but this
     * method is public in case you meddle with the EventQueue and need to
     * pass InvocationEvents along manually.
     */
    public boolean dispatchEvent(InvocationEvent event) {
        if (!isValid() || handlers.isEmpty())
            return false;

        try {
            String paramString = event.paramString();
            Runnable runnable = null;
            Callable<?> callable = null;
            Method method = null;

            if (paramString.contains("com.sun.java.accessibility.internal.AccessBridge$InvocationUtils$CallableWrapper")) {
                // for everything that uses a return value
                runnable = (Runnable) field_invocationEvent_runnable.get(event);
                callable = (Callable<?>) field_callableWrapper_callable.get(runnable);
                method = callable.getClass().getEnclosingMethod();
                if (method == null) {
                    // this can happen for lambdas, such as getAccessibleActionCount
                }
            } else if (paramString.contains("com.sun.java.accessibility.internal.AccessBridge")) {
                // for Runnables with no return value:
                runnable = (Runnable) field_invocationEvent_runnable.get(event);
                method = runnable.getClass().getEnclosingMethod();
            }
            if (method != null) {
                InvocationEventHelper invocationHelper = new InvocationEventHelper(event, method, runnable, callable);
                invocationHelper.invokeMethod();
                return true;
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }

        return false;
    }

    private class InvocationEventHelper implements Supplier<Object>, Runnable {
        private final Method method;
        private final InvocationEvent invocationEvent;
        private final Runnable invocationEventRunnable;
        private final Callable<?> runnableCallable;
        private final Iterator<H> handlerIterator;
        private final Object[] arguments;
        private Object originalResult;

        /**
         * @param runnableCallable this may be null, depending on whether the Runnable is a plain
         *                         Runnable (like when you request the focus), or if it's a
         *                         LWCToolkit$CallableWrapper (like when you need a return value)
         */
        private InvocationEventHelper(InvocationEvent invocationEvent, Method method,
                                      Runnable invocationEventRunnable, Callable<?> runnableCallable) {
            this.method = Objects.requireNonNull(method);
            this.invocationEvent = Objects.requireNonNull(invocationEvent);
            this.invocationEventRunnable = Objects.requireNonNull(invocationEventRunnable);
            this.runnableCallable = runnableCallable;
            this.handlerIterator = handlers.iterator();
            try {
                arguments = createArgumentArray(runnableCallable != null ? runnableCallable :
                        invocationEventRunnable);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        void invokeMethod() {
            Object returnValue = null;
            try {
                try {
                    returnValue = get();
                    returnValue = wrapReturnValue(method, originalResult, returnValue, false);
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
                AbstractBridgeHandler h = handlerIterator.next();
                Object returnValue = h.invoke(invocationEvent, method, this, this, arguments);
                if (returnValue != AbstractBridgeHandler.RETURN_VALUE_UNSUPPORTED) {
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
                    return wrapReturnValue(method, originalResult, originalResult, true);
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

    /**
     * This method gives subclasses a chance to convert `currentResult` to another value
     *
     * @param isForHandlers if true then the return value of this method is passed to
     *                      AbstractBridgeHandlers. If false then the return value of
     *                      this method is sent back to AccessBridge/CAccessibility/CAccessibleText.
     * @return when in doubt: return currentResult.
     */
    protected Object wrapReturnValue(Method method, Object originalResult, Object currentResult, boolean isForHandlers) throws IllegalAccessException, InvocationTargetException {
        return currentResult;
    }

    private final List<H> handlers = new CopyOnWriteArrayList<>();

    public void addHandler(H listener) {
        if (listener == null)
            return;
        if (!handlers.contains(listener))
            handlers.add(0, listener);
    }

    public void removeHandler(H listener) {
        handlers.remove(listener);
    }

    /**
     * Identify the arguments that were passed when we set up a Runnable or Callable to
     * execute on the event dispatch thread.
     */
    protected Object[] createArgumentArray(Object callableOrRunnable) throws Exception {
        Field[] fields = callableOrRunnable.getClass().getDeclaredFields();
        List<Object> returnValue = new ArrayList<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(callableOrRunnable);
            String name = field.getName();
            if (name.startsWith("val$")) {
                returnValue.add(fieldValue);
            }
        }
        return returnValue.toArray(new Object[0]);
    }
}
