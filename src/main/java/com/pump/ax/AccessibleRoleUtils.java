package com.pump.ax;

import javax.accessibility.*;
import java.util.HashMap;
import java.util.Map;

public class AccessibleRoleUtils {

    static Map<AccessibleRole, String> roleToKey = new HashMap<>();

    static {
        roleToKey.put(AccessibleRole.ALERT, "alert");
        roleToKey.put(AccessibleRole.COLUMN_HEADER, "columnheader");
        roleToKey.put(AccessibleRole.CANVAS, "canvas");
        roleToKey.put(AccessibleRole.COMBO_BOX, "combobox");
        roleToKey.put(AccessibleRole.DESKTOP_ICON, "desktopicon");
        roleToKey.put(AccessibleRole.HTML_CONTAINER, "htmlcontainer");
        roleToKey.put(AccessibleRole.INTERNAL_FRAME, "internalframe");
        roleToKey.put(AccessibleRole.DESKTOP_PANE, "desktoppane");
        roleToKey.put(AccessibleRole.OPTION_PANE, "optionpane");
        roleToKey.put(AccessibleRole.WINDOW, "window");
        roleToKey.put(AccessibleRole.FRAME, "frame");
        roleToKey.put(AccessibleRole.DIALOG, "dialog");
        roleToKey.put(AccessibleRole.COLOR_CHOOSER, "colorchooser");
        roleToKey.put(AccessibleRole.DIRECTORY_PANE, "directorypane");
        roleToKey.put(AccessibleRole.FILE_CHOOSER, "filechooser");
        roleToKey.put(AccessibleRole.FILLER, "filler");
        roleToKey.put(AccessibleRole.HYPERLINK, "hyperlink");
        roleToKey.put(AccessibleRole.ICON, "icon");
        roleToKey.put(AccessibleRole.LABEL, "label");
        roleToKey.put(AccessibleRole.ROOT_PANE, "rootpane");
        roleToKey.put(AccessibleRole.GLASS_PANE, "glasspane");
        roleToKey.put(AccessibleRole.LAYERED_PANE, "layeredpane");
        roleToKey.put(AccessibleRole.LIST, "list");
        roleToKey.put(AccessibleRole.LIST_ITEM, "listitem");
        roleToKey.put(AccessibleRole.MENU_BAR, "menubar");
        roleToKey.put(AccessibleRole.POPUP_MENU, "popupmenu");
        roleToKey.put(AccessibleRole.MENU, "menu");
        roleToKey.put(AccessibleRole.MENU_ITEM, "menuitem");
        roleToKey.put(AccessibleRole.SEPARATOR, "separator");
        roleToKey.put(AccessibleRole.PAGE_TAB_LIST, "pagetablist");
        roleToKey.put(AccessibleRole.PAGE_TAB, "pagetab");
        roleToKey.put(AccessibleRole.PANEL, "panel");
        roleToKey.put(AccessibleRole.PROGRESS_BAR, "progressbar");
        roleToKey.put(AccessibleRole.PASSWORD_TEXT, "passwordtext");
        roleToKey.put(AccessibleRole.PUSH_BUTTON, "pushbutton");
        roleToKey.put(AccessibleRole.TOGGLE_BUTTON, "togglebutton");
        roleToKey.put(AccessibleRole.CHECK_BOX, "checkbox");
        roleToKey.put(AccessibleRole.RADIO_BUTTON, "radiobutton");
        roleToKey.put(AccessibleRole.ROW_HEADER, "rowheader");
        roleToKey.put(AccessibleRole.SCROLL_PANE, "scrollpane");
        roleToKey.put(AccessibleRole.SCROLL_BAR, "scrollbar");
        roleToKey.put(AccessibleRole.VIEWPORT, "viewport");
        roleToKey.put(AccessibleRole.SLIDER, "slider");
        roleToKey.put(AccessibleRole.SPLIT_PANE, "splitpane");
        roleToKey.put(AccessibleRole.TABLE, "table");
        roleToKey.put(AccessibleRole.TEXT, "text");
        roleToKey.put(AccessibleRole.TREE, "tree");
        roleToKey.put(AccessibleRole.TOOL_BAR, "toolbar");
        roleToKey.put(AccessibleRole.TOOL_TIP, "tooltip");
        roleToKey.put(AccessibleRole.AWT_COMPONENT, "awtcomponent");
        roleToKey.put(AccessibleRole.SWING_COMPONENT, "swingcomponent");
        roleToKey.put(AccessibleRole.UNKNOWN, "unknown");
        roleToKey.put(AccessibleRole.STATUS_BAR, "statusbar");
        roleToKey.put(AccessibleRole.DATE_EDITOR, "dateeditor");
        roleToKey.put(AccessibleRole.SPIN_BOX, "spinbox");
        roleToKey.put(AccessibleRole.FONT_CHOOSER, "fontchooser");
        roleToKey.put(AccessibleRole.GROUP_BOX, "groupbox");
        roleToKey.put(AccessibleRole.HEADER, "header");
        roleToKey.put(AccessibleRole.FOOTER, "footer");
        roleToKey.put(AccessibleRole.PARAGRAPH, "paragraph");
        roleToKey.put(AccessibleRole.RULER, "ruler");
        roleToKey.put(AccessibleRole.EDITBAR, "editbar");
        roleToKey.put(AccessibleRole.PROGRESS_MONITOR, "progressMonitor");
    }

    public static String getKey(AccessibleRole role) {
        // oof. I wish AccessibleRole included a method like `getKey()`.
        return roleToKey.get(role);
    }
}
