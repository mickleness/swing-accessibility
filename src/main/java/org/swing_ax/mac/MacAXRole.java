package org.swing_ax.mac;

import javax.accessibility.AccessibleRole;

public class MacAXRole extends AccessibleRole {

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
