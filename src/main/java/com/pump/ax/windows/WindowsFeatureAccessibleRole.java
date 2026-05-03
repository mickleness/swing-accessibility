package com.pump.ax.windows;

import com.pump.ax.AXHelper;
import com.pump.ax.AbstractFeatureAccessibleRole;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import java.awt.*;
import java.util.function.Supplier;

public class WindowsFeatureAccessibleRole extends AbstractFeatureAccessibleRole {
    final AccessBridgeHandler handler = new AccessBridgeHandler() {

        @Override
        public AccessibleRole getAccessibleRoleStringFromContext(Supplier<AccessibleRole> defaultSupplier, AccessibleContext ac) {
            Component c = getComponent(ac);
            if (c != null) {
                AccessibleRole r = WindowsFeatureAccessibleRole.this.getAccessibleRole(c);
                if (r != null)
                    return r;
            }
            return super.getAccessibleRoleStringFromContext(defaultSupplier, ac);
        }
    };

    @Override
    public void install() throws UnsupportedOperationException {
        if (!isSupported())
            throw new UnsupportedOperationException(AccessBridgeController.UNSUPPORTED_EXCEPTION_MESSAGE);
        AccessBridgeController.get().addHandler(handler);
    }

    @Override
    public void uninstall() {
        AccessBridgeController.get().removeHandler(handler);
    }

    @Override
    public boolean isRecommended() {
        return AXHelper.isWindows;
    }

    @Override
    public boolean isSupported() {
        return AccessBridgeController.get().isValid();
    }
}
