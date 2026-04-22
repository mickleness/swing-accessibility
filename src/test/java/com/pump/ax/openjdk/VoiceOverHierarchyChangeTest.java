package com.pump.ax.openjdk;

import com.pump.ax.AXHelper;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * This is adapted from OpenJDK ticket #8381236.
 *
 * Instructions:
 * 1. Open app
 * 2. Turn on VoiceOver
 * 3. Move the mouse over "Does Nothing" button
 * 4. Click the "Move to Other Window Button"
 * 5. Move the mouse over "Does Nothing" button
 *
 * Expected result: VoiceOver can read "Does Nothing" button both times.
 */
public class VoiceOverHierarchyChangeTest {
    public static void main(String[] args) {
        AXHelper.install(true);

        JFrame f1 = new JFrame();
        f1.getContentPane().setPreferredSize(new Dimension(300, 100));
        JFrame f2 = new JFrame();
        f2.getContentPane().setPreferredSize(new Dimension(300, 100));

        JButton hopButton = new JButton("Move To Other Window");
        JButton noopButton = new JButton("Does Nothing");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(hopButton, BorderLayout.NORTH);
        panel.add(noopButton, BorderLayout.SOUTH);

        hopButton.addActionListener(e -> {
            if (SwingUtilities.isDescendingFrom(hopButton, f1)) {
                f2.getContentPane().add(panel);
            } else {
                f1.getContentPane().add(panel);
            }
            f1.repaint();
            f2.repaint();
        });

        f1.getContentPane().add(panel);
        f1.pack();
        f2.pack();

        f1.setVisible(true);

        f1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                Rectangle r = f1.getBounds();
                f2.setLocation(r.x, r.y + r.height);
                f2.setVisible(true);
            }
        });
    }
}
