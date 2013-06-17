/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.dummy.claimtriangle.edtior.nodes.widgets;

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
 * @version 1.0
 */
class JRLibNodeBorder implements Border {
    
    private final static Color INPUT_COLOR = new Color(90, 190, 90);
    private final static Color OUTPUT_COLOR = new Color(240, 210, 40);
    private final static int ANCHOR_SIZE = 4;
    
    private Color borderColor;
    private Color bgColor;
    private int thickness;
    
    private Insets insets;
    private Stroke stroke;

    JRLibNodeBorder(int thickness, Color border, Color background) {
        this.borderColor = border;
        this.bgColor = background;
        this.thickness = thickness;
        this.insets = new Insets(thickness, thickness+ANCHOR_SIZE, thickness, thickness+ANCHOR_SIZE);
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
        drawInputAnchor(gr, bounds);
        drawOutputAnchor(gr, bounds);
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
        int x = bounds.x + ANCHOR_SIZE;
        int width = bounds.width - 2 * ANCHOR_SIZE;
        Shape rect = new RoundRectangle2D.Float(x, bounds.y, width, bounds.height, 4, 4);
        gr.fill(rect);
    }
    
    private void drawBorder(Graphics2D gr, Rectangle bounds) {
        gr.setColor(borderColor);
        Stroke previousStroke = gr.getStroke ();
        gr.setStroke(stroke);
        gr.draw (new RoundRectangle2D.Float ((bounds.x+ANCHOR_SIZE) + 0.5f, bounds.y + 0.5f, bounds.width - 1 - 2*ANCHOR_SIZE, bounds.height - 1, 4, 4));
        gr.setStroke (previousStroke);
    }
    
    private void drawInputAnchor(Graphics2D gr, Rectangle bounds) {
        drawAnchor(gr, bounds, INPUT_COLOR, bounds.x+1);
    }
    
    private void drawAnchor(Graphics2D gr, Rectangle bounds, Color color, int x) {
        int y = bounds.y + bounds.height/2 - ANCHOR_SIZE;
        int diameter = 2*ANCHOR_SIZE;
        gr.setColor(color);
        
        gr.fillOval(x, y, diameter, diameter);
        
        gr.setColor(borderColor);
        gr.drawOval(x, y, diameter, diameter);
    }
    
    private void drawOutputAnchor(Graphics2D gr, Rectangle bounds) {
        drawAnchor(gr, bounds, OUTPUT_COLOR, bounds.x + bounds.width - 1 - 2*ANCHOR_SIZE);
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
    
}
