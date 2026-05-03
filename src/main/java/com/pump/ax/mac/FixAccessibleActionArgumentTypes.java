package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;

import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.event.InvocationEvent;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * This fixes a VoiceOver bug that assumed if an AccessibleAction was
 * in use that it was the same object as the AccessibleContext.
 *
 * This is a work-around for <a href="https://bugs.openjdk.org/browse/JDK-8380849">JDK-8380849</a>
 */
public class FixAccessibleActionArgumentTypes extends Feature {

    final CAccessibilityHandler handler = new CAccessibilityHandler() {
        @Override
        public Object invoke(InvocationEvent invocationEvent, Method method, Supplier defaultSupplier, Runnable defaultRunnable, Object[] arguments) {
            if (method.getName().equals("doAccessibleAction")) {
                if (arguments[0] instanceof AccessibleContext && !(arguments[0] instanceof AccessibleAction)) {
                    AccessibleAction aa = ((AccessibleContext) arguments[0]).getAccessibleAction();
                    aa.doAccessibleAction( (Integer) arguments[1]);
                    return null;
                }
            }

            return super.invoke(invocationEvent, method, defaultSupplier, defaultRunnable, arguments);
        }
    };

    @Override
    public void install() throws UnsupportedOperationException {
        if (!isSupported())
            throw new UnsupportedOperationException(CAccessibilityController.UNSUPPORTED_EXCEPTION_MESSAGE);
        CAccessibilityController.get().addHandler(handler);
    }

    @Override
    public void uninstall() {
        CAccessibilityController.get().removeHandler(handler);
    }

    @Override
    public boolean isRecommended() {
        return AXHelper.isMac && AXHelper.javaVersion >= 17 && AXHelper.javaVersion < 27;
    }

    @Override
    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
