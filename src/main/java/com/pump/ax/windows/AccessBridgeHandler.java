package com.pump.ax.windows;

import com.pump.ax.AbstractBridgeHandler;

import javax.accessibility.*;
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
