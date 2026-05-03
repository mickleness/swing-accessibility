package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleRole;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This prevents VoiceOver from announcing a toggle/radio/checkbox button when
 * focus shifts to the next component.
 *
 * This is a work-around for <a href="https://bugs.openjdk.org/browse/JDK-8377936">JDK-8377936</a>.
 */
public class FixAnnouncePrevFocusedComponent extends Feature {

    final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        protected void requestFocus(Accessible a, Component c) {
            // normally (without this intervention) CAccessible's AXChangeNotifier will receive
            // a AccessibleStateSet notification when the current focus owner loses the keyboard focus.
            // As a result, it will fire a CAccessible.valueChanged message. VoiceOver ends up reading
            // the prev focus owner's description, as if the user meant to interact with it.

            // so to avoid this: we'll temporarily remove the FocusListener that updates AXChangeNotifier.
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            AccessibleRole focusOwnerRole = focusOwner == null ? null : focusOwner.getAccessibleContext().getAccessibleRole();
            boolean isToggle = focusOwnerRole == AccessibleRole.TOGGLE_BUTTON ||
                    focusOwnerRole == AccessibleRole.RADIO_BUTTON ||
                    focusOwnerRole == AccessibleRole.CHECK_BOX;
            if (isToggle) {
                FocusListener[] listeners = focusOwner.getFocusListeners();
                for (FocusListener listener : listeners) {
                    if (listener.getClass().getName().equals("java.awt.Component$AccessibleAWTComponent$AccessibleAWTFocusHandler")) {
                        focusOwner.removeFocusListener(listener);
                        focusOwner.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                focusOwner.addFocusListener(listener);
                                focusOwner.removeFocusListener(this);
                            }

                            @Override
                            public void focusLost(FocusEvent e) {
                                focusGained(e);
                            }
                        });
                        break;
                    }
                }
            }
            super.requestFocus(a, c);
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
        return AXHelper.isMac && AXHelper.javaVersion >= 14 && AXHelper.javaVersion < 27;
    }

    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
