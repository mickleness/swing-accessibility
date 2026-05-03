package com.pump.ax.windows;

import com.pump.ax.AbstractBridgeController;

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

public class AccessBridgeController extends AbstractBridgeController<AccessBridgeHandler> {
    static final String UNSUPPORTED_EXCEPTION_MESSAGE = "This feature is not supported because AccessBridgeController could not initialize correctly. This can usually be resolved by adding these VM arguments: `--add-opens java.desktop/java.awt.event=ALL-UNNAMED --add-opens jdk.accessibility/com.sun.java.accessibility.internal=ALL-UNNAMED`";

    private static final AccessBridgeController instance = new AccessBridgeController();
    private static boolean isInitialized;

    public static void initialize() {
        synchronized (AccessBridgeController.class) {
            if (isInitialized)
                return;
            isInitialized = true;
        }
    }

    public static AccessBridgeController get() {
        initialize();
        return instance;
    }

    private AccessBridgeController() {
        super("com.sun.java.accessibility.internal.AccessBridge$InvocationUtils$CallableWrapper");
    }
}