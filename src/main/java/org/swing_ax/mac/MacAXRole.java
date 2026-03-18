package org.swing_ax.mac;

import javax.accessibility.AccessibleRole;
import javax.swing.*;

public class MacAXRole extends AccessibleRole {

    public static final Object PROPERTY_MAC_AX_ROLE = "MacAXRole";

    static {
        CAccessibilityController.get().addRequestListener(new CAccessibilityController.RequestListener() {
            @Override
            public void request(CAccessibilityController.MethodInvocationRequest r) {
                if (r.getMethod().getName().equals("getAccessibleRole") &&
                        r.getArgument(0) instanceof JComponent) {
                    JComponent jc = (JComponent) r.getArgument(0);
                    MacAXRole macAXRole = (MacAXRole) jc.getClientProperty(PROPERTY_MAC_AX_ROLE);
                    if (macAXRole != null)
                        r.intercept(macAXRole.getKey());
                } else if (r.getMethod().getName().equals("invokeGetChildrenAndRoles")) {
                    r.filter(objects -> {
                        for (int a = 0; a < objects.length; a++) {
                            if (objects[a] instanceof JComponent &&
                                    a + 1 < objects.length &&
                                    objects[a + 1] instanceof AccessibleRole) {
                                JComponent jc = (JComponent) objects[a];
                                MacAXRole macAXRole = (MacAXRole) jc.getClientProperty(PROPERTY_MAC_AX_ROLE);
                                if (macAXRole != null)
                                    objects[a + 1] =  macAXRole;
                            }
                        }
                        return objects;
                    });
                }
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
