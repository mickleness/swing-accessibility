package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;
import com.pump.ax.AccessibleRoleUtils;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This lets you use a client property ({@link #PROPERTY_ACCESSIBLE_ROLE}) to determine
 * a JComponent's AccessibleRole. For example: if you have a JButton and you want its
 * role to be AccessibleRole.HYPERLINK, then you can call
 * `b.putClientProperty(PROPERTY_ACCESSIBLE_ROLE, AccessibleRole.HYPERLINK)`.
 *
 * You can also assign universal replacements via {@link #setRoleReplacement(AccessibleRole, AccessibleRole)}.
 *
 * This object may use replacements to apply a fix for <a href="https://bugs.openjdk.org/browse/JDK-8377745">JDK-8377745</a>
 * depending on the java version.
 */
public class FeatureAccessibleRole extends Feature {

    public static final String PROPERTY_ACCESSIBLE_ROLE = "accessible role";

    final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        public String getAccessibleRole(Supplier<String> defaultImplementation, Accessible a, Component c) {
            if (a instanceof JComponent) {
                JComponent jc = (JComponent) a;
                AccessibleRole returnValue = getReplacementAccessibleRole(jc);

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
            return super.getAccessibleRole(defaultImplementation, a, c);
        }

        @Override
        public Object[] invokeGetChildrenAndRoles(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
            Object[] returnValue = super.invokeGetChildrenAndRoles(defaultImplementation, a, c, whichChildren, allowIgnored, ops);
            replaceAccessibleRoles(returnValue, 2);
            return returnValue;
        }

        @Override
        public Object[] getChildrenAndRolesRecursive(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, int level) {
            Object[] returnValue = super.getChildrenAndRolesRecursive(defaultImplementation, a, c, whichChildren, allowIgnored, level);
            replaceAccessibleRoles(returnValue, 3);
            return returnValue;
        }

        private void replaceAccessibleRoles(Object[] componentsAndRoles, int arrayIncr) {
            for (int i = 0; i < componentsAndRoles.length; i += arrayIncr) {
                if (componentsAndRoles[i] instanceof Component) {
                    AccessibleRole replacementRole = getReplacementAccessibleRole((Component) componentsAndRoles[i]);
                    if (replacementRole != null)
                        componentsAndRoles[i + 1] = replacementRole;
                }
            }
        }

        /**
         * Return the preferred AccessibleRole of a Component, or null
         * if we should use the default `getAccessibleContext().getAccessibleRole()`
         */
        private AccessibleRole getReplacementAccessibleRole(Component component) {
            AccessibleRole returnValue = null;
            if (isClientPropertyActive() && component instanceof JComponent) {
                JComponent jc = (JComponent) component;
                returnValue = (AccessibleRole) jc.getClientProperty(PROPERTY_ACCESSIBLE_ROLE);
            }
            if (returnValue == null) {
                AccessibleContext axContext = component.getAccessibleContext();
                if (axContext != null)
                    returnValue = axContext.getAccessibleRole();
            }
            if (returnValue != null) {
                AccessibleRole replacementRole = getRoleReplacement(returnValue);
                if (replacementRole != null)
                    returnValue = replacementRole;
            }

            return returnValue;
        }
    };

    private Map<AccessibleRole, AccessibleRole> replacementMap = new HashMap<>();
    private boolean isClientPropertyActive = true;

    public FeatureAccessibleRole() {
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

    public boolean isClientPropertyActive() {
        return isClientPropertyActive;
    }

    /**
     * Toggle whether the {@link #PROPERTY_ACCESSIBLE_ROLE} is consulted.
     *
     * If this property is toggled off then the client property is not consulted,
     * but replacemenet roles are still consulted.
     */
    public void setClientPropertyActive(boolean b) {
        isClientPropertyActive = b;
    }

    /**
     * Return an AccessibleRole that should replace incoming role.
     *
     * For example: in some configurations AccessibleRole.HYPERLINK
     * may always be replaced with MacAXRole.AXLink
     */
    public AccessibleRole getRoleReplacement(AccessibleRole role) {
        return replacementMap.get(role);
    }

    public void setRoleReplacement(AccessibleRole role, AccessibleRole replacement) {
        replacementMap.put(role, replacement);
    }

    /**
     * Return an unmodifiable view of the current replacement map.
     */
    public Map<AccessibleRole, AccessibleRole> getReplacementMap() {
        return Collections.unmodifiableMap(replacementMap);
    }
}
