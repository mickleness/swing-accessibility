/*
 * Copyright (c) 1999, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.sax.mac;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <code>PopupFactory</code>, as the name implies, is used to obtain
 * instances of <code>Popup</code>s. <code>Popup</code>s are used to
 * display a <code>Component</code> above all other <code>Component</code>s
 * in a particular containment hierarchy. The general contract is that
 * once you have obtained a <code>Popup</code> from a
 * <code>PopupFactory</code>, you must invoke <code>hide</code> on the
 * <code>Popup</code>. The typical usage is:
 * <pre>
 *   PopupFactory factory = PopupFactory.getSharedInstance();
 *   Popup popup = factory.getPopup(owner, contents, x, y);
 *   popup.show();
 *   ...
 *   popup.hide();
 * </pre>
 *
 * @see Popup
 *
 * @since 1.4
 */
public class TransparentPopupFactory extends PopupFactory {
    private static PopupFactory origFactory;
    private static final TransparentPopupFactory transFactory = new TransparentPopupFactory();

    public static void setActive(boolean isActive) {
        if (isActive) {
            if (PopupFactory.getSharedInstance() == transFactory)
                return;
            origFactory = PopupFactory.getSharedInstance();
            PopupFactory.setSharedInstance(transFactory);
        } else {
            if (origFactory != null)
                PopupFactory.setSharedInstance(origFactory);
            origFactory = null;
        }
    }

    public TransparentPopupFactory() {
        TransparentPopup.initialize();
    }

    @Override
    protected Popup getPopup(Component owner, Component contents, int x, int y, boolean isHeavyWeightPopup) throws IllegalArgumentException {
        return new TransparentPopup(owner, contents, x, y, isHeavyWeightPopup);
    }
}
