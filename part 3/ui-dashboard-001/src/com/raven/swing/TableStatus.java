package com.raven.swing;

import com.raven.model.StatusType;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class TableStatus extends JLabel {

    public StatusType getType() {
        return type;
    }

    public TableStatus() {
        setForeground(Color.WHITE);
    }

    private StatusType type;

    public void setType(StatusType type) {
        this.type = type;
        setText(type.toString());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (type != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            if (type == StatusType.PENDIENTE) {
                g = new GradientPaint(0, 0, new Color(255, 236, 148), 0, getHeight(), new Color(255, 223, 94));
            } else if (type == StatusType.LISTA) {
                g = new GradientPaint(0, 0, new Color(157, 229, 155), 0, getHeight(), new Color(123, 210, 121));
            } else if (type == StatusType.CANCELADA) {
                g = new GradientPaint(0, 0, new Color(255, 165, 165), 0, getHeight(), new Color(245, 135, 135));
            } else {
                // Por si aparece algún otro estado inesperado
               g = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, getHeight(), Color.GRAY);
    }
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }
}
