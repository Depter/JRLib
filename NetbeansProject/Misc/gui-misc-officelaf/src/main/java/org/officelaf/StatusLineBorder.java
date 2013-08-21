package org.officelaf;

import org.netbeans.swing.plaf.LFCustoms;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * (copy & paste of XP StatusLineBorder)
 * @author  S. Aubrecht
 */
class StatusLineBorder extends AbstractBorder {

    /** Constants for sides of status line border */
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 4;

    private Insets insets;

    private int type;

    /** Constructs new status line border of specified type. Type is bit
     * mask specifying which sides of border should be visible */
    public StatusLineBorder(int type) {
        this.type = type;
    }

    public void paintBorder(Component c, Graphics g, int x, int y,
    int w, int h) {
        g.translate(x, y);
        Color borderC = UIManager.getColor (LFCustoms.SCROLLPANE_BORDER_COLOR);
        g.setColor(borderC);
        // top
        if ((type & TOP) != 0) {
            g.drawLine(0, 0, w - 1, 0);
        }
        // left side
        if ((type & LEFT) != 0) {
            g.drawLine(0, 0, 0, h - 1);
        }
        // right side
        if ((type & RIGHT) != 0) {
            g.drawLine(w - 1, 0, w - 1, h - 1);
        }

        g.translate(-x, -y);
    }

    public Insets getBorderInsets(Component c) {
        if (insets == null) {
            insets = new Insets((type & TOP) != 0 ? 1 : 0,
            (type & LEFT) != 0 ? 1 : 0, 0,
            (type & RIGHT) != 0 ? 1 : 0);
        }
        return insets;
    }
}