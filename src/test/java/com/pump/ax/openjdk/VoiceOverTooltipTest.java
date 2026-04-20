package com.pump.ax.openjdk;

import com.pump.ax.AXHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Test instructions:
 * 1. Open app
 * 2. Turn on VoiceOver
 * 3. Move mouse over each button. Wait for tooltip to appear.
 *
 * Observe every time the tooltip appears, VoiceOver aborts what it was saying
 * and says, "in system dialog, content is empty, java has new system dialog"
 *
 * Expected behavior: VoiceOver should not say anything when tooltips appear. Or if it must speak: it should say something descriptive/helpful.
 */
public class VoiceOverTooltipTest {
    public static void main(String[] args) {
        AXHelper.install(true);

        JFrame f = new JFrame();
        JButton b1 = new JButton("But");
        b1.setToolTipText("But it wasn't my fault, I was given those beans");

        JButton b2 = new JButton("You");
        b2.setToolTipText("You persuaded me to trade away my cow for beans.");

        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(b1, BorderLayout.NORTH);
        f.getContentPane().add(b2, BorderLayout.SOUTH);
        f.pack();
        f.setVisible(true);
    }
}