package org.swing_ax.mac;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import java.awt.*;
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
}
