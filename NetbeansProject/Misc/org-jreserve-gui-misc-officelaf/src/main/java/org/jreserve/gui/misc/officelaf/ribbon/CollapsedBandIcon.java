package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

import javax.swing.*;
import java.awt.*;

public class CollapsedBandIcon implements ResizableIcon {
    protected static final int SIZE = 31;
    protected static final int MAX_ICONSIZE = 20;
    protected static final int CORNER_SIZE = 3;
    protected static final int BAR_HEIGHT = 7;
    protected static final Insets INSETS = new Insets(3, 3, 0, 3);

    protected static final Color BORDER_COLOR = new Color(0xa0a0a0);
    protected static final Color BAR_COLOR = new Color(0xc2c2c2);

    protected Icon inner;

    public CollapsedBandIcon() {
        this(null);
    }

    public CollapsedBandIcon(Icon icon) {
        if (icon == null) {
            icon = new ImageIcon();
        } else if (icon.getIconHeight() > MAX_ICONSIZE || icon.getIconWidth() > MAX_ICONSIZE) {
            if (icon instanceof ResizableIcon) {
                ResizableIcon ri = (ResizableIcon) icon;
                ri.setDimension(new Dimension(16, 16));
            } else {
                throw new IllegalArgumentException("icon exceeds the max size (" + MAX_ICONSIZE + ")");
            }
        }
        this.inner = icon;
    }

    public int getIconHeight() {
        return SIZE + INSETS.top + INSETS.bottom;
    }

    public int getIconWidth() {
        return SIZE + INSETS.left + INSETS.right;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        x = x + INSETS.left;
        y = y + INSETS.top;
        int x1 = x + CORNER_SIZE;
        int x2 = x + SIZE - CORNER_SIZE - 1;
        int x3 = x + SIZE - 1;
        int y1 = y + CORNER_SIZE;
        int y2 = y + SIZE - CORNER_SIZE - 1;
        int y3 = y + SIZE - 1;

        // Draw background
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(Color.WHITE);
        for (int i=1; i<CORNER_SIZE; i++) {
            g2d.drawLine(x+i+1, y+CORNER_SIZE-i, x3-i-1, y+CORNER_SIZE-i);
        }
        g2d.drawLine(x+1, y1-1, x1, y);   // Corner: TL
        g2d.drawLine(x2, y, x3-1, y1-1);  // Corner: TR
        g2d.fillRect(x+1, y1, SIZE-2, SIZE-CORNER_SIZE-BAR_HEIGHT-1);
        g2d.setComposite(old);

        // Draw bar
        g2d.setColor(BAR_COLOR);
        g2d.fillRect(x+1, y+SIZE-BAR_HEIGHT-1, SIZE-2, BAR_HEIGHT);

        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.drawLine(x, y1-1, x1-1, y);   // Corner: TL
        g2d.drawLine(x1, y, x2, y);       // Line:   T
        g2d.drawLine(x2+1, y, x3, y1-1);  // Corner: TR
        g2d.drawLine(x3, y1, x3, y2);     // Line:   R
        g2d.drawLine(x3, y2+1, x2+1, y3); // Corner: BR
        g2d.drawLine(x1, y3, x2, y3);     // Line:   B
        g2d.drawLine(x1-1, y3, x, y2+1);  // Corner: BL
        g2d.drawLine(x, y1, x, y2);       // Line:   L

        g2d.dispose();

        // Paint the delegate in the center
        x1 = x + (SIZE - inner.getIconWidth()) / 2;
        y1 = y + (SIZE - BAR_HEIGHT - inner.getIconHeight()) / 2;
        inner.paintIcon(c, g, x1, y1);
    }

    public void setDimension(Dimension newDimension) {
    }
}
