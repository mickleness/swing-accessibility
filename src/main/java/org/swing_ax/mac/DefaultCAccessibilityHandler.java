package org.swing_ax.mac;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class DefaultCAccessibilityHandler extends CAccessibilityHandler {

    public static final float javaVersion = getJavaVersionAsFloat();

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
            public boolean isReproducible() {
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
            public boolean isReproducible() {
                return javaVersion >= 14 && javaVersion < 27;
            }
        };

        public abstract boolean isReproducible();
    }

    Set<Feature> activeFeatures = new HashSet<>();

    public DefaultCAccessibilityHandler() {
        for (Feature f : Feature.values()) {
            if (f.isReproducible())
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
}
