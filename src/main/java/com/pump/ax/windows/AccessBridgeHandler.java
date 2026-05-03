package com.pump.ax.windows;

import com.pump.ax.AbstractBridgeHandler;

import javax.accessibility.*;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Method;
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


    @Override
    public Object invoke(InvocationEvent invocationEvent, Method method, Supplier defaultSupplier, Runnable defaultRunnable, Object[] arguments) {
        switch(method.getName()) {
            case "getRootAccessibleContext":
                return getRootAccessibleContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleContextAt_2":
                return getAccessibleContextAt_2(defaultSupplier, (Integer) arguments[0], (Integer) arguments[1]);
            case "getAccessibleContextWithFocus":
                return getAccessibleContextWithFocus(defaultSupplier);
            case "getAccessibleNameFromContext":
                return getAccessibleNameFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleDescriptionFromContext":
                return getAccessibleDescriptionFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleRoleStringFromContext":
                return getAccessibleRoleStringFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleStatesStringFromContext_en_US":
                return getAccessibleStatesStringFromContext_en_US(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleParentFromContext":
                return getAccessibleParentFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleIndexInParentFromContext":
                return getAccessibleIndexInParentFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleChildrenCountFromContext":
                return getAccessibleChildrenCountFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleBoundsOnScreenFromContext":
                return getAccessibleBoundsOnScreenFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleComponentFromContext":
                return getAccessibleComponentFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleActionFromContext":
                return getAccessibleActionFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleSelectionFromContext":
                if (arguments.length == 2) {
                    return getAccessibleSelectionFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
                }
                return getAccessibleSelectionFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTextFromContext":
                return getAccessibleTextFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleValueFromContext":
                return getAccessibleValueFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getCaretLocation":
                return getCaretLocation(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleCharCountFromContext":
                return getAccessibleCharCountFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleCaretPositionFromContext":
                return getAccessibleCaretPositionFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleIndexAtPointFromContext":
                return getAccessibleIndexAtPointFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "getAccessibleLetterAtIndexFromContext":
                return getAccessibleLetterAtIndexFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleWordAtIndexFromContext":
                return getAccessibleWordAtIndexFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleSentenceAtIndexFromContext":
                return getAccessibleSentenceAtIndexFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleTextSelectionStartFromContext":
                return getAccessibleTextSelectionStartFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTextSelectionEndFromContext":
                return getAccessibleTextSelectionEndFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTextSelectedTextFromContext":
                return getAccessibleTextSelectedTextFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleAttributesAtIndexFromContext":
                return getAccessibleAttributesAtIndexFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleTextLineLeftBoundsFromContext":
                return getAccessibleTextLineLeftBoundsFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleTextLineRightBoundsFromContext":
                return getAccessibleTextLineRightBoundsFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleTextRangeFromContext":
                return getAccessibleTextRangeFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "getAccessibleAttributeSetAtIndexFromContext":
                return getAccessibleTextRangeFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getAccessibleTextRectAtIndexFromContext":
                return getAccessibleTextRectAtIndexFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "getCurrentAccessibleValueFromContext":
                return getCurrentAccessibleValueFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getMaximumAccessibleValueFromContext":
                return getMaximumAccessibleValueFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getMinimumAccessibleValueFromContext":
                return getMinimumAccessibleValueFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "addAccessibleSelectionFromContext":
                addAccessibleSelectionFromContext(defaultRunnable, (AccessibleContext) arguments[0], (Integer) arguments[1]);
                return null;
            case "clearAccessibleSelectionFromContext":
                clearAccessibleSelectionFromContext(defaultRunnable, (AccessibleContext) arguments[0]);
                return null;
            case "getAccessibleSelectionCountFromContext":
                return getAccessibleSelectionCountFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "isAccessibleChildSelectedFromContext":
                return isAccessibleChildSelectedFromContext(defaultSupplier, (AccessibleContext) arguments[0], (Integer) arguments[1]);
            case "removeAccessibleSelectionFromContext":
                removeAccessibleSelectionFromContext(defaultRunnable, (AccessibleContext) arguments[0], (Integer) arguments[1]);
                return null;
            case "selectAllAccessibleSelectionFromContext":
                selectAllAccessibleSelectionFromContext(defaultRunnable, (AccessibleContext) arguments[0]);
                return null;
            case "getAccessibleTableFromContext":
                return getAccessibleTableFromContext(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableRowCount":
                return getAccessibleTableRowCount(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableColumnCount":
                return getAccessibleTableColumnCount(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableCellAccessibleContext":
                return getAccessibleTableCellAccessibleContext(defaultSupplier,
                        (AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "getAccessibleTableCellIndex":
                return getAccessibleTableCellIndex(defaultSupplier,
                        (AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "getAccessibleTableCellRowExtent":
                return getAccessibleTableCellRowExtent(defaultSupplier,
                        (AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "getAccessibleTableCellColumnExtent":
                return getAccessibleTableCellColumnExtent(defaultSupplier,
                        (AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "isAccessibleTableCellSelected":
                return isAccessibleTableCellSelected(defaultSupplier,
                        (AccessibleTable) arguments[0], (Integer) arguments[1], (Integer) arguments[2]);
            case "getAccessibleTableRowHeader":
                return getAccessibleTableRowHeader(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableColumnHeader":
                return getAccessibleTableColumnHeader(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableRowHeaderRowCount":
                return getAccessibleTableRowHeaderRowCount(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableRowHeaderColumnCount":
                return getAccessibleTableRowHeaderColumnCount(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableColumnHeaderRowCount":
                return getAccessibleTableColumnHeaderRowCount(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableColumnHeaderColumnCount":
                return getAccessibleTableColumnHeaderColumnCount(defaultSupplier, (AccessibleContext) arguments[0]);
            case "getAccessibleTableRowDescription":
                return getAccessibleTableRowDescription(defaultSupplier, (AccessibleTable) arguments[0], (Integer) arguments[1]);
            case "getAccessibleTableColumnDescription":
                return getAccessibleTableColumnDescription(defaultSupplier, (AccessibleTable) arguments[0], (Integer) arguments[1]);

            default:
                return RETURN_VALUE_UNSUPPORTED;
        }
    }

    protected Rectangle getAccessibleTextRectAtIndexFromContext(Supplier<Rectangle> defaultSupplier, AccessibleContext ac, int index) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getRootAccessibleContext(Supplier<AccessibleContext> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleContextAt_2(Supplier<AccessibleContext> defaultSupplier, int x, int y) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleContextWithFocus(Supplier<AccessibleContext> defaultSupplier) {
        return defaultSupplier.get();
    }

    protected String getAccessibleNameFromContext(Supplier<String> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected String getAccessibleDescriptionFromContext(Supplier<String> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleRole getAccessibleRoleStringFromContext(Supplier<AccessibleRole> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleStateSet getAccessibleStatesStringFromContext_en_US(Supplier<AccessibleStateSet> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleParentFromContext(Supplier<AccessibleContext> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleIndexInParentFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleChildrenCountFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected Rectangle getAccessibleBoundsOnScreenFromContext(Supplier<Rectangle> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleComponent getAccessibleComponentFromContext(Supplier<AccessibleComponent> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleAction getAccessibleActionFromContext(Supplier<AccessibleAction> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleSelection getAccessibleSelectionFromContext(Supplier<AccessibleSelection> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleText getAccessibleTextFromContext(Supplier<AccessibleText> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleValue getAccessibleValueFromContext(Supplier<AccessibleValue> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected Rectangle getCaretLocation(Supplier<Rectangle> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleCharCountFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleCaretPositionFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleIndexAtPointFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac, int x, int y) {
        return defaultSupplier.get();
    }

    protected String getAccessibleLetterAtIndexFromContext(Supplier<String> defaultSupplier, AccessibleContext ac, int index) {
        return defaultSupplier.get();
    }

    protected String getAccessibleWordAtIndexFromContext(Supplier<String> defaultSupplier, AccessibleContext ac, Integer index) {
        return defaultSupplier.get();
    }

    protected String getAccessibleSentenceAtIndexFromContext(Supplier<String> defaultSupplier, AccessibleContext ac, Integer index) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTextSelectionStartFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTextSelectionEndFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected String getAccessibleTextSelectedTextFromContext(Supplier<String> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected String getAccessibleAttributesAtIndexFromContext(Supplier<String> defaultSupplier, AccessibleContext ac, Integer index) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTextLineLeftBoundsFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac, Integer index) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTextLineRightBoundsFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac, Integer index) {
        return defaultSupplier.get();
    }

    protected String getAccessibleTextRangeFromContext(Supplier<String> defaultSupplier, AccessibleContext ac, Integer start, Integer end) {
        return defaultSupplier.get();
    }

    protected AttributeSet getAccessibleTextRangeFromContext(Supplier<AttributeSet> defaultSupplier, AccessibleContext ac, Integer index) {
        return defaultSupplier.get();
    }

    protected Number getCurrentAccessibleValueFromContext(Supplier<Number> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected Number getMaximumAccessibleValueFromContext(Supplier<Number> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected Number getMinimumAccessibleValueFromContext(Supplier<Number> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected void addAccessibleSelectionFromContext(Runnable defaultRunnable, AccessibleContext ac, int i) {
        defaultRunnable.run();
    }

    protected void clearAccessibleSelectionFromContext(Runnable defaultRunnable, AccessibleContext ac) {
        defaultRunnable.run();
    }

    protected AccessibleContext getAccessibleSelectionFromContext(Supplier<AccessibleContext> defaultSupplier, AccessibleContext ac, int i) {
        return defaultSupplier.get();
    }

    protected int getAccessibleSelectionCountFromContext(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected boolean isAccessibleChildSelectedFromContext(Supplier<Boolean> defaultSupplier, AccessibleContext ac, int i) {
        return defaultSupplier.get();
    }

    protected void removeAccessibleSelectionFromContext(Runnable defaultRunnable, AccessibleContext ac, int i) {
        defaultRunnable.run();
    }

    protected void selectAllAccessibleSelectionFromContext(Runnable defaultRunnable, AccessibleContext ac) {
        defaultRunnable.run();
    }

    protected AccessibleTable getAccessibleTableFromContext(Supplier<AccessibleTable> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableRowCount(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableColumnCount(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleTableCellAccessibleContext(Supplier<AccessibleContext> defaultSupplier,
                                                                      AccessibleTable at, int row, int column) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableCellIndex(Supplier<Integer> defaultSupplier, AccessibleTable at, int row, int column) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableCellRowExtent(Supplier<Integer> defaultSupplier, AccessibleTable at, int row, int column) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableCellColumnExtent(Supplier<Integer> defaultSupplier, AccessibleTable at, int row, int column) {
        return defaultSupplier.get();
    }

    protected boolean isAccessibleTableCellSelected(Supplier<Boolean> defaultSupplier, AccessibleTable at, int row, int column) {
        return defaultSupplier.get();
    }

    protected AccessibleTable getAccessibleTableRowHeader(Supplier<AccessibleTable> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleTable getAccessibleTableColumnHeader(Supplier<AccessibleTable> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableRowHeaderRowCount(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableRowHeaderColumnCount(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableColumnHeaderRowCount(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected int getAccessibleTableColumnHeaderColumnCount(Supplier<Integer> defaultSupplier, AccessibleContext ac) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleTableRowDescription(Supplier<AccessibleContext> defaultSupplier, AccessibleTable table, int row) {
        return defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleTableColumnDescription(Supplier<AccessibleContext> defaultSupplier, AccessibleTable at, int column) {
        return defaultSupplier.get();
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
