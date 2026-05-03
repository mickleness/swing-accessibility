package com.pump.ax;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This lets you use a client property ({@link #PROPERTY_ACCESSIBLE_ROLE}) to determine
 * a JComponent's AccessibleRole. For example: if you have a JButton and you want its
 * role to be AccessibleRole.HYPERLINK, then you can call
 * `b.putClientProperty(PROPERTY_ACCESSIBLE_ROLE, AccessibleRole.HYPERLINK)`.
 *
 * You can also assign universal replacements via {@link #setRoleReplacement(AccessibleRole, AccessibleRole)}.
 */
public abstract class AbstractFeatureAccessibleRole extends Feature {

    public static final String PROPERTY_ACCESSIBLE_ROLE = "accessible role";

    private final Map<AccessibleRole, AccessibleRole> replacementMap = new HashMap<>();
    private boolean isClientPropertyActive = true;

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

    /**
     * Return the preferred AccessibleRole of a Component, or null.
     *
     * This may check the client property {@link #PROPERTY_ACCESSIBLE_ROLE},
     * or it may use AccessibleContext.getAccessibleRole().
     *
     * Then if the current role is passed through {@link #getReplacementMap()}
     * if possible.
     */
    protected AccessibleRole getAccessibleRole(Component component) {
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
}
