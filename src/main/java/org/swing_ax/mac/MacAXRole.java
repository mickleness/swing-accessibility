package org.swing_ax.mac;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class MacAXRole extends AccessibleRole {

    public static final Object PROPERTY_ACCESSIBLE_ROLE = "ClientAccessibleRole";

    static {
        CAccessibilityController.get().addHandler(new CAccessibilityHandler() {
            @Override
            public String getAccessibleRole(Supplier<String> defaultImplementation, Accessible a, Component c) {
                AccessibleRole clientRole = getClientAccessibleRole(c);
                if (clientRole != null) {
                    // TODO: get key from AccessibleRole (do NOT return localized toString())
                    return clientRole.toString();
                }
                return super.getAccessibleRole(defaultImplementation, a, c);
            }

            @Override
            public Object[] invokeGetChildrenAndRoles(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
                Object[] returnValue = super.invokeGetChildrenAndRoles(defaultImplementation, a, c, whichChildren, allowIgnored, ops);
                for (int i = 0; i < returnValue.length; i++) {
                    if (returnValue[i] instanceof Component &&
                            i + 1 < returnValue.length &&
                            returnValue[i + 1] instanceof AccessibleRole) {
                        Component comp = (Component) returnValue[i];
                        AccessibleRole clientRole = getClientAccessibleRole(comp);
                        if (clientRole != null)
                            returnValue[i + 1] = clientRole;
                    }
                }
                return returnValue;
            }

            /**
             * Return the AccessibleRole based on {@link MacAXRole#PROPERTY_ACCESSIBLE_ROLE}, or null if it is not defined
             */
            private AccessibleRole getClientAccessibleRole(Component c) {
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    AccessibleRole axRole = (AccessibleRole) jc.getClientProperty(PROPERTY_ACCESSIBLE_ROLE);
                    if (axRole != null)
                        return axRole;
                }
                return null;
            }
        });
    }

    public static MacAXRole AXImage = new MacAXRole("AXImage");
    public static MacAXRole AXPopUpButton = new MacAXRole("AXPopUpButton");
    public static MacAXRole AXMenuButton = new MacAXRole("AXMenuButton");
    public static MacAXRole AXLink = new MacAXRole("AXLink");
    public static MacAXRole AXValueIndicator = new MacAXRole("AXValueIndicator");
    public static MacAXRole AXIncrementor = new MacAXRole("AXIncrementor");
    public static MacAXRole AXBusyIndicator = new MacAXRole("AXBusyIndicator");
    public static MacAXRole AXProgressIndicator = new MacAXRole("AXProgressIndicator");
    public static MacAXRole AXDisclosureTriangle = new MacAXRole("AXDisclosureTriangle");
    public static MacAXRole AXColorWell = new MacAXRole("AXColorWell");
    public static MacAXRole AXTimeField = new MacAXRole("AXTimeField");
    public static MacAXRole AXDateField = new MacAXRole("AXDateField");
    public static MacAXRole AXHelpTag = new MacAXRole("AXHelpTag");
    public static MacAXRole AXLevelIndicator = new MacAXRole("AXLevelIndicator");
    public static MacAXRole AXHandle = new MacAXRole("AXHandle");
    public static MacAXRole AXPopover = new MacAXRole("AXPopover");
    public static MacAXRole AXUnknown = new MacAXRole("AXUnknown");

    protected MacAXRole(String key) {
        super(key);
    }

    public String getKey() {
        return key;
    }
}
