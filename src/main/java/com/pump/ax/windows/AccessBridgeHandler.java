package com.pump.ax.windows;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class AccessBridgeHandler {

    public static final Object RETURN_VALUE_UNSUPPORTED = new Object();

    public Object invoke(Method method, Supplier defaultSupplier, Runnable defaultRunnable, Object[] arguments) {
        switch(method.getName()) {
            case "getAccessibleRoleStringFromContext":
                return getAccessibleRoleStringFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            default:
                return RETURN_VALUE_UNSUPPORTED;
        }
    }

    public AccessibleRole getAccessibleRoleStringFromContext(Supplier<AccessibleRole> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected Component getComponent(AccessibleContext ac) {
        // TODO: write fallback; this may be too fragile
        Accessible accessibleParent = ac.getAccessibleParent();
        int indexInParent = ac.getAccessibleIndexInParent();
        AccessibleContext parentContext = accessibleParent == null ? null : accessibleParent.getAccessibleContext();
        Accessible comp = parentContext == null ? null : parentContext.getAccessibleChild(indexInParent);
        if (comp instanceof Component)
            return (Component) comp;
        return null;
    }
}
