/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.officelaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.netbeans.swing.tabcontrol.plaf.AbstractTabCellRenderer;
import org.netbeans.swing.tabcontrol.plaf.TabPainter;

/**
 *
 * @author mikael
 */
public class OfficeTabCellRenderer extends AbstractTabCellRenderer {
    public static final int TOP_INSET    = 1;
    public static final int BOTTOM_INSET = 1;
    public static final int LEFT_INSET   = 1;
    public static final int RIGHT_INSET  = 1;
    
    protected static Color[] TITLEBAR_DIMMED_GRADIENT = {new Color(240, 241, 242), new Color(189, 194, 200)};

    public OfficeTabCellRenderer() {
        super(new OfficeTabPainter(), new Dimension(32,42));
    }
    
    static class OfficeTabPainter implements TabPainter {

        public void paintInterior(Graphics g, Component c) {
            int y = 0;
            int height = c.getHeight();
            int width  = c.getWidth();
            Graphics2D g2d = (Graphics2D) g;
            Color[] gradient = TITLEBAR_DIMMED_GRADIENT;
            g2d.setPaint(new GradientPaint(1, y, gradient[0], 1, y + height - 2, gradient[1]));
            g2d.fillRect(1, y, width - 1, height - 1);
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.WHITE);
            g.drawLine(x, y, x, y + height - 1);
            g.drawLine(x, y, x + width - 1, y);

            g.setColor(new Color(76, 83, 92));
            g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
            g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            /*g.setColor(Color.RED);
            g.drawRect(x, y, width-1, height-1);*/
        }

        public Polygon getInteriorPolygon(Component c) {
            Polygon retVal = new Polygon();

            Insets insets = getBorderInsets(c);
            int x = 0,y = 0;
            int width = c.getWidth() + 1;
            int height = c.getHeight() - insets.bottom;
            
            // plain rectangle
            retVal.addPoint(x,         y + insets.top);
            retVal.addPoint(x + width, y + insets.top);
            retVal.addPoint(x + width, y + height - 1);
            retVal.addPoint(x,         y + height - 1);
            
            return retVal;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(TOP_INSET, LEFT_INSET, BOTTOM_INSET, RIGHT_INSET);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void getCloseButtonRectangle(JComponent c, Rectangle rect, Rectangle bounds) {
            rect.setBounds(-20, -20, 0, 0);
        }

        public boolean supportsCloseButton(JComponent c) {
            return false;
        }
    } 
}
