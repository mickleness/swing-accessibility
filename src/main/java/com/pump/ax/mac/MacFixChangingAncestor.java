package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ContainerEvent;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Method;
import java.util.function.Supplier;


/**
 * This fixes a VoiceOver bug that failed to describe Components if they were removed
 * from one Window and added to another.
 *
 * This is a work-around for <a href="https://bugs.openjdk.org/browse/JDK-8381236">JDK-8381236</a>
 */
public class MacFixChangingAncestor extends Feature {
    /**
     * This becomes true if we ever intercepted a CAccessibility or CAccessibleText method.
     */
    private static boolean was_AX_ever_used;

    /**
     * This Feature does not require a CAccessibilityHandler to intercept method
     * invocations. However: we use one here simply to track if it looks like
     * CAccessibility or CAccessibleText methods are being used. (This is optional,
     * but it lets us avoid unnecessary work later.)
     */
    private final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        public Object invoke(InvocationEvent invocationEvent, Method method, Supplier defaultSupplier, Object[] arguments) {
            Object returnValue = super.invoke(invocationEvent, method, defaultSupplier, arguments);
            if (returnValue != CAccessibilityHandler.RETURN_VALUE_UNSUPPORTED)
                was_AX_ever_used = true;
            return returnValue;
        }
    };

    private final AWTEventListener componentRemovedListener = new AWTEventListener() {
        @Override
        public void eventDispatched(AWTEvent event) {
            if (event.getID() == ContainerEvent.COMPONENT_REMOVED && was_AX_ever_used) {
                ContainerEvent containerEvent = (ContainerEvent) event;
                try {
                    CAccessibilityController.disposeCAccessible(containerEvent.getChild());
                } catch(Exception e) {
                    e.printStackTrace();
                    uninstall();
                }
            }
        }
    };

    @Override
    public void install() throws Exception {
        CAccessibilityController.get().addHandler(handler);
        Toolkit.getDefaultToolkit().addAWTEventListener(componentRemovedListener, ContainerEvent.CONTAINER_EVENT_MASK);
    }

    @Override
    public void uninstall() {
        CAccessibilityController.get().removeHandler(handler);
        Toolkit.getDefaultToolkit().removeAWTEventListener(componentRemovedListener);
    }

    @Override
    public boolean isRecommended() {
        // confirmed the bug reproduces & this Feature resolves it in:
        // 1.8 11, 13, 14, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27

        // TODO: maybe revise if https://github.com/openjdk/jdk/pull/30578/changes is not accepted
        return AXHelper.isMac && AXHelper.javaVersion >= 1.8f && AXHelper.javaVersion < 27f;
    }

    @Override
    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
