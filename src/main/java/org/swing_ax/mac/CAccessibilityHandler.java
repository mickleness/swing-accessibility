package org.swing_ax.mac;

import javax.accessibility.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * This class handles calls to static CAccessibility and CAccessibleText methods.
 * <p>
 * By default every method simply calls <code>defaultImplementation.get()</code> or <code>defaultImplementation.run()</code>,
 * but subclasses can override any method and either ignore `defaultImplementation` or modify/filter its return value.
 * </p>
 */
public abstract class CAccessibilityHandler {

    public static final Object RETURN_VALUE_UNSUPPORTED = new Object();

    /**
     * Return a value for a given CAccessibility or CAccessibleText metthod, or return RETURN_VALUE_UNSUPPORTED.
     * <p>
     * Subclasses usually shouldn't need to override this method unless they need to correct the incoming arguments.
     */
    public Object invoke(Method method, Supplier defaultSupplier, Runnable defaultRunnable, Component component, Object[] arguments) {
        switch (method.getName()) {
            case "getAccessibleActionDescription":
                return getAccessibleActionDescription(defaultSupplier,
                        (AccessibleAction) arguments[0],
                        (Integer) arguments[1],
                        component);
            case "doAccessibleAction":
                doAccessibleAction(defaultRunnable,
                        (AccessibleAction) arguments[0],
                        (Integer) arguments[1],
                        component);
                return null;
            case "getSize":
                return getSize(defaultSupplier,
                        (AccessibleComponent) arguments[0],
                        component);
            case "getAccessibleSelection":
                return getAccessibleSelection(defaultSupplier,
                        (AccessibleContext) arguments[0],
                        component);
            case "ax_getAccessibleSelection":
                return ax_getAccessibleSelection(defaultSupplier,
                        (AccessibleContext) arguments[0],
                        (Integer) arguments[1],
                        component);
            case "addAccessibleSelection":
                addAccessibleSelection(defaultRunnable,
                        (AccessibleContext) arguments[0],
                        (Integer) arguments[1],
                        component);
                return null;
            case "getAccessibleContext":
                return getAccessibleContext(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "isAccessibleChildSelected":
                return isAccessibleChildSelected(defaultSupplier,
                        (Accessible) arguments[0],
                        (Integer) arguments[1],
                        component);
            case "getAccessibleStateSet":
                return getAccessibleStateSet(defaultSupplier,
                        (AccessibleContext) arguments[0],
                        component);
            case "contains":
                return contains(defaultSupplier,
                        (AccessibleContext) arguments[0],
                        (AccessibleState) arguments[1],
                        component);
            case "getAccessibleRole":
                return getAccessibleRole(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getLocationOnScreen":
                return getLocationOnScreen(defaultSupplier,
                        (AccessibleComponent) arguments[0],
                        component);
            case "getCharCount":
                return getCharCount(defaultSupplier,
                        (AccessibleText) arguments[0],
                        component);
            case "getAccessibleParent":
                return getAccessibleParent(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleIndexInParent":
                return getAccessibleIndexInParent(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleComponent":
                return getAccessibleComponent(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleValue":
                return getAccessibleValue(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleName":
                return getAccessibleName(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleText":
                return getAccessibleText(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleDescription":
                return getAccessibleDescription(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "isFocusTraversable":
                return isFocusTraversable(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "accessibilityHitTest":
                return accessibilityHitTest(defaultSupplier,
                        (Container) arguments[0],
                        (Float) arguments[1],
                        (Float) arguments[2]);
            case "getAccessibleAction":
                return getAccessibleAction(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            // TODO: reevaluate getAccessibleActionCount (lambdas are a different case)
            case "isEnabled":
                return isEnabled(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "requestFocus":
                requestFocus(defaultRunnable,
                        (Accessible) arguments[0],
                        component);
                return null;
            case "requestSelection":
                requestSelection(defaultRunnable,
                        (Accessible) arguments[0],
                        component);
                return null;
            case "getMaximumAccessibleValue":
                return getMaximumAccessibleValue(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getMinimumAccessibleValue":
                return getMinimumAccessibleValue(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleRoleDisplayString":
                return getAccessibleRoleDisplayString(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getCurrentAccessibleValue":
                return getCurrentAccessibleValue(defaultSupplier,
                        (AccessibleValue) arguments[0],
                        component);
            case "getFocusOwner":
                return getFocusOwner(defaultSupplier,
                        component);
            case "getInitialAttributeStates":
                return getInitialAttributeStates(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "invokeGetChildrenAndRoles":
                return invokeGetChildrenAndRoles(defaultSupplier,
                        (Accessible) arguments[0],
                        (Component) arguments[1],
                        (Integer) arguments[2],
                        (Boolean) arguments[3],
                        arguments[4]);
            case "getChildrenAndRolesRecursive":
                return getChildrenAndRolesRecursive(defaultSupplier,
                        (Accessible) arguments[0],
                        (Component) arguments[1],
                        (Integer) arguments[2],
                        (Boolean) arguments[3],
                        (Integer) arguments[4]);
            case "getAccessibleCurrentAccessible":
                return getAccessibleCurrentAccessible(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAccessibleComboboxValue":
                return getAccessibleComboboxValue(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getCurrentAccessiblePopupMenu":
                return getCurrentAccessiblePopupMenu(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getTableInfo":
                return getTableInfo(defaultSupplier,
                        (Accessible) arguments[0],
                        (Component) arguments[1],
                        (Integer) arguments[2]);
            case "getTableSelectedInfo":
                return getTableSelectedInfo(defaultSupplier,
                        (Accessible) arguments[0],
                        (Component) arguments[1],
                        (Integer) arguments[2]);
            case "getChildren":
                return getChildren(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getAWTView":
                return getAWTView(defaultSupplier,
                        (Accessible) arguments[0]);
            case "isTreeRootVisible":
                return isTreeRootVisible(defaultSupplier,
                        (Accessible) arguments[0],
                        component);

            // from CAccessibleText:
            case "getAccessibleEditableText":
                return getAccessibleEditableText(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getSelectedText":
                return getSelectedText(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "setSelectedText":
                setSelectedText(defaultRunnable,
                        (Accessible) arguments[0],
                        component,
                        (String) arguments[1] );
                return null;
            case "setSelectedTextRange":
                setSelectedTextRange(defaultRunnable,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1],
                        (Integer) arguments[2] );
                return null;
            case "getTextRange":
                return getTextRange(defaultSupplier,
                        (AccessibleEditableText) arguments[0],
                        (Integer) arguments[1],
                        (Integer) arguments[2],
                        component);
            case "getCharacterIndexAtPosition":
                return getCharacterIndexAtPosition(defaultSupplier,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1],
                        (Integer) arguments[2] );
            case "getSelectedTextRange":
                return getSelectedTextRange(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getVisibleCharacterRange":
                return getVisibleCharacterRange(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getLineNumberForIndex":
                return getLineNumberForIndex(defaultSupplier,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1] );
            case "getLineNumberForInsertionPoint":
                return getLineNumberForInsertionPoint(defaultSupplier,
                        (Accessible) arguments[0],
                        component);
            case "getRangeForLine":
                return getRangeForLine(defaultSupplier,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1] );
            case "getRangeForIndex":
                return getRangeForIndex(defaultSupplier,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1] );
            case "getBoundsForRange":
                return getBoundsForRange(defaultSupplier,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1],
                        (Integer) arguments[2] );
            case "getStringForRange":
                return getStringForRange(defaultSupplier,
                        (Accessible) arguments[0],
                        component,
                        (Integer) arguments[1],
                        (Integer) arguments[2] );

            default:
                return RETURN_VALUE_UNSUPPORTED;
        }
    }

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
