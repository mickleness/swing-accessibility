package com.pump.ax.openjdk;

import com.pump.ax.AXHelper;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is adapted from the jtreg test for OpenJDK ticket #8377428.
 *
 * Instructions:
 * 1. Open app
 * 2. Turn on VoiceOver
 * 3. Press CTRL + ALT + LEFT repeatedly (slowly).
 *
 * This test passes if the VoiceOver cursor always frames a part of the UI.
 * This test fails if VoiceOver announces hidden components, and it shows
 * its VoiceOver cursor anchored at (0,0) while trying to describe them.
 */
public class TestVoiceOverHiddenComponentNavigation {
    public static void main(String[] args) {
        AXHelper.install();

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.add(createRow("Hidden Button", false));
        rows.add(createRow("Visible Button", true));

        JFrame frame = new JFrame();
        frame.getContentPane().add(rows);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Create a row to add to this demo frame.
     *
     * @param buttonText the button name/text
     * @param isVisible whether JPanel.isVisible() should be true
     * @return a row for the demo frame
     */
    private static JPanel createRow(String buttonText,
                                    boolean isVisible) {
        JPanel returnValue = new JPanel();
        returnValue.setVisible(isVisible);
        JButton button = new JButton(buttonText);
        returnValue.add(button);
        return returnValue;
    }
}