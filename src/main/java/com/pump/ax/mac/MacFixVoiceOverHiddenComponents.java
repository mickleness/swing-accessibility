package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;

import javax.accessibility.Accessible;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * This prevents VoiceOver from cataloging hidden components.
 *
 * This is a work-around for <a href="https://bugs.openjdk.org/browse/JDK-8377428">JDK-8377428</a>,
 * which should be resolved in OpenJDK v27.
 */
public class MacFixVoiceOverHiddenComponents extends Feature {

    final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        protected Object[] invokeGetChildrenAndRoles(Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
            Object[] returnValue = super.invokeGetChildrenAndRoles(a, c, whichChildren, allowIgnored, ops);
            if (!allowIgnored)
                returnValue = removeHiddenComponents(returnValue, 2);
            return returnValue;
        }

        @Override
        protected Object[] getChildrenAndRolesRecursive(Accessible a, Component c, int whichChildren, boolean allowIgnored, int level) {
            Object[] returnValue = super.getChildrenAndRolesRecursive(a, c, whichChildren, allowIgnored, level);
            if (!allowIgnored)
                returnValue = removeHiddenComponents(returnValue, 3);
            return returnValue;
        }

        /**
         * Remove all Component entries that are not currently showing.
         */
        private Object[] removeHiddenComponents(Object[] components, int arrayIncr) {
            Collection<Integer> removedIndices = new HashSet<>();
            for (int i = 0; i < components.length; i += arrayIncr) {
                if (components[i] instanceof Component) {
                    Component component = (Component) components[i];
                    if (component != null && !component.isShowing()) {
                        removedIndices.add(i);
                    }
                }
            }

            if (removedIndices.isEmpty())
                return components;

            List<Object> returnValue = new ArrayList<>(arrayIncr * (components.length - removedIndices.size()));
            for (int i = 0; i < components.length; i += arrayIncr) {
                if (!removedIndices.contains(i)) {
                    for (int a = 0; a < arrayIncr; a++) {
                        returnValue.add(components[i + a]);
                    }
                }
            }

            return returnValue.toArray(new Object[0]);
        }
    };

    @Override
    public void install() throws Exception {
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
        // I didn't test JDKs 9-12.
        // I briefly tested 8, and observed that it did
        // not reproduce this problem. (I did not explore why.)
        return AXHelper.isMac && 13 <= AXHelper.javaVersion && AXHelper.javaVersion <= 26;
    }

    @Override
    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
