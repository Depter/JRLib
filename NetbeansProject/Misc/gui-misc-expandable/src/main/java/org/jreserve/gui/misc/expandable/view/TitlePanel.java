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
package org.jreserve.gui.misc.expandable.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TitlePanel extends JPanel {
    
    private final static int VERTICAL_MARGIN = 4;
    private final static int HORIZONTAL_MARGIN = 4;
    private final static float HIGHT_PART = 0.33f;
    
    final static Color INNER_BORDER = new Color(255, 255, 255, 100);
    final static Color OUTTER_BORDER = new Color(50, 50, 50, 200);
    
    private Color color;
    
    private GradientPaint topPaint;
    
    TitlePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    TitlePanel(LayoutManager layout) {
        super(layout);
    }

    TitlePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public TitlePanel() {
        
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        int w = getHeight();
//        int h = getWidth();
//        initPaints(h);
//        
//        super.paintComponent(g);
//        
//        //Graphics2D g2d = (Graphics2D) g.create();
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        
//        Rectangle r2d = new Rectangle(0, 0, w, h);
//        g2d.clip(r2d);
//        g2d.setPaint(topPaint);
//        g.fillRect(0, 0, w, h);
//        
//        g2d.setColor(ExpandablePanelBorder.INNER_BORDER);
//        g2d.drawLine(0, h-2, w, h-2);
//        g2d.setColor(ExpandablePanelBorder.OUTTER_BORDER);
//        g2d.drawLine(0, h-1, w, h-1);
//    }
    
    @Override
    public void paint(Graphics g) {
        if (g instanceof Graphics2D) {
            paintBackground((Graphics2D) g);
            super.paintChildren(g);
        } else {
            super.paint(g);
        }
    }

    private void paintBackground(Graphics2D g) {
        int h = getHeight();
        int w = getWidth();
        initPaints(h);
        
        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Rectangle r2d = new Rectangle(0, 0, w, h);
        g.clip(r2d);
        g.setPaint(topPaint);
        g.fillRect(0, 0, w, h);
        
        g.setColor(ExpandablePanelBorder.INNER_BORDER);
        g.drawLine(0, h-2, w, h-2);
        g.setColor(ExpandablePanelBorder.OUTTER_BORDER);
        g.drawLine(0, h-1, w, h-1);
    }
    
    private void initPaints(float height) {
        initColor();
        float yTop = height * HIGHT_PART;
	topPaint = new GradientPaint(0f, 0f, Color.WHITE, 0f, yTop, color);
    }
    
    private void initColor() {
        if(color == null)
            color = getBackground();
    }
    
    @Override
    public void setBackground(Color color) {
        color = null;
        super.setBackground(color);
    }
        
    @Override
    public Dimension getPreferredSize() {
        Dimension size = getLayout().preferredLayoutSize(this);
        int w = size.width + 2 * HORIZONTAL_MARGIN;
        int h = size.height + 2 * VERTICAL_MARGIN;
        return new Dimension(w, h);
    }

}
