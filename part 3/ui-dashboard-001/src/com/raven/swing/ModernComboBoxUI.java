
package com.raven.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;


public class ModernComboBoxUI extends BasicComboBoxUI {
  private Color normalBorder = new Color(180, 180, 180);
    private Color hoverBorder = new Color(0, 0, 0);

    private Color normalBg = Color.WHITE;
    private Color hoverBg = new Color(245, 245, 245);

    private boolean hovering = false;

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JComboBox<?> combo = (JComboBox<?>) c;

        combo.setBorder(new EmptyBorder(2, 8, 2, 8));
        combo.setFont(combo.getFont().deriveFont(Font.PLAIN, 12f));
        combo.setBackground(normalBg);
        combo.setFocusable(false);
        combo.setOpaque(false);

        combo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                combo.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                combo.repaint();
            }
        });
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension size = super.getPreferredSize(c);
        size.height = 28; // Ajustar si querés más pequeño o más alto
        return size;
    }

    @Override
    protected JButton createArrowButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 8;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                g2.setColor(Color.BLACK);
                g2.fillPolygon(
                    new int[]{x, x + size / 2, x + size},
                    new int[]{y, y + size, y},
                    3
                );

                g2.dispose();
            }
        };
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(false);
        btn.setFocusable(false);
        return btn;
    }

    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 12;

        g2.setColor(hovering ? hoverBg : normalBg);
        g2.fillRoundRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, arc, arc);

        g2.setColor(hovering ? hoverBorder : normalBorder);
        g2.drawRoundRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1, arc, arc);

        g2.dispose();
    }
}
