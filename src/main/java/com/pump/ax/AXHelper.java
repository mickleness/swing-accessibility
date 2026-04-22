package com.pump.ax;

import com.pump.ax.mac.*;

import java.util.ArrayList;
import java.util.Objects;

public class AXHelper {

    public static boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

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
    private static boolean includeAllRecommendedFeatures;
    static ArrayList<Feature> allFeatures = new ArrayList<>();
    public static void install(boolean includeAllRecommendedFeatures) {
        synchronized (AXHelper.class) {
            if (isInstalled) {
                if (AXHelper.includeAllRecommendedFeatures == includeAllRecommendedFeatures)
                    return;
                throw new IllegalStateException("install(" + AXHelper.includeAllRecommendedFeatures + ") was already called");
            }
            isInstalled = true;
            AXHelper.includeAllRecommendedFeatures = includeAllRecommendedFeatures;
        }

        if (includeAllRecommendedFeatures) {
            for (Feature feature : allFeatures) {
                try {
                    feature.install();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

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
        }

        // TODO: log Features that installed by default
    }

    public static void registerFeature(Feature feature) {
        Objects.requireNonNull(feature);

        synchronized (AXHelper.class) {
            if (!allFeatures.contains(feature))
                allFeatures.add(feature);
        }

        if (isInstalled && includeAllRecommendedFeatures) {
            try {
                feature.install();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
