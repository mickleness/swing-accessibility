package com.pump.ax;

public abstract class Feature {
    public abstract void install() throws Exception;
    public abstract void uninstall();
    public abstract boolean isRecommended();
    public abstract boolean isSupported();
}
