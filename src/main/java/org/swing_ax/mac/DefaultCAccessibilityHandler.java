package org.swing_ax.mac;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class DefaultCAccessibilityHandler extends CAccessibilityHandler {
    @Override
    public String getAccessibleActionDescription(Supplier<String> defaultImplementation, AccessibleAction aa, int index, Component c) {
        return super.getAccessibleActionDescription(defaultImplementation, aa, index, c);
    }

    @Override
    public String getAccessibleRole(Supplier<String> defaultImplementation, Accessible a, Component c) {
        return super.getAccessibleRole(defaultImplementation, a, c);
    }
}
