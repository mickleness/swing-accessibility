package org.swing_ax.mac;

import org.swing_ax.AccessibleRoleUtils;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class DefaultCAccessibilityHandler extends CAccessibilityHandler {

    public static final float javaVersion = getJavaVersionAsFloat();

    /**
     * This client property on a JComponent should resolve to an AccessibleRole.
     */
    public static final String PROPERTY_ACCESSIBLE_ROLE = "accessible role";

    private static float getJavaVersionAsFloat() {
        String version = System.getProperty("java.version");
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("^\\d+(\\.\\d+)?").matcher(version);

        if (matcher.find()) {
            return Float.parseFloat(matcher.group());
        }

        return -1;
    }

    enum Feature {
        /**
         * An OpenJDK bug mistakenly assumed the AccessibleContext always implemented the AccessibleAction
         * interface. If the AccessibleAction was a separate class, then this resulted in a IncompatibleClassChangeError.
         * <p>
         * This bug would not be observed with default JComponents, which usually follow the pattern of making
         * their AccessibleContext implement their related accessible interfaces.
         * </p>
         *
         * {@see <a href="https://bugs.openjdk.org/browse/JDK-8380849">JDK-8380849</a>
         */
        BUG_FIX_DO_ACCESSIBLE_ACTION_ARGUMENT_CORRECTION() {
            @Override
            public boolean isRelevant() {
                return javaVersion >= 17 && javaVersion < 27;
            }
        },

        /**
         * When the current focus owner lost focus: VoiceOver would mistakenly issue a
         * valueChanged notification. As a result: the keyboard focus moved from
         * component A to B, but VoiceOver may try to re-announce a description of component A.
         *
         * {@see <a href="https://bugs.openjdk.org/browse/JDK-8377936">JDK-8377936</a>
         */
        BUG_FIX_DONT_ANNOUNCE_VALUE_CHANGE_FOR_LOST_FOCUS() {
            @Override
            public boolean isRelevant() {
                return javaVersion >= 14 && javaVersion < 27;
            }
        },

        /**
         * This may override a JComponent's default AccessibleRole if
         * `JComponent.getClientProperty(PROPERTY_ACCESSIBLE_ROLE)`
         * is defined.
         */
        FEATURE_SUPPORT_ROLE_AS_CLIENT_PROPERTY() {
            @Override
            public boolean isRelevant() {
                return true;
            }
        },

        /**
         * Replace AccessibleRole.HYPERLINK with Mac's "AXLink" role.
         *
         * {@see <a href="https://bugs.openjdk.org/browse/JDK-8377745">JDK-8377745</a>
         */
        BUG_FIX_USE_MAC_LINK_ROLE() {
            @Override
            public boolean isRelevant() {
                return javaVersion < 27;
            }
        },

        /**
         * Un-localize AccessibleAction.getAccessibleActionDescription(i) for VoiceOver.
         *
         * For example: if your current Locale is German then getAccessibleActionDescription(i)
         * may return "clicken". But the JDK's obj c code is compiled to only look for
         * "click", so it fails to understand the action description. (This is usually
         * harmless, though, because VoiceOver falls back to simulating mouse movements
         * to "click" the component.)
         *
         * {@see <a href="https://bugs.openjdk.org/browse/JDK-8377938">JDK-8377938</a>
         */
        BUG_FIX_USE_CLICK_ACTION_DESCRIPTION() {

            @Override
            public boolean isRelevant() {
                // TODO: update if 8377938 is resolved
                return true;
            }
        },

        /**
         * This prevents VoiceOver from cataloging hidden components.
         *
         * {@see <a href="https://bugs.openjdk.org/browse/JDK-8377428">JDK-8377428</a>
         */
        BUG_FIX_SKIP_HIDDEN_COMPONENTS() {

            @Override
            public boolean isRelevant() {
                // I didn't test JDKs 9-12.
                // I briefly tested 8, and observed a NPE i _addChildren.
                // That is probably something these classes could address, but
                // support JDK 8 is outside my currently planned scope
                return 13 <= javaVersion && javaVersion <= 26;
            }
        };

        /**
         * Return true if this Feature is probably useful in the current JVM.
         * Some Features return false based on the JDK version.
         */
        public abstract boolean isRelevant();
    }

    Set<Feature> activeFeatures = new HashSet<>();

    public DefaultCAccessibilityHandler() {
        for (Feature f : Feature.values()) {
            if (f.isRelevant())
                addFeature(f);
        }
        System.out.println("DefaultCAccessibilityHandler active features for JDK version " + javaVersion + ": " + activeFeatures);
    }

    public void addFeature(Feature feature) {
        activeFeatures.add(feature);
    }

    public void remove(Feature feature) {
        activeFeatures.remove(feature);
    }

    @Override
    public Object invoke(Method method, Supplier defaultSupplier, Runnable defaultRunnable, Component component, Object[] arguments) {
        if (activeFeatures.contains(Feature.BUG_FIX_DO_ACCESSIBLE_ACTION_ARGUMENT_CORRECTION) &&
                method.getName().equals("doAccessibleAction")) {
            if (arguments[0] instanceof AccessibleContext && !(arguments[0] instanceof AccessibleAction)) {
                AccessibleAction aa = ((AccessibleContext) arguments[0]).getAccessibleAction();
                aa.doAccessibleAction( (Integer) arguments[1]);
                return null;
            }
        }

        return super.invoke(method, defaultSupplier, defaultRunnable, component, arguments);
    }

    @Override
    public void requestFocus(Runnable defaultImplementation, Accessible a, Component c) {
        if (activeFeatures.contains(Feature.BUG_FIX_DONT_ANNOUNCE_VALUE_CHANGE_FOR_LOST_FOCUS)) {
            // normally (without this intervention) CAccessible's AXChangeNotifier will receive
            // a AccessibleStateSet notification when the current focus owner loses the keyboard focus.
            // As a result, it will fire a CAccessible.valueChanged message. VoiceOver ends up reading
            // the prev focus owner's description, as if the user meant to interact with it.

            // so to avoid this: we'll temporarily remove the FocusListener that updates AXChangeNotifier.
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            AccessibleRole focusOwnerRole = focusOwner == null ? null : focusOwner.getAccessibleContext().getAccessibleRole();
            boolean isToggle = focusOwnerRole == AccessibleRole.TOGGLE_BUTTON ||
                    focusOwnerRole == AccessibleRole.RADIO_BUTTON ||
                    focusOwnerRole == AccessibleRole.CHECK_BOX;
            if (isToggle) {
                FocusListener[] listeners = focusOwner.getFocusListeners();
                for (FocusListener listener : listeners) {
                    if (listener.getClass().getName().equals("java.awt.Component$AccessibleAWTComponent$AccessibleAWTFocusHandler")) {
                        focusOwner.removeFocusListener(listener);
                        focusOwner.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                focusOwner.addFocusListener(listener);
                                focusOwner.removeFocusListener(this);
                            }

                            @Override
                            public void focusLost(FocusEvent e) {
                                focusGained(e);
                            }
                        });
                        break;
                    }
                }
            }
        }
        super.requestFocus(defaultImplementation, a, c);
    }

    @Override
    public String getAccessibleRole(Supplier<String> defaultImplementation, Accessible a, Component c) {
        if (a instanceof JComponent) {
            JComponent jc = (JComponent) a;
            AccessibleRole returnValue = getReplacementAccessibleRole(jc);
            if (returnValue instanceof MacAXRole) {
                return ((MacAXRole)returnValue).getKey();
            } else if (returnValue != null) {
                String key = AccessibleRoleUtils.getKey(returnValue);
                if (key != null)
                    return key;
                // TODO: improve logging
                System.err.println("getAccessibleRole identified " + returnValue + " but could not identify its key");
            }
        }
        return super.getAccessibleRole(defaultImplementation, a, c);
    }

    /**
     * Return the preferred AccessibleRole of a Component, or null
     * if we should use the default `getAccessibleContext().getAccessibleRole()`
     */
    private AccessibleRole getReplacementAccessibleRole(Component component) {
        AccessibleRole returnValue = component.getAccessibleContext().getAccessibleRole();
        if (activeFeatures.contains(Feature.FEATURE_SUPPORT_ROLE_AS_CLIENT_PROPERTY) &&
                component instanceof JComponent) {
            JComponent jc = (JComponent) component;
            AccessibleRole clientRole = (AccessibleRole) jc.getClientProperty(PROPERTY_ACCESSIBLE_ROLE);
            if (clientRole != null)
                returnValue = clientRole;
        }

        if (activeFeatures.contains(Feature.BUG_FIX_USE_MAC_LINK_ROLE) &&
                returnValue == AccessibleRole.HYPERLINK) {
            returnValue = MacAXRole.AXLink;
        }

        return returnValue;
    }

    @Override
    public Object[] invokeGetChildrenAndRoles(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, Object ops) {
        Object[] returnValue = super.invokeGetChildrenAndRoles(defaultImplementation, a, c, whichChildren, allowIgnored, ops);
        replaceRolesWithCustomClientRole(returnValue, 2);
        if (!allowIgnored)
            returnValue = removeHiddenComponents(returnValue, 2);
        return returnValue;
    }

    private void replaceRolesWithCustomClientRole(Object[] componentsAndRoles, int arrayIncr) {
        for (int i = 0; i < componentsAndRoles.length; i += arrayIncr) {
            if (componentsAndRoles[i] instanceof Component) {
                AccessibleRole customClientRole = getReplacementAccessibleRole((Component) componentsAndRoles[i]);
                if (customClientRole != null)
                    componentsAndRoles[i + 1] = customClientRole;
            }
        }
    }

    /**
     * Remove all Component entries that are not currently showing.
     */
    private Object[] removeHiddenComponents(Object[] components, int arrayIncr) {
        if (!activeFeatures.contains(Feature.BUG_FIX_SKIP_HIDDEN_COMPONENTS))
            return components;

        Collection<Integer> removedIndices = new HashSet<>();
        for (int i = 0; i < components.length; i += arrayIncr) {
            if (components[i] instanceof Component) {
                Component component = (Component) components[i];
                if (component != null && !component.isShowing()) {
                    removedIndices.add(i);
                }
            }
        }

        if (removedIndices.isEmpty())
            return components;

        List<Object> returnValue = new ArrayList<>(arrayIncr * (components.length - removedIndices.size()));
        for (int i = 0; i < components.length; i += arrayIncr) {
            if (!removedIndices.contains(i)) {
                for (int a = 0; a < arrayIncr; a++) {
                    returnValue.add(components[i + a]);
                }
            }
        }

        return returnValue.toArray(new Object[0]);
    }

    @Override
    public Object[] getChildrenAndRolesRecursive(Supplier<Object[]> defaultImplementation, Accessible a, Component c, int whichChildren, boolean allowIgnored, int level) {
        Object[] returnValue = super.getChildrenAndRolesRecursive(defaultImplementation, a, c, whichChildren, allowIgnored, level);
        replaceRolesWithCustomClientRole(returnValue, 3);
        if (!allowIgnored)
            returnValue = removeHiddenComponents(returnValue, 3);
        return returnValue;
    }

    @Override
    public String getAccessibleActionDescription(Supplier<String> defaultImplementation, AccessibleAction aa, int index, Component c) {
        String returnValue = super.getAccessibleActionDescription(defaultImplementation, aa, index, c);
        if (activeFeatures.contains(Feature.BUG_FIX_USE_CLICK_ACTION_DESCRIPTION) &&
                Objects.equals(UIManager.getString("AbstractButton.clickText"), returnValue)) {
            return AccessibleAction.CLICK;
        }
        return returnValue;
    }
}
