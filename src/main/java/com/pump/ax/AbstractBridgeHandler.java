package com.pump.ax;

import java.awt.event.InvocationEvent;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public abstract class AbstractBridgeHandler {

    /**
     * The `invoke` method should return this value if this
     * handler does not support the given Method.
     */
    public static final Object RETURN_VALUE_UNSUPPORTED = new Object();

    /**
     * This may intercept a Method invocation from AccessBridge,
     * CAccessibility or CAccessibleText.
     * <p>
     * Either `defaultSupplier` or `defaultRunnable` will be non-null.
     * (They will not both be non-null.)
     *
     * @param invocationEvent the InvocationEvent used to wrap up the method
     * @param method the Method being invoked
     * @param defaultSupplier the optional Supplier that would run
     *                        by default if this method lets it.
     * @param defaultRunnable the optional Runnable that would run
     *                        by default if this method lets it.
     * @param arguments the arguments the Supplier or Runnable uses.
     * @return an intercepted return value, or {@link #RETURN_VALUE_UNSUPPORTED}
     * if this AbstractBridgeHandler does not recognize the Method provided.
     */
    public abstract Object invoke(InvocationEvent invocationEvent, Method method, Supplier defaultSupplier, Runnable defaultRunnable, Object[] arguments);
}
