package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;

import javax.accessibility.AccessibleAction;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This de-localized the action description, which lets VoiceOver
 * correctly trigger buttons using their AccessibleAction methods.
 *
 * This is a work-around for <a href="https://bugs.openjdk.org/browse/JDK-8377938">JDK-8377938</a>.
 */
public class FixLocalizedActionDescription extends Feature {

    final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        public String getAccessibleActionDescription(Supplier<String> defaultImplementation, AccessibleAction aa, int index, Component c) {
            String returnValue = super.getAccessibleActionDescription(defaultImplementation, aa, index, c);
            if (Objects.equals(UIManager.getString("AbstractButton.clickText"), returnValue)) {
                return AccessibleAction.CLICK;
            }
            return returnValue;
        }
    };

    @Override
    public void install() throws UnsupportedOperationException {
        if (!isSupported())
            throw new UnsupportedOperationException(CAccessibilityController.UNSUPPORTED_EXCEPTION_MESSAGE);
        CAccessibilityController.get().addHandler(handler);
    }

    @Override
    public void uninstall() {
        CAccessibilityController.get().removeHandler(handler);
    }

    @Override
    public boolean isRecommended() {
        // TODO: add jdk version info if 8377938 is resolved
        return AXHelper.isMac;
    }

    @Override
    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
