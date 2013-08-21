package org.officelaf;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import org.netbeans.swing.plaf.LFCustoms;

/**
 * @author mikael
 */
class EditorToolbarBorder extends AbstractBorder {
    private static final Insets insets = new Insets(1,0,1,0);

    public EditorToolbarBorder() {
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color borderC = UIManager.getColor(LFCustoms.SCROLLPANE_BORDER_COLOR);
        g.setColor(borderC);
        g.drawLine(x, y + height -1, x + width - 1, y + height -1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }
}
