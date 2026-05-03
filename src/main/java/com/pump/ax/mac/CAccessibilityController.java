package com.pump.ax.mac;

import com.pump.ax.AbstractBridgeController;

import javax.accessibility.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CAccessibilityController extends AbstractBridgeController<CAccessibilityHandler> {
    static final String UNSUPPORTED_EXCEPTION_MESSAGE = "This feature is not supported because CAccessibilityController could not initialize correctly. This can usually be resolved by adding these VM arguments: `--add-opens java.desktop/java.awt.event=ALL-UNNAMED --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED`";

    private static final CAccessibilityController instance = new CAccessibilityController();
    private static boolean isInitialized;

    public static void initialize() {
        synchronized (CAccessibilityController.class) {
            if (isInitialized)
                return;
            isInitialized = true;
        }
    }

    public static CAccessibilityController get() {
        initialize();
        return instance;
    }

    static Method method_caccessible_getSwingAccessible, method_caccessible_getCAccessible, method_caccessible_dispose;
    static Class class_caccessible;

    private CAccessibilityController() {
        super("sun.lwawt.macosx.LWCToolkit$CallableWrapper");
    }

    @Override
    protected void initializeFields() throws Throwable {
        class_caccessible = Class.forName("sun.lwawt.macosx.CAccessible");
        method_caccessible_getSwingAccessible = getMethod(class_caccessible, "getSwingAccessible", Accessible.class);
        method_caccessible_getCAccessible = getMethod(class_caccessible, "getCAccessible", Accessible.class);
        method_caccessible_dispose = getMethod(class_caccessible, "dispose");
    }

    /**
     * This invokes `CAccessible.dispose()`
     */
    static void disposeCAccessible(Component comp) throws Exception {
        if (!get().isValid())
            throw new UnsupportedOperationException(UNSUPPORTED_EXCEPTION_MESSAGE);

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                disposeCAccessible(child);
            }
        }

        Object caccessible = method_caccessible_getCAccessible.invoke(null, comp);
        method_caccessible_dispose.invoke(caccessible);
    }

    private Object convertFromCAccessibleValue(Object obj) throws InvocationTargetException, IllegalAccessException {
        if (class_caccessible.isInstance(obj)) {
            obj = method_caccessible_getSwingAccessible.invoke(null, obj);
        }
        return obj;
    }

    @Override
    protected Object wrapReturnValue(Method method, Object originalResult, Object currentResult, boolean isForHandlers) throws IllegalAccessException, InvocationTargetException {
        if (isForHandlers) {
            currentResult = convertFromCAccessibleValue(currentResult);
        } else {
            if (shouldMethodReturnValueBeCAccessible(method, originalResult))
                currentResult = convertToCAccessibleValue(currentResult);
        }
        return currentResult;
    }

    @Override
    protected Object[] createArgumentArray(Object callableOrRunnable) throws Exception {
        Object[] returnValue = super.createArgumentArray(callableOrRunnable);
        for (int a = 0; a < returnValue.length; a++) {
            returnValue[a] = convertFromCAccessibleValue(returnValue[a]);
        }
        return returnValue;
    }

    static Map<Method, Boolean> isMethodExpectingCAccessibleValue = new HashMap<>();

    private boolean shouldMethodReturnValueBeCAccessible(Method method, Object originalResult) {
        // always check with a real-world example, if we can:
        if (originalResult != null)
            isMethodExpectingCAccessibleValue.put(method, class_caccessible.isInstance(originalResult));
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
        if (obj instanceof Accessible && !class_caccessible.isInstance(obj)) {
            obj = method_caccessible_getCAccessible.invoke(null, obj);
        }
        return obj;
    }
}