
package com.raven.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;


public class ModernComboBoxUI extends BasicComboBoxUI {
    
    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton("▼");
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBackground(new Color(240, 240, 240));
        return button;
    }

    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        g.setColor(new Color(255, 255, 255));
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 12, 12);
    }
}
