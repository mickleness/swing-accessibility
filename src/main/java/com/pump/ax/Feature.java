package com.pump.ax;

public abstract class Feature {
    public abstract void install() throws Exception;
    public abstract void uninstall();
    public abstract boolean isRecommended();
    public abstract boolean isSupported();

    @Override
    public String toString() {
        // the default Features aren't configurable; just declaring the classname should offer a meaningful/unique name
        return getClass().getSimpleName();
    }
}
