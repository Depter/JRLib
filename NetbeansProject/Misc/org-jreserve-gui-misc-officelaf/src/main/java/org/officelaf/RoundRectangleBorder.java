package org.officelaf;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 *
 * @author mikael
 */
public class RoundRectangleBorder extends AbstractBorder {
    private static final Color color64  = new Color(64,64,64);
    private static final Color color77  = new Color(77,77,77);
    private static final Color color102 = new Color(102,102,102);
    private static final Color color47  = new Color(47, 47, 47);
     
     
    Color foreground;
    Image corner;
    float arcwidth,archeight;
    int   titleheight;
    
    public RoundRectangleBorder(float arcwidth, float archeight, int titleheight, Image titleCorner) {
        this.foreground  = color47;
        this.arcwidth    = arcwidth;
        this.archeight   = archeight;
        this.titleheight = titleheight;
        this.corner      = titleCorner;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Color old = g2d.getColor();

        g.drawImage(corner,0,1,width,corner.getHeight(c),c);
        
        // Rely on cliping to remove unwanted pixels
        g2d.setColor(this.foreground);
        g2d.drawRect(x,y,width-1,height-1);
        /*g2d.setColor(Color.RED);
        g2d.drawLine(x+2, y+1, x+4, y+1);
        g2d.drawLine(x+1, y+2, x+1, y+3);
        g2d.drawLine(0, 0, width-1, height-1);*/

        drawToplessBorder(g,color77,  1, x,y,width,height);
        drawToplessBorder(g,color102, 2, x,y,width,height);
        drawToplessBorder(g,color64,  3, x,y,width,height);
        
        g2d.setColor(old);
    }

    protected void drawToplessBorder(Graphics g, Color c, int offset, 
                                     int x, int y, int width, int height) {
        int h1 = height - 1 - y;
        width  = width - 1 - offset;
        int x1 = x + offset;
        int y1 = height - 1 - offset;
        g.setColor(c);
        g.drawLine(x1, y+titleheight, x1, h1);       // Left
        g.drawLine(x1, y1, width, y1);                   // Bottom
        g.drawLine(width, h1, width, y+titleheight); // Right
        
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
    
    
    
    /**
     * This default implementation returns a new <code>Insets</code>
     * instance where the <code>top</code>, <code>left</code>,
     * <code>bottom</code>, and 
     * <code>right</code> fields are set to <code>0</code>.
     * @param c the component for which this border insets value applies
     * @return the new <code>Insets</code> object initialized to 0
     */
    public Insets getBorderInsets(Component c)       { 
        //return new Insets(1, 4, 1, 4);
        return new Insets(1, 4, 4, 4);
    }

    /** 
     * Reinitializes the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     * @return the <code>insets</code> object
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = 4; 
        insets.top  = insets.bottom = 1;
        insets.bottom = 4;
        return insets;
    }
   
}
