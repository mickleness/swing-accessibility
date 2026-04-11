package org.swing_ax;

import org.swing_ax.mac.*;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;

public class DemoUI extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TransparentPopupFactory.setActive(true);

                DemoUI d = new DemoUI();
                d.pack();
                d.setVisible(true);
            }
        });
    }

    public DemoUI() {
        setTitle("DemoUI Frame Title");

        DefaultListModel listModel = new DefaultListModel<>();
        listModel.addElement("Row 1");
        listModel.addElement("Row 2");
        listModel.addElement("Row 3");
        listModel.addElement("<html><u>Row 4, Underline</u></html>");
        JList<String> list = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(list);

        list.getAccessibleContext().setAccessibleName("The list");
        list.getAccessibleContext().setAccessibleDescription("This list is very important to me.");

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        getContentPane().add(listScrollPane, c);
        c.gridx++;
        getContentPane().add(panel2);

        JButton postSecret = new JButton("<html><u>postsecret.com</u></html>");
        postSecret.setBorderPainted(false);
        postSecret.putClientProperty(DefaultCAccessibilityHandler.PROPERTY_ACCESSIBLE_ROLE, MacAXRole.AXLink);
        // postSecret.putClientProperty(DefaultCAccessibilityHandler.PROPERTY_ACCESSIBLE_ROLE, AccessibleRole.HYPERLINK);
        postSecret.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        postSecret.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://postsecret.com/"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        panel2.add(postSecret);

        JButton button = new JButton("Sample Button");
        button.getAccessibleContext().setAccessibleName("Terrifying button");
        button.getAccessibleContext().setAccessibleDescription("You may experience untold horrors if you click this button");
        button.setToolTipText("I'm a little tooltip short and stout");
        panel2.add(button);

        JLabel label = new JLabel("Unused");
        panel2.add(label);
        label.setFocusable(true);

        JCheckBox checkbox = new JCheckBox("Sample JCheckBox");
        JRadioButton radioButton = new JRadioButton("Sample JRadioButton");
        panel2.add(checkbox);
        panel2.add(radioButton);

        JToggleButton toggleButton = new JToggleButton("Sample JToggleButton");
        panel2.add(toggleButton);
//        label.setLabelFor(toggleButton);

//        toggleButton.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                label.setText("Toggle button " + (toggleButton.isSelected() ? "is selected" : "is not selected"));
//                CAccessibilityController.valueChanged(toggleButton, "xyz");
//            }
//        });

        JProgressBar progressBar = new JProgressBar(50, 150);
        panel2.add(progressBar);

        JSlider slider = new JSlider(25, 45);
        panel2.add(slider);
//
//        Timer animatorTimer = new Timer(100, new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                incrementProgressBar();
//                decrementSlider();
//            }
//
//            private void incrementProgressBar() {
//                int v = progressBar.getValue();
//                v++;
//                if (v > progressBar.getMaximum()) {
//                    v = progressBar.getMinimum();
//                }
//                progressBar.setValue(v);
//            }
//
//            private void decrementSlider() {
//                int v = slider.getValue();
//                v--;
//                if (v < slider.getMinimum()) {
//                    v = slider.getMaximum();
//                }
//                slider.setValue(v);
//            }
//        });
//        animatorTimer.start();

        CAccessibilityController.get().addHandler(new DefaultCAccessibilityHandler());
    }
}