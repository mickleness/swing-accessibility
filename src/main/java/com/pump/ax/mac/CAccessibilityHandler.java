package com.pump.ax.mac;

import com.pump.ax.AbstractBridgeHandler;

import javax.accessibility.*;
import java.awt.*;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * This class intercepts calls to static CAccessibility and CAccessibleText methods.
 * <p>
 * The methods in this class are only called on the event dispatch thread.
 * <p>
 * Some methods in CAccessibility may expect a CAccessible object as an argument,
 * or they may return a CAccessible object. The CAccessibilityController class will
 * convert arguments and return values as needed automatically to/from CAccessible;
 * subclasses of this class never need to think about CAccessible objects.
 */
public abstract class CAccessibilityHandler extends AbstractBridgeHandler {

    protected Supplier defaultSupplier;

    /**
     * Return a value for a given CAccessibility or CAccessibleText metthod, or return RETURN_VALUE_UNSUPPORTED.
     * <p>
     * Subclasses usually shouldn't need to override this method unless they need to correct the incoming arguments.
     */
    @Override
    public Object invoke(InvocationEvent invocationEvent, Method method, Supplier defaultSupplier, Object[] arguments) {
        this.defaultSupplier = defaultSupplier;

        Object src = invocationEvent.getSource();
        Component component = src instanceof Component ?
                (Component) src : null;
        
        try {
            switch (method.getName()) {
                case "getAccessibleActionDescription":
                    return getAccessibleActionDescription((AccessibleAction) arguments[0],
                            (Integer) arguments[1],
                            component);
                case "doAccessibleAction":
                    doAccessibleAction((AccessibleAction) arguments[0],
                            (Integer) arguments[1],
                            component);
                    return null;
                case "getSize":
                    return getSize((AccessibleComponent) arguments[0],
                            component);
                case "getAccessibleSelection":
                    return getAccessibleSelection((AccessibleContext) arguments[0],
                            component);
                case "ax_getAccessibleSelection":
                    return ax_getAccessibleSelection((AccessibleContext) arguments[0],
                            (Integer) arguments[1],
                            component);
                case "addAccessibleSelection":
                    addAccessibleSelection((AccessibleContext) arguments[0],
                            (Integer) arguments[1],
                            component);
                    return null;
                case "getAccessibleContext":
                    return getAccessibleContext((Accessible) arguments[0],
                            component);
                case "isAccessibleChildSelected":
                    return isAccessibleChildSelected((Accessible) arguments[0],
                            (Integer) arguments[1],
                            component);
                case "getAccessibleStateSet":
                    return getAccessibleStateSet((AccessibleContext) arguments[0],
                            component);
                case "contains":
                    return contains((AccessibleContext) arguments[0],
                            (AccessibleState) arguments[1],
                            component);
                case "getAccessibleRole":
                    return getAccessibleRole((Accessible) arguments[0],
                            component);
                case "getLocationOnScreen":
                    return getLocationOnScreen((AccessibleComponent) arguments[0],
                            component);
                case "getCharCount":
                    return getCharCount((AccessibleText) arguments[0],
                            component);
                case "getAccessibleParent":
                    return getAccessibleParent((Accessible) arguments[0],
                            component);
                case "getAccessibleIndexInParent":
                    return getAccessibleIndexInParent((Accessible) arguments[0],
                            component);
                case "getAccessibleComponent":
                    return getAccessibleComponent((Accessible) arguments[0],
                            component);
                case "getAccessibleValue":
                    return getAccessibleValue((Accessible) arguments[0],
                            component);
                case "getAccessibleName":
                    return getAccessibleName((Accessible) arguments[0],
                            component);
                case "getAccessibleText":
                    return getAccessibleText((Accessible) arguments[0],
                            component);
                case "getAccessibleDescription":
                    return getAccessibleDescription((Accessible) arguments[0],
                            component);
                case "isFocusTraversable":
                    return isFocusTraversable((Accessible) arguments[0],
                            component);
                case "accessibilityHitTest":
                    return accessibilityHitTest((Container) arguments[0],
                            (Float) arguments[1],
                            (Float) arguments[2]);
                case "getAccessibleAction":
                    return getAccessibleAction((Accessible) arguments[0],
                            component);
                // TODO: reevaluate getAccessibleActionCount (lambdas are a different case)
                case "isEnabled":
                    return isEnabled((Accessible) arguments[0],
                            component);
                case "requestFocus":
                    requestFocus((Accessible) arguments[0],
                            component);
                    return null;
                case "requestSelection":
                    requestSelection((Accessible) arguments[0],
                            component);
                    return null;
                case "getMaximumAccessibleValue":
                    return getMaximumAccessibleValue((Accessible) arguments[0],
                            component);
                case "getMinimumAccessibleValue":
                    return getMinimumAccessibleValue((Accessible) arguments[0],
                            component);
                case "getAccessibleRoleDisplayString":
                    return getAccessibleRoleDisplayString((Accessible) arguments[0],
                            component);
                case "getCurrentAccessibleValue":
                    return getCurrentAccessibleValue((AccessibleValue) arguments[0],
                            component);
                case "getFocusOwner":
                    return getFocusOwner(component);
                case "getInitialAttributeStates":
                    return getInitialAttributeStates((Accessible) arguments[0],
                            component);
                case "getChildrenAndRoles":
                    // this is observed up to Java 19
                    return invokeGetChildrenAndRoles((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1],
                            (Boolean) arguments[2],
                            null
                    );
                case "invokeGetChildrenAndRoles":
                    // this is observed in Java 19+
                    return invokeGetChildrenAndRoles((Accessible) arguments[0],
                            (Component) arguments[1],
                            (Integer) arguments[2],
                            (Boolean) arguments[3],
                            arguments[4]);
                case "getChildrenAndRolesRecursive":
                    return getChildrenAndRolesRecursive((Accessible) arguments[0],
                            (Component) arguments[1],
                            (Integer) arguments[2],
                            (Boolean) arguments[3],
                            (Integer) arguments[4]);
                case "getAccessibleCurrentAccessible":
                    return getAccessibleCurrentAccessible((Accessible) arguments[0],
                            component);
                case "getAccessibleComboboxValue":
                    return getAccessibleComboboxValue((Accessible) arguments[0],
                            component);
                case "getCurrentAccessiblePopupMenu":
                    return getCurrentAccessiblePopupMenu((Accessible) arguments[0],
                            component);
                case "getTableInfo":
                    return getTableInfo((Accessible) arguments[0],
                            (Component) arguments[1],
                            (Integer) arguments[2]);
                case "getTableSelectedInfo":
                    return getTableSelectedInfo((Accessible) arguments[0],
                            (Component) arguments[1],
                            (Integer) arguments[2]);
                case "getChildren":
                    return getChildren((Accessible) arguments[0],
                            component);
                case "getAWTView":
                    return getAWTView((Accessible) arguments[0]);
                case "isTreeRootVisible":
                    return isTreeRootVisible((Accessible) arguments[0],
                            component);

                // from CAccessibleText:
                case "getAccessibleEditableText":
                    return getAccessibleEditableText((Accessible) arguments[0],
                            component);
                case "getSelectedText":
                    return getSelectedText((Accessible) arguments[0],
                            component);
                case "setSelectedText":
                    setSelectedText((Accessible) arguments[0],
                            component,
                            (String) arguments[1] );
                    return null;
                case "setSelectedTextRange":
                    setSelectedTextRange((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1],
                            (Integer) arguments[2] );
                    return null;
                case "getTextRange":
                    return getTextRange((AccessibleEditableText) arguments[0],
                            (Integer) arguments[1],
                            (Integer) arguments[2],
                            component);
                case "getCharacterIndexAtPosition":
                    return getCharacterIndexAtPosition((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1],
                            (Integer) arguments[2] );
                case "getSelectedTextRange":
                    return getSelectedTextRange((Accessible) arguments[0],
                            component);
                case "getVisibleCharacterRange":
                    return getVisibleCharacterRange((Accessible) arguments[0],
                            component);
                case "getLineNumberForIndex":
                    return getLineNumberForIndex((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1] );
                case "getLineNumberForInsertionPoint":
                    return getLineNumberForInsertionPoint((Accessible) arguments[0],
                            component);
                case "getRangeForLine":
                    return getRangeForLine((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1] );
                case "getRangeForIndex":
                    return getRangeForIndex((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1] );
                case "getBoundsForRange":
                    // CAccessibleText.getBoundsForRange is the only method in that
                    // class that creates a final field before calling invokeAndWait.
                    // That final field is visible here as `arguments[1]`.
                    double[] returnValue = getBoundsForRange((Accessible) arguments[0],
                            component,
                            (Integer) arguments[2],
                            (Integer) arguments[3]);
                    double[] destArray = (double[]) arguments[1];
                    System.arraycopy(returnValue, 0, destArray, 0, 4);
                    return destArray;
                case "getStringForRange":
                    return getStringForRange((Accessible) arguments[0],
                            component,
                            (Integer) arguments[1],
                            (Integer) arguments[2] );

                default:
                    return RETURN_VALUE_UNSUPPORTED;
            }
        } finally {
            this.defaultSupplier = null;
        }
    }

    protected String getAccessibleActionDescription(AccessibleAction aa, int index, Component c) {
        return (String) defaultSupplier.get();
    }

    protected void doAccessibleAction(AccessibleAction aa, int index, Component c) {
        defaultSupplier.get();
    }

    protected Dimension getSize(AccessibleComponent ac, Component c) {
        return (Dimension) defaultSupplier.get();
    }

    protected AccessibleSelection getAccessibleSelection(AccessibleContext ac, Component c) {
        return (AccessibleSelection) defaultSupplier.get();
    }

    protected Accessible ax_getAccessibleSelection(AccessibleContext ac, int index, Component c) {
        return (Accessible) defaultSupplier.get();
    }

    protected void addAccessibleSelection(AccessibleContext ac, int index, Component c) {
        defaultSupplier.get();
    }

    protected AccessibleContext getAccessibleContext(Accessible a, Component c) {
        return (AccessibleContext) defaultSupplier.get();
    }

    protected boolean isAccessibleChildSelected(Accessible a, int index, Component c) {
        return (Boolean) defaultSupplier.get();
    }

    protected AccessibleStateSet getAccessibleStateSet(AccessibleContext ac, Component c) {
        return (AccessibleStateSet) defaultSupplier.get();
    }

    protected boolean contains(AccessibleContext ac, AccessibleState as, Component c) {
        return (Boolean) defaultSupplier.get();
    }

    protected String getAccessibleRole(Accessible a, Component c) {
        return (String) defaultSupplier.get();
    }

    protected Point getLocationOnScreen(AccessibleComponent ac, Component c) {
        return (Point) defaultSupplier.get();
    }

    protected int getCharCount(AccessibleText at, Component c) {
        return (Integer) defaultSupplier.get();
    }

    protected Accessible getAccessibleParent(Accessible a, Component c) {
        return (Accessible) defaultSupplier.get();
    }

    protected int getAccessibleIndexInParent(Accessible a, Component c) {
        return (Integer) defaultSupplier.get();
    }

    protected AccessibleComponent getAccessibleComponent(Accessible a, Component c) {
        return (AccessibleComponent) defaultSupplier.get();
    }

    protected AccessibleValue getAccessibleValue(Accessible a, Component c) {
        return (AccessibleValue) defaultSupplier.get();
    }

    protected String getAccessibleName(Accessible a, Component c) {
        return (String) defaultSupplier.get();
    }

    protected AccessibleText getAccessibleText(Accessible a, Component c) {
        return (AccessibleText) defaultSupplier.get();
    }

    protected String getAccessibleDescription(Accessible a, Component c) {
        return (String) defaultSupplier.get();
    }

    protected boolean isFocusTraversable(Accessible a, Component c) {
        return (Boolean) defaultSupplier.get();
    }

    protected Accessible accessibilityHitTest(Container parent, float hitPointX, float hitPointY) {
        return (Accessible) defaultSupplier.get();
    }

    protected AccessibleAction getAccessibleAction(Accessible a, Component c) {
        return (AccessibleAction) defaultSupplier.get();
    }

    // TODO: reevaluate (lambdas are a different case)
//    default int getAccessibleActionCount(AccessibleAction aa, Component c) {
//        if (aa == null) return 0;
//
//        return invokeAndWait(aa::getAccessibleActionCount, c);
//    }

    protected boolean isEnabled(Accessible a, Component c) {
        return (Boolean) defaultSupplier.get();
    }

    protected void requestFocus(Accessible a, Component c) {
        defaultSupplier.get();
    }

    protected void requestSelection(Accessible a, Component c) {
        defaultSupplier.get();
    }

    protected Number getMaximumAccessibleValue(Accessible a, Component c) {
        return (Number) defaultSupplier.get();
    }

    protected Number getMinimumAccessibleValue(Accessible a, Component c) {
        return (Number) defaultSupplier.get();
    }

    protected String getAccessibleRoleDisplayString(Accessible a, Component c) {
        return (String) defaultSupplier.get();
    }

    protected Number getCurrentAccessibleValue(AccessibleValue av, Component c) {
        return (Number) defaultSupplier.get();
    }

    protected Accessible getFocusOwner(Component c) {
        return (Accessible) defaultSupplier.get();
    }

    protected boolean[] getInitialAttributeStates(Accessible a, Component c) {
        return (boolean[]) defaultSupplier.get();
    }

    // TODO: reevaluate
//    // Duplicated from JavaComponentAccessibility
//    // Note that values >=0 are indexes into the child array
//    @Native static final int JAVA_AX_ALL_CHILDREN = -1;
//    @Native static final int JAVA_AX_SELECTED_CHILDREN = -2;
//    @Native static final int JAVA_AX_VISIBLE_CHILDREN = -3;

    protected Object[] invokeGetChildrenAndRoles(Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
        return (Object[]) defaultSupplier.get();
    }

    // This method is called from the native
    // Each child takes up three entries in the array: one for itself, one for its role, and one for the recursion level
    protected Object[] getChildrenAndRolesRecursive(Accessible a, Component c, int whichChildren, boolean allowIgnored, int level) {
        return (Object[]) defaultSupplier.get();
    }

    protected Accessible getAccessibleCurrentAccessible(Accessible a, Component c) {
        return (Accessible) defaultSupplier.get();
    }

    protected Accessible getAccessibleComboboxValue(Accessible a, Component c) {
        return (Accessible) defaultSupplier.get();
    }

    protected Accessible getCurrentAccessiblePopupMenu(Accessible a, Component c) {
        return (Accessible) defaultSupplier.get();
    }

    // TODO: reevaluate
//    @Native private static final int JAVA_AX_ROWS = 1;
//    @Native private static final int JAVA_AX_COLS = 2;

    protected int getTableInfo(Accessible a, Component c,
                                   int info) {
        return (Integer) defaultSupplier.get();
    }

    protected int[] getTableSelectedInfo(Accessible a, Component c,
                                              int info) {
        return (int[]) defaultSupplier.get();
    }

    protected Object[] getChildren(Accessible a, Component c) {
        return (Object[]) defaultSupplier.get();
    }

    protected long getAWTView(Accessible a) {
        return (Long) defaultSupplier.get();
    }

    protected boolean isTreeRootVisible(Accessible a, Component c) {
        return (Boolean) defaultSupplier.get();
    }

    // from CAccessibleText:

    protected AccessibleEditableText getAccessibleEditableText(Accessible a, Component c) {
        return (AccessibleEditableText) defaultSupplier.get();
    }

    protected String getSelectedText(Accessible a, Component c) {
        return (String) defaultSupplier.get();
    }

    protected void setSelectedText(Accessible a, Component c, String newText) {
        defaultSupplier.get();
    }

    protected void setSelectedTextRange(Accessible a, Component c, int startIndex, int endIndex) {
        defaultSupplier.get();
    }

    protected String getTextRange(AccessibleEditableText aet, int start, int stop, Component c) {
        return (String) defaultSupplier.get();
    }

    protected int getCharacterIndexAtPosition(Accessible a, Component c, int x, int y) {
        return (int) defaultSupplier.get();
    }

    protected int[] getSelectedTextRange(Accessible a, Component c) {
        return (int[]) defaultSupplier.get();
    }


    protected int[] getVisibleCharacterRange(Accessible a, Component c) {
        return (int[]) defaultSupplier.get();
    }

    protected int getLineNumberForIndex(Accessible a, Component c, int index) {
        return (Integer) defaultSupplier.get();
    }

    protected int getLineNumberForInsertionPoint(Accessible a, Component c) {
        return (Integer) defaultSupplier.get();
    }

    protected int[] getRangeForLine(Accessible a, Component c, int line) {
        return (int[]) defaultSupplier.get();
    }

    protected int[] getRangeForIndex(Accessible a, Component c, int index) {
        return (int[]) defaultSupplier.get();
    }

    protected double[] getBoundsForRange(Accessible a, Component c, int location, int length) {
        return (double[]) defaultSupplier.get();
    }

    protected String getStringForRange(Accessible a, Component c, int location, int length) {
        return (String) defaultSupplier.get();
    }
}
