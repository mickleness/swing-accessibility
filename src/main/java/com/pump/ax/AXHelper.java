package com.pump.ax;

import com.pump.ax.mac.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AXHelper {
    public static final Logger logger = Logger.getLogger("AXHelper");
    public static boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
    public static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");

    public static final float javaVersion = getJavaVersionAsFloat();

    private static float getJavaVersionAsFloat() {
        String version = System.getProperty("java.version");
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("^\\d+(\\.\\d+)?").matcher(version);

        if (matcher.find()) {
            return Float.parseFloat(matcher.group());
        }

        return -1;
    }

    private static boolean isInstalled = false;

    public static void install() {
        synchronized (AXHelper.class) {
            if (isInstalled)
                return;
            isInstalled = true;
        }

        logger.log(Level.INFO, "Installing for JDK {0} on {1}", new Object[] { javaVersion, System.getProperty("os.name") });

        if (isMac) {
            registerFeature(new FixVoiceOverHiddenComponents());
            registerFeature(new FixAnnouncePrevFocusedComponent());
            registerFeature(new FixAccessibleActionArgumentTypes());
            registerFeature(new FixLocalizedActionDescription());
            registerFeature(new FeatureAccessibleRole());
            registerFeature(new FixReadingHTML());
            registerFeature(new FixChangingAncestor());

            // TODO: also write fix for https://bugs.openjdk.org/browse/JDK-8378404
            // (use TransparentPopupFactory -- the Feature that doesn't use CAccessbilityController)
        } else if (isWindows) {
            // TODO: can we write similar architecture for FeatureAccessibleRole?
        }
    }

    static ArrayList<Feature> allFeatures = new ArrayList<>();

    public static void registerFeature(Feature feature) {
        Objects.requireNonNull(feature);

        synchronized (AXHelper.class) {
            if (!allFeatures.contains(feature)) {
                allFeatures.add(feature);
                if (!feature.isRecommended()) {
                    logger.log(Level.INFO, "{0} is not recommended in this environment.", feature);
                } else if (!feature.isSupported()) {
                    logger.log(Level.INFO, "{0}} is recommended in this environment, but it is not supported.", feature);
                } else {
                    try {
                        feature.install();
                        logger.log(Level.INFO, "{0} installed.", feature);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, feature + " failed ot install", e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
