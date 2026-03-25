package org.swing_ax.mac;

import javax.accessibility.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * This class handles calls to static CAccessibility and CAccessibleText methods.
 * <p>
 * By default every method simply calls <code>defaultImplementation.get()</code> or <code>defaultImplementation.run()</code>,
 * but subclasses can override any method and either ignore `defaultImplementation` or modify/filter its return value.
 * </p>
 */
public abstract class CAccessibilityHandler {

    public String getAccessibleActionDescription(Supplier<String> defaultImplementation, AccessibleAction aa, int index, Component c) {
        return defaultImplementation.get();
    }

    public void doAccessibleAction(Runnable defaultImplementation, AccessibleAction aa, int index, Component c) {
        defaultImplementation.run();
    }

    public Dimension getSize(Supplier<Dimension> defaultImplementation, AccessibleComponent ac, Component c) {
        return defaultImplementation.get();
    }

    public AccessibleSelection getAccessibleSelection(Supplier<AccessibleSelection> defaultImplementation, AccessibleContext ac, Component c) {
        return defaultImplementation.get();
    }

    public Accessible ax_getAccessibleSelection(Supplier<Accessible> defaultImplementation, AccessibleContext ac, int index, Component c) {
        return defaultImplementation.get();
    }

    public void addAccessibleSelection(Runnable defaultImplementation, AccessibleContext ac, int index, Component c) {
        defaultImplementation.run();
    }

    public AccessibleContext getAccessibleContext(Supplier<AccessibleContext> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public boolean isAccessibleChildSelected(Supplier<Boolean> defaultImplementation, Accessible a, int index, Component c) {
        return defaultImplementation.get();
    }

    public AccessibleStateSet getAccessibleStateSet(Supplier<AccessibleStateSet> defaultImplementation, AccessibleContext ac, Component c) {
        return defaultImplementation.get();
    }

    public boolean contains(Supplier<Boolean> defaultImplementation, AccessibleContext ac, AccessibleState as, Component c) {
        return defaultImplementation.get();
    }

    public String getAccessibleRole(Supplier<String> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public Point getLocationOnScreen(Supplier<Point> defaultImplementation, AccessibleComponent ac, Component c) {
        return defaultImplementation.get();
    }

    public int getCharCount(Supplier<Integer> defaultImplementation, AccessibleText at, Component c) {
        return defaultImplementation.get();
    }

    public Accessible getAccessibleParent(Supplier<Accessible> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public int getAccessibleIndexInParent(Supplier<Integer> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public AccessibleComponent getAccessibleComponent(Supplier<AccessibleComponent> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public AccessibleValue getAccessibleValue(Supplier<AccessibleValue> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public String getAccessibleName(Supplier<String> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public AccessibleText getAccessibleText(Supplier<AccessibleText> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public String getAccessibleDescription(Supplier<String> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public boolean isFocusTraversable(Supplier<Boolean> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public Accessible accessibilityHitTest(Supplier<Accessible> defaultImplementation, Container parent, float hitPointX, float hitPointY) {
        return defaultImplementation.get();
    }

    public AccessibleAction getAccessibleAction(Supplier<AccessibleAction> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    // TODO: reevaluate (lambdas are a different case)
//    default int getAccessibleActionCount(Supplier<Integer> defaultImplementation, AccessibleAction aa, Component c) {
//        if (aa == null) return 0;
//
//        return invokeAndWait(aa::getAccessibleActionCount, c);
//    }

    public boolean isEnabled(Supplier<Boolean> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public void requestFocus(Runnable defaultImplementation, Accessible a, Component c) {
        defaultImplementation.run();
    }

    public void requestSelection(Runnable defaultImplementation, Accessible a, Component c) {
        defaultImplementation.run();
    }

    public Number getMaximumAccessibleValue(Supplier<Number> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public Number getMinimumAccessibleValue(Supplier<Number> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public String getAccessibleRoleDisplayString(Supplier<String> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public Number getCurrentAccessibleValue(Supplier<Number> defaultImplementation, AccessibleValue av, Component c) {
        return defaultImplementation.get();
    }

    public Accessible getFocusOwner(Supplier<Accessible> defaultImplementation, Component c) {
        return defaultImplementation.get();
    }

    public boolean[] getInitialAttributeStates(Supplier<boolean[]> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    // TODO: reevaluate
//    // Duplicated from JavaComponentAccessibility
//    // Note that values >=0 are indexes into the child array
//    @Native static final int JAVA_AX_ALL_CHILDREN = -1;
//    @Native static final int JAVA_AX_SELECTED_CHILDREN = -2;
//    @Native static final int JAVA_AX_VISIBLE_CHILDREN = -3;

    public Object[] invokeGetChildrenAndRoles(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
        return defaultImplementation.get();
    }

    // This method is called from the native
    // Each child takes up three entries in the array: one for itself, one for its role, and one for the recursion level
    public Object[] getChildrenAndRolesRecursive(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, int level) {
        return defaultImplementation.get();
    }

    public Accessible getAccessibleCurrentAccessible(Supplier<Accessible> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public Accessible getAccessibleComboboxValue(Supplier<Accessible> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public Accessible getCurrentAccessiblePopupMenu(Supplier<Accessible> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    // TODO: reevaluate
//    @Native private static final int JAVA_AX_ROWS = 1;
//    @Native private static final int JAVA_AX_COLS = 2;

    public int getTableInfo(Supplier<Integer> defaultImplementation, Accessible a, Component c,
                                   int info) {
        return defaultImplementation.get();
    }

    public int[] getTableSelectedInfo(Supplier<int[]> defaultImplementation, Accessible a, Component c,
                                              int info) {
        return defaultImplementation.get();
    }

    public Object[] getChildren(Supplier<Object[]> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public long getAWTView(Supplier<Long> defaultImplementation, Accessible a) {
        return defaultImplementation.get();
    }

    public boolean isTreeRootVisible(Supplier<Boolean> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    // from CAccessibleText:

    public AccessibleEditableText getAccessibleEditableText(Supplier<AccessibleEditableText> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public String getSelectedText(Supplier<String> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public void setSelectedText(Runnable defaultImplementation, Accessible a, Component c, String newText) {
        defaultImplementation.run();
    }

    public void setSelectedTextRange(Runnable defaultImplementation, Accessible a, Component c, int startIndex, int endIndex) {
        defaultImplementation.run();
    }

    public String getTextRange(Supplier<String> defaultImplementation, AccessibleEditableText aet, int start, int stop, Component c) {
        return defaultImplementation.get();
    }

    public int getCharacterIndexAtPosition(Supplier<Integer> defaultImplementation, Accessible a, Component c, int x, int y) {
        return defaultImplementation.get();
    }

    public int[] getSelectedTextRange(Supplier<int[]> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }


    public int[] getVisibleCharacterRange(Supplier<int[]> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public int getLineNumberForIndex(Supplier<Integer> defaultImplementation, Accessible a, Component c, int index) {
        return defaultImplementation.get();
    }

    public int getLineNumberForInsertionPoint(Supplier<Integer> defaultImplementation, Accessible a, Component c) {
        return defaultImplementation.get();
    }

    public int[] getRangeForLine(Supplier<int[]> defaultImplementation, Accessible a, Component c, int line) {
        return defaultImplementation.get();
    }

    public int[] getRangeForIndex(Supplier<int[]> defaultImplementation, Accessible a, Component c, int index) {
        return defaultImplementation.get();
    }

    public double[] getBoundsForRange(Supplier<double[]> defaultImplementation, Accessible a, Component c, int location, int length) {
        return defaultImplementation.get();
    }

    public String getStringForRange(Supplier<String> defaultImplementation, Accessible a, Component c, int location, int length) {
        return defaultImplementation.get();
    }
}
