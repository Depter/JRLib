/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AnchorWidget extends Widget {

    private final static Dimension SIZE = new Dimension(16, 16);
    
    public AnchorWidget(Scene scene, boolean isInput, JRLibWidgetUI ui) {
        super(scene);
        setMinimumSize(SIZE);
        setMaximumSize(SIZE);
        setPreferredSize(SIZE);
        setOpaque(false);
        setBorder(new AnchorBorder(ui.getAnchorColor(isInput)));
    }
    
    private static class AnchorBorder implements Border {
        
        private final static Insets INSETS = new Insets(0, 0, 0, 0) ;
        private final Color color;

        public AnchorBorder(Color color) {
            this.color = color;
        }
        
        @Override
        public Insets getInsets() {
            return INSETS;
        }

        @Override
        public void paint(Graphics2D gr, Rectangle bounds) {
            Shape previousClip = gr.getClip();

            gr.setPaint(Color.BLACK);
            gr.drawOval(bounds.x+3, bounds.y+3, 10, 10);
            
            gr.setPaint(color);
            gr.fillOval(bounds.x+4, bounds.y+4, 8, 8);

            gr.setClip(previousClip);
        }

        @Override
        public boolean isOpaque() {
            return true;
        }
    }
}
