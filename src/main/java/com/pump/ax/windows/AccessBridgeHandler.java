package com.pump.ax.windows;

import com.pump.ax.AbstractBridgeHandler;

import javax.accessibility.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This offer subclasses the chance to intercept several AccessBridge methods.
 * <p>
 * The AccessBridge.java file is over 7,000 lines long. This class does not
 * attempt to support all AccessBridge methods. For the time being this
 * class targets most simple methods that only use one `invokeAndWait` or `invokeLater`
 * execution.
 */
public class AccessBridgeHandler extends AbstractBridgeHandler {

    protected Supplier defaultSupplier;

    @Override
    public Object invoke(InvocationEvent invocationEvent, Method method, Supplier defaultSupplier, Object[] arguments) {
        this.defaultSupplier = Objects.requireNonNull(defaultSupplier);
        try {
            switch(method.getName()) {
                case "getRootAccessibleContext":
                    return getRootAccessibleContext((AccessibleContext) arguments[0]);
                case "getAccessibleContextAt_2":
                    return getAccessibleContextAt_2((Integer) arguments[0], (Integer) arguments[1]);
                case "getAccessibleContextWithFocus":
                    return getAccessibleContextWithFocus();
                case "getAccessibleNameFromContext":
                    return getAccessibleNameFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleDescriptionFromContext":
                    return getAccessibleDescriptionFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleRoleStringFromContext":
                    return getAccessibleRoleStringFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleStatesStringFromContext_en_US":
                    return getAccessibleStatesStringFromContext_en_US((AccessibleContext) arguments[0]);
                case "getAccessibleParentFromContext":
                    return getAccessibleParentFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleIndexInParentFromContext":
                    return getAccessibleIndexInParentFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleChildrenCountFromContext":
                    return getAccessibleChildrenCountFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleBoundsOnScreenFromContext":
                    return getAccessibleBoundsOnScreenFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleComponentFromContext":
                    return getAccessibleComponentFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleActionFromContext":
                    return getAccessibleActionFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleSelectionFromContext":
                    if (arguments.length == 2) {
                        return getAccessibleSelectionFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                    }
                    return getAccessibleSelectionFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleTextFromContext":
                    return getAccessibleTextFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleValueFromContext":
                    return getAccessibleValueFromContext((AccessibleContext) arguments[0]);
                case "getCaretLocation":
                    return getCaretLocation((AccessibleContext) arguments[0]);
                case "getAccessibleCharCountFromContext":
                    return getAccessibleCharCountFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleCaretPositionFromContext":
                    return getAccessibleCaretPositionFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleIndexAtPointFromContext":
                    return getAccessibleIndexAtPointFromContext((AccessibleContext) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleLetterAtIndexFromContext":
                    return getAccessibleLetterAtIndexFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleWordAtIndexFromContext":
                    return getAccessibleWordAtIndexFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleSentenceAtIndexFromContext":
                    return getAccessibleSentenceAtIndexFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTextSelectionStartFromContext":
                    return getAccessibleTextSelectionStartFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleTextSelectionEndFromContext":
                    return getAccessibleTextSelectionEndFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleTextSelectedTextFromContext":
                    return getAccessibleTextSelectedTextFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleAttributesAtIndexFromContext":
                    return getAccessibleAttributesAtIndexFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTextLineLeftBoundsFromContext":
                    return getAccessibleTextLineLeftBoundsFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTextLineRightBoundsFromContext":
                    return getAccessibleTextLineRightBoundsFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTextRangeFromContext":
                    return getAccessibleTextRangeFromContext((AccessibleContext) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleAttributeSetAtIndexFromContext":
                    return getAccessibleTextRangeFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTextRectAtIndexFromContext":
                    return getAccessibleTextRectAtIndexFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getCurrentAccessibleValueFromContext":
                    return getCurrentAccessibleValueFromContext((AccessibleContext) arguments[0]);
                case "getMaximumAccessibleValueFromContext":
                    return getMaximumAccessibleValueFromContext((AccessibleContext) arguments[0]);
                case "getMinimumAccessibleValueFromContext":
                    return getMinimumAccessibleValueFromContext((AccessibleContext) arguments[0]);
                case "addAccessibleSelectionFromContext":
                    addAccessibleSelectionFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                    return null;
                case "clearAccessibleSelectionFromContext":
                    clearAccessibleSelectionFromContext((AccessibleContext) arguments[0]);
                    return null;
                case "getAccessibleSelectionCountFromContext":
                    return getAccessibleSelectionCountFromContext((AccessibleContext) arguments[0]);
                case "isAccessibleChildSelectedFromContext":
                    return isAccessibleChildSelectedFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "removeAccessibleSelectionFromContext":
                    removeAccessibleSelectionFromContext((AccessibleContext) arguments[0], (Integer) arguments[1]);
                    return null;
                case "selectAllAccessibleSelectionFromContext":
                    selectAllAccessibleSelectionFromContext((AccessibleContext) arguments[0]);
                    return null;
                case "getAccessibleTableFromContext":
                    return getAccessibleTableFromContext((AccessibleContext) arguments[0]);
                case "getAccessibleTableRowCount":
                    return getAccessibleTableRowCount((AccessibleContext) arguments[0]);
                case "getAccessibleTableColumnCount":
                    return getAccessibleTableColumnCount((AccessibleContext) arguments[0]);
                case "getAccessibleTableCellAccessibleContext":
                    return getAccessibleTableCellAccessibleContext((AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleTableCellIndex":
                    return getAccessibleTableCellIndex((AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleTableCellRowExtent":
                    return getAccessibleTableCellRowExtent((AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleTableCellColumnExtent":
                    return getAccessibleTableCellColumnExtent((AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "isAccessibleTableCellSelected":
                    return isAccessibleTableCellSelected((AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleTableRowHeader":
                    return getAccessibleTableRowHeader((AccessibleContext) arguments[0]);
                case "getAccessibleTableColumnHeader":
                    return getAccessibleTableColumnHeader((AccessibleContext) arguments[0]);
                case "getAccessibleTableRowHeaderRowCount":
                    return getAccessibleTableRowHeaderRowCount((AccessibleContext) arguments[0]);
                case "getAccessibleTableRowHeaderColumnCount":
                    return getAccessibleTableRowHeaderColumnCount((AccessibleContext) arguments[0]);
                case "getAccessibleTableColumnHeaderRowCount":
                    return getAccessibleTableColumnHeaderRowCount((AccessibleContext) arguments[0]);
                case "getAccessibleTableColumnHeaderColumnCount":
                    return getAccessibleTableColumnHeaderColumnCount((AccessibleContext) arguments[0]);
                case "getAccessibleTableRowDescription":
                    return getAccessibleTableRowDescription((AccessibleTable) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTableColumnDescription":
                    return getAccessibleTableColumnDescription((AccessibleTable) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTableRowSelectionCount":
                    return getAccessibleTableRowSelectionCount((AccessibleTable) arguments[0]);
                case "getAccessibleTableRowSelections":
                    return getAccessibleTableRowSelections((AccessibleTable) arguments[0], (Integer) arguments[0]);
                case "isAccessibleTableRowSelected":
                    return isAccessibleTableRowSelected((AccessibleTable) arguments[0], (Integer) arguments[1]);
                case "isAccessibleTableColumnSelected":
                    return isAccessibleTableColumnSelected((AccessibleTable) arguments[0],  (Integer) arguments[1]);
                case "getAccessibleTableColumnSelectionCount":
                    return getAccessibleTableColumnSelectionCount((AccessibleTable) arguments[0]);
                case "getAccessibleTableColumnSelections":
                    return getAccessibleTableColumnSelections((AccessibleTable) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTableRow":
                    return getAccessibleTableRow((AccessibleTable) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTableColumn":
                    return getAccessibleTableColumn((AccessibleTable) arguments[0], (Integer) arguments[1]);
                case "getAccessibleTableIndex":
                    return getAccessibleTableIndex((AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleRelationCount":
                    return getAccessibleRelationCount((AccessibleContext) arguments[0]);
                case "getAccessibleRelationKey":
                    return getAccessibleRelationKey((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleRelationTargetCount":
                    return getAccessibleRelationTargetCount((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleRelationTarget":
                    return getAccessibleRelationTarget((AccessibleContext) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "getAccessibleHypertext":
                    return getAccessibleHypertext((AccessibleContext) arguments[0]);
                case "getAccessibleHyperlinkCount":
                    return getAccessibleHyperlinkCount((AccessibleContext) arguments[0]);
                case "getAccessibleHyperlinkText":
                    return getAccessibleHyperlinkText((AccessibleHyperlink) arguments[0]);
                case "getAccessibleHyperlinkURL":
                    return getAccessibleHyperlinkURL((AccessibleHyperlink) arguments[0]);
                case "getAccessibleHyperlinkStartIndex":
                    return getAccessibleHyperlinkStartIndex((AccessibleHyperlink) arguments[0]);
                case "getAccessibleHyperlinkEndIndex":
                    return getAccessibleHyperlinkEndIndex((AccessibleHyperlink) arguments[0]);
                case "getAccessibleHypertextLinkIndex":
                    return getAccessibleHypertextLinkIndex((AccessibleHypertext) arguments[0], (Integer) arguments[1]);
                case "activateAccessibleHyperlink":
                    return activateAccessibleHyperlink((AccessibleContext) arguments[0], (AccessibleHyperlink) arguments[1]);
                case "getMnemonic":
                    return getMnemonic((AccessibleContext) arguments[0]);
                case "getAccelerator":
                    return getAccelerator((AccessibleContext) arguments[0]);
                case "getAccessibleIconsCount":
                    return getAccessibleIconsCount((AccessibleContext) arguments[0]);
                case "getAccessibleIconDescription":
                    return getAccessibleIconDescription((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleIconHeight":
                    return getAccessibleIconHeight((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleIconWidth":
                    return getAccessibleIconWidth((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "getAccessibleActionsCount":
                    return getAccessibleActionsCount((AccessibleContext) arguments[0]);
                case "getAccessibleActionName":
                    return getAccessibleActionName((AccessibleContext) arguments[0], (Integer) arguments[1]);
                case "doAccessibleActions":
                    return doAccessibleActions((AccessibleContext) arguments[0], (String) arguments[1]);
                case "setTextContents":
                    return setTextContents((AccessibleContext) arguments[0], (String) arguments[1]);
                case "getTopLevelObject":
                    return getTopLevelObject((AccessibleContext) arguments[0]);
                case "getParentWithRole":
                    return getParentWithRole((AccessibleContext) arguments[0], (String) arguments[1]);
                case "getObjectDepth":
                    return getObjectDepth((AccessibleContext) arguments[0]);
                case "getJAWSAccessibleName":
                    return getJAWSAccessibleName((AccessibleContext) arguments[0]);
                case "requestFocus":
                    return requestFocus((AccessibleContext) arguments[0]);
                case "selectTextRange":
                    return selectTextRange((AccessibleContext) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
                case "setCaretPosition":
                    return setCaretPosition((AccessibleContext) arguments[0], (Integer) arguments[1]);

                // these are methods we'll skip. If a complaint comes up we can
                // maybe add them later:


                case "getVisibleChild":
                    // this includes an invokeAndWait, but it's only used for
                    // logging. Intercepting it would serve no use.

                // these are methods that have multiple InvocationEvents; maybe someday
                // we can add support but for now let's skip them:
                case "getAccessibleContextAt_1":
                case "getVirtualAccessibleNameFromContext":
                case "getAccessibleStatesStringFromContext":
                case "getAccessibleChildFromContext":
                case "expandStyleConstants":
                case "getAccessibleHyperlink":
                case "getActiveDescendent":
                case "_getVisibleChildrenCount":
                case "_getVisibleChild":

                // These are methods I'm skipping because I thought they're too specific
                // to be worth intercepting for most bug fixes:
                case "getAccessibleFromNativeWindowHandle":
                case "registerVirtualFrame":
                case "revokeVirtualFrame":
                case "getContextFromNativeWindowHandle":
                case "addJavaEventNotification":
                case "removeJavaEventNotification":
                case "addAccessibilityEventNotification":
                case "removeAccessibilityEventNotification":
                    // we don't intercept these (anyone want them?)
                    return defaultSupplier.get();
                default:
                    return RETURN_VALUE_UNSUPPORTED;
            }
        } finally {
            this.defaultSupplier = null;
        }
    }

    protected Rectangle getAccessibleTextRectAtIndexFromContext(AccessibleContext ac, int index) {
        return (Rectangle) defaultSupplier.get();
    }

    protected AccessibleContext getRootAccessibleContext(AccessibleContext ac) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleContextAt_2(int x, int y) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleContextWithFocus() {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected String getAccessibleNameFromContext(AccessibleContext ac) {
        return (String) defaultSupplier.get();
    }

    protected String getAccessibleDescriptionFromContext(AccessibleContext ac) {
        return (String) defaultSupplier.get();
    }

    protected AccessibleRole getAccessibleRoleStringFromContext(AccessibleContext ac) {
        return (AccessibleRole) defaultSupplier.get();
    }

    protected AccessibleStateSet getAccessibleStatesStringFromContext_en_US(AccessibleContext ac) {
        // this is not a typo; the Callable returns an AccessibleStateSet
        return (AccessibleStateSet) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleParentFromContext(AccessibleContext ac) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected int getAccessibleIndexInParentFromContext(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleChildrenCountFromContext(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected Rectangle getAccessibleBoundsOnScreenFromContext(AccessibleContext ac) {
        return (Rectangle) defaultSupplier.get();
    }

    protected AccessibleComponent getAccessibleComponentFromContext(AccessibleContext ac) {
        return (AccessibleComponent) defaultSupplier.get();
    }

    protected AccessibleAction getAccessibleActionFromContext(AccessibleContext ac) {
        return (AccessibleAction) defaultSupplier.get();
    }

    protected AccessibleSelection getAccessibleSelectionFromContext(AccessibleContext ac) {
        return (AccessibleSelection) defaultSupplier.get();
    }

    protected AccessibleText getAccessibleTextFromContext(AccessibleContext ac) {
        return (AccessibleText) defaultSupplier.get();
    }

    protected AccessibleValue getAccessibleValueFromContext(AccessibleContext ac) {
        return (AccessibleValue) defaultSupplier.get();
    }

    protected Rectangle getCaretLocation(AccessibleContext ac) {
        return (Rectangle) defaultSupplier.get();
    }

    protected int getAccessibleCharCountFromContext(AccessibleContext ac) {
        return (int) defaultSupplier.get();
    }

    protected int getAccessibleCaretPositionFromContext(AccessibleContext ac) {
        return (int) defaultSupplier.get();
    }

    protected int getAccessibleIndexAtPointFromContext(AccessibleContext ac, int x, int y) {
        return (int) defaultSupplier.get();
    }

    protected String getAccessibleLetterAtIndexFromContext(AccessibleContext ac, int index) {
        return (String) defaultSupplier.get();
    }

    protected String getAccessibleWordAtIndexFromContext(AccessibleContext ac, Integer index) {
        return (String) defaultSupplier.get();
    }

    protected String getAccessibleSentenceAtIndexFromContext(AccessibleContext ac, Integer index) {
        return (String) defaultSupplier.get();
    }

    protected int getAccessibleTextSelectionStartFromContext(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTextSelectionEndFromContext(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected String getAccessibleTextSelectedTextFromContext(AccessibleContext ac) {
        return (String) defaultSupplier.get();
    }

    protected String getAccessibleAttributesAtIndexFromContext(AccessibleContext ac, Integer index) {
        return (String) defaultSupplier.get();
    }

    protected int getAccessibleTextLineLeftBoundsFromContext(AccessibleContext ac, Integer index) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTextLineRightBoundsFromContext(AccessibleContext ac, Integer index) {
        return (Integer) defaultSupplier.get();
    }

    protected String getAccessibleTextRangeFromContext(AccessibleContext ac, Integer start, Integer end) {
        return (String) defaultSupplier.get();
    }

    protected AttributeSet getAccessibleTextRangeFromContext(AccessibleContext ac, Integer index) {
        return (AttributeSet) defaultSupplier.get();
    }

    protected Number getCurrentAccessibleValueFromContext(AccessibleContext ac) {
        return (Number) defaultSupplier.get();
    }

    protected Number getMaximumAccessibleValueFromContext(AccessibleContext ac) {
        return (Number) defaultSupplier.get();
    }

    protected Number getMinimumAccessibleValueFromContext(AccessibleContext ac) {
        return (Number) defaultSupplier.get();
    }

    protected void addAccessibleSelectionFromContext(AccessibleContext ac, int i) {
        defaultSupplier.get();
    }

    protected void clearAccessibleSelectionFromContext(AccessibleContext ac) {
        defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleSelectionFromContext(AccessibleContext ac, int i) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected int getAccessibleSelectionCountFromContext(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected boolean isAccessibleChildSelectedFromContext(AccessibleContext ac, int i) {
        return (Boolean) defaultSupplier.get();
    }

    protected void removeAccessibleSelectionFromContext(AccessibleContext ac, int i) {
        defaultSupplier.get();
    }

    protected void selectAllAccessibleSelectionFromContext(AccessibleContext ac) {
        defaultSupplier.get();
    }

    protected AccessibleTable getAccessibleTableFromContext(AccessibleContext ac) {
        return (AccessibleTable) defaultSupplier.get();
    }

    protected int getAccessibleTableRowCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableColumnCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleTableCellAccessibleContext(AccessibleTable at, int row, int column) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected int getAccessibleTableCellIndex(AccessibleTable at, int row, int column) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableCellRowExtent(AccessibleTable at, int row, int column) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableCellColumnExtent(AccessibleTable at, int row, int column) {
        return (Integer) defaultSupplier.get();
    }

    protected boolean isAccessibleTableCellSelected(AccessibleTable at, int row, int column) {
        return (Boolean) defaultSupplier.get();
    }

    protected AccessibleTable getAccessibleTableRowHeader(AccessibleContext ac) {
        return (AccessibleTable) defaultSupplier.get();
    }

    protected AccessibleTable getAccessibleTableColumnHeader(AccessibleContext ac) {
        return (AccessibleTable) defaultSupplier.get();
    }

    protected int getAccessibleTableRowHeaderRowCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableRowHeaderColumnCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableColumnHeaderRowCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableColumnHeaderColumnCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleTableRowDescription(AccessibleTable table, int row) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleTableColumnDescription(AccessibleTable at, int column) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected int getAccessibleTableRowSelectionCount(AccessibleTable at) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableRowSelections(AccessibleTable at, int i) {
        return (Integer) defaultSupplier.get();
    }

    protected boolean isAccessibleTableRowSelected(AccessibleTable at, int row) {
        return (Boolean) defaultSupplier.get();
    }

    protected boolean isAccessibleTableColumnSelected(AccessibleTable at, int column) {
        return (Boolean) defaultSupplier.get();
    }

    protected int getAccessibleTableColumnSelectionCount(AccessibleTable at) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableColumnSelections(AccessibleTable at, int i) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableRow(AccessibleTable at, int index) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableColumn(AccessibleTable at, int index) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleTableIndex(AccessibleTable at, int row, int column) {
        return (Integer) defaultSupplier.get();
    }

    protected AccessibleRelationSet getAccessibleRelationCount(AccessibleContext ac) {
        // this is not a typo: the Callable returns an AccessibleRelationSet instead of an int
        return (AccessibleRelationSet) defaultSupplier.get();
    }

    protected String getAccessibleRelationKey(AccessibleContext ac, int i) {
        return (String) defaultSupplier.get();
    }

    protected int getAccessibleRelationTargetCount(AccessibleContext ac, int i) {
        return (Integer) defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleRelationTarget(AccessibleContext ac, int i, int j) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected AccessibleHypertext getAccessibleHypertext(AccessibleContext ac) {
        return (AccessibleHypertext) defaultSupplier.get();
    }

    protected int getAccessibleHyperlinkCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected String getAccessibleHyperlinkText(AccessibleHyperlink link) {
        return (String) defaultSupplier.get();
    }

    protected String getAccessibleHyperlinkURL(AccessibleHyperlink link) {
        return (String) defaultSupplier.get();
    }

    protected int getAccessibleHyperlinkStartIndex(AccessibleHyperlink link) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleHyperlinkEndIndex(AccessibleHyperlink link) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleHypertextLinkIndex(AccessibleHypertext hypertext, int charIndex) {
        return (Integer) defaultSupplier.get();
    }

    protected boolean activateAccessibleHyperlink(AccessibleContext ac, AccessibleHyperlink link) {
        return (Boolean) defaultSupplier.get();
    }

    protected KeyStroke getMnemonic(AccessibleContext ac) {
        return (KeyStroke) defaultSupplier.get();
    }

    protected KeyStroke getAccelerator(AccessibleContext ac) {
        return (KeyStroke) defaultSupplier.get();
    }

    protected int getAccessibleIconsCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected String getAccessibleIconDescription(AccessibleContext ac, int index) {
        return (String) defaultSupplier.get();
    }

    protected int getAccessibleIconHeight(AccessibleContext ac, int index) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleIconWidth(AccessibleContext ac, int index) {
        return (Integer) defaultSupplier.get();
    }

    protected int getAccessibleActionsCount(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected String getAccessibleActionName(AccessibleContext ac, int index) {
        return (String) defaultSupplier.get();
    }

    protected boolean doAccessibleActions(AccessibleContext ac, String name) {
        return (Boolean) defaultSupplier.get();
    }

    protected boolean setTextContents(AccessibleContext ac, String text) {
        return (Boolean) defaultSupplier.get();
    }

    protected AccessibleContext getTopLevelObject (AccessibleContext ac) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected AccessibleContext getParentWithRole (AccessibleContext ac, String roleName) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected int getObjectDepth(AccessibleContext ac) {
        return (Integer) defaultSupplier.get();
    }

    protected String getJAWSAccessibleName(AccessibleContext ac) {
        return (String) defaultSupplier.get();
    }

    protected boolean requestFocus(AccessibleContext ac) {
        return (Boolean) defaultSupplier.get();
    }

    protected boolean selectTextRange(AccessibleContext ac, int startIndex, int endIndex) {
        return (Boolean) defaultSupplier.get();
    }

    protected boolean setCaretPosition(AccessibleContext ac, int position) {
        return (Boolean) defaultSupplier.get();
    }

    protected Component getComponent(AccessibleContext ac) {
        // TODO: write fallback; this may be too fragile
        Accessible accessibleParent = ac.getAccessibleParent();
        int indexInParent = ac.getAccessibleIndexInParent();
        AccessibleContext parentContext = accessibleParent == null ? null : accessibleParent.getAccessibleContext();
        Accessible comp = parentContext == null ? null : parentContext.getAccessibleChild(indexInParent);
        if (comp instanceof Component)
            return (Component) comp;
        return null;
    }
}
