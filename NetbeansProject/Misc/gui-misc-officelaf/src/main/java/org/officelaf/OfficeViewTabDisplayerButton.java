package org.officelaf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;

/**
 *
 * @author mikael
 */
public class OfficeViewTabDisplayerButton extends BasicButton {

    public OfficeViewTabDisplayerButton() {
    }

    public OfficeViewTabDisplayerButton(Action a) {
        super(a);
    }

    public OfficeViewTabDisplayerButton(Icon icon) {
        super(icon);
    }

    public OfficeViewTabDisplayerButton(String text) {
        super(text);
    }

    public OfficeViewTabDisplayerButton(String text, Icon icon) {
        super(text, icon);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Insets insets = getOuterBorder() != null ? getOuterBorder().getBorderInsets(this) : new Insets(0, 0, 0, 0);
        ButtonModel model = getModel();
        if (model.isRollover() || model.isPressed()) {
            int x1 = insets.left;
            int x2 = getWidth() - insets.right;
            int y1 = insets.top;
            int y2 = getHeight() - insets.bottom;
            int width = x2 - insets.left;
            int height = y2 - insets.top;

            Color startColor = model.isPressed() ? new Color(255, 171, 63) : new Color(255, 215, 103);
            Color endColor = model.isPressed() ? new Color(254, 225, 123) : new Color(255, 230, 159);

            g2d.setPaint(new GradientPaint(x1, y1, startColor, x2, y2, endColor));
            g2d.fillRect(x1, y1, width, height);
        }

        super.paintComponent(g);
    }
}

