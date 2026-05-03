package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.AbstractFeatureAccessibleRole;
import com.pump.ax.AccessibleRoleUtils;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * This object may use replacements to apply a fix for <a href="https://bugs.openjdk.org/browse/JDK-8377745">JDK-8377745</a>
 * depending on the java version.
 */
public class MacFeatureAccessibleRole extends AbstractFeatureAccessibleRole {

    final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        protected String getAccessibleRole(Accessible a, Component c) {
            if (a instanceof JComponent) {
                JComponent jc = (JComponent) a;
                AccessibleRole returnValue = MacFeatureAccessibleRole.this.getAccessibleRole(jc);

                // I wish there was a public method AccessibleRole#getKey()

                if (returnValue instanceof MacAXRole) {
                    return ((MacAXRole)returnValue).getKey();
                } else if (returnValue != null) {
                    String key = AccessibleRoleUtils.getKey(returnValue);
                    if (key != null)
                        return key;
                    // TODO: improve logging
                    System.err.println("getAccessibleRole identified " + returnValue + " but could not identify its key");
                }
            }
            return super.getAccessibleRole(a, c);
        }

        @Override
        protected Object[] invokeGetChildrenAndRoles(Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
            Object[] returnValue = super.invokeGetChildrenAndRoles(a, c, whichChildren, allowIgnored, ops);
            replaceAccessibleRoles(returnValue, 2);
            return returnValue;
        }

        @Override
        protected Object[] getChildrenAndRolesRecursive(Accessible a, Component c, int whichChildren, boolean allowIgnored, int level) {
            Object[] returnValue = super.getChildrenAndRolesRecursive(a, c, whichChildren, allowIgnored, level);
            replaceAccessibleRoles(returnValue, 3);
            return returnValue;
        }

        private void replaceAccessibleRoles(Object[] componentsAndRoles, int arrayIncr) {
            for (int i = 0; i < componentsAndRoles.length; i += arrayIncr) {
                if (componentsAndRoles[i] instanceof Component) {
                    AccessibleRole replacementRole = MacFeatureAccessibleRole.this.getAccessibleRole((Component) componentsAndRoles[i]);
                    if (replacementRole != null)
                        componentsAndRoles[i + 1] = replacementRole;
                }
            }
        }
    };

    public MacFeatureAccessibleRole() {
        if (AXHelper.javaVersion < 27)
            setRoleReplacement(AccessibleRole.HYPERLINK, MacAXRole.AXLink);

        // TODO: update this if https://github.com/openjdk/jdk/pull/30251 is accepted
        setRoleReplacement(AccessibleRole.HEADER, MacAXRole.AXHeading);
    }

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
        return AXHelper.isMac;
    }

    @Override
    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
