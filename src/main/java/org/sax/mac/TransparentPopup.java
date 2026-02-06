package org.sax.mac;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class TransparentPopup extends Popup {
    static LinkedList<MutableWindow> unusedWindows = new LinkedList<>();

    public static void initialize() {
        for (int a = 0; a < 5; a++) {
            MutableWindow w = new MutableWindow();
            w.setOpacity(0);
            w.setVisible(true);
            unusedWindows.add(w);
        }
    }

    static class MutableWindow extends JWindow {
        MutableWindow() {
            setType(Type.POPUP);
            setAlwaysOnTop(true);
        }
        public void setRootPane(JRootPane rootPane) {
            super.setRootPane(rootPane);
        }
    }

    MutableWindow window;

    public TransparentPopup(Component owner, Component contents, int x, int y, boolean isHeavyWeightPopup) {
        window = getWindow();
        window.setRootPane(new JRootPane());
        window.getContentPane().add(contents);
        window.pack();
        Point p = owner.getLocationOnScreen();
        window.setLocation(x, y);
    }

    private MutableWindow getWindow() {
        if (unusedWindows.isEmpty())
            return new MutableWindow();
        return unusedWindows.pop();
    }

    @Override
    public void show() {
        window.setOpacity(1);
        window.setVisible(true);
    }

    @Override
    public void hide() {
        window.setOpacity(0);
        // help gc any old data
        window.setRootPane(new JRootPane());
        unusedWindows.add(window);
    }
}
