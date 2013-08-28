/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import org.netbeans.api.visual.border.Border;

/**
 *
 * @author Peter Decsi
 */
public class JRLibNodeBorder implements Border {
    
    private Color borderColor;
    private Color bgColor;
    
    private Insets insets;
    private Stroke stroke;

    JRLibNodeBorder(int thickness, Color border, Color background) {
        this.borderColor = border;
        this.bgColor = background;
        this.insets = new Insets(thickness, thickness, thickness, thickness);
        this.stroke = new BasicStroke(thickness);
    }
    
    @Override
    public Insets getInsets() {
        return insets;
    }

    @Override
    public void paint(Graphics2D gr, Rectangle bounds) {
        Shape previousClip = clipArea(gr, bounds);
        drawBackGround(gr, bounds);
        drawBorder(gr, bounds);
        gr.setClip (previousClip);
    }
    
    private Shape clipArea(Graphics2D gr, Rectangle bounds) {
        Shape previousClip = gr.getClip ();
        gr.clip(new RoundRectangle2D.Float (bounds.x, bounds.y, bounds.width, bounds.height, 4, 4));
        return previousClip;
    }
    
    private void drawBackGround(Graphics2D gr, Rectangle bounds) {
        gr.setPaint(bgColor);
        int x = bounds.x;
        int width = bounds.width;
        Shape rect = new RoundRectangle2D.Float(x, bounds.y, width, bounds.height, 4, 4);
        gr.fill(rect);
    }
    
    private void drawBorder(Graphics2D gr, Rectangle bounds) {
        gr.setColor(borderColor);
        Stroke previousStroke = gr.getStroke ();
        gr.setStroke(stroke);
        gr.draw (new RoundRectangle2D.Float ((bounds.x) + 0.5f, bounds.y + 0.5f, bounds.width - 1, bounds.height - 1, 4, 4));
        gr.setStroke (previousStroke);
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
