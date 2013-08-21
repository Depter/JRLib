package org.officelaf;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DoubleArrowIcon implements Icon {
    private static final Color WHITE_60A = new Color(255,255,255,60);
    private static final Color GRAY      = new Color(49, 52, 49);

    public enum Orientation {
        UP    (Math.toRadians(90D),   9,  10),
        DOWN  (Math.toRadians(-90D),  9,  10),
        LEFT  (Math.toRadians(0),    10,  9),
        RIGHT (Math.toRadians(180D), 10,  9);

        double rotation;
        int width, height;
        BufferedImage image;

        Orientation(double radians, int width, int height) {
            rotation = radians;
            this.width = width;
            this.height = height;
        }


        public double getRotation() {
            return rotation;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public BufferedImage getImage() {
            return image;
        }


        public void setImage(BufferedImage image) {
            this.image = image;
        }
    }

    Orientation orientation;

    public DoubleArrowIcon() {
        this(Orientation.LEFT);
    }

    public DoubleArrowIcon(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getIconHeight() {
        return orientation.getHeight();
    }

    public int getIconWidth() {
        return orientation.getWidth();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        if(orientation.getImage() == null) { // This is not mts
            GraphicsConfiguration dc = g2d.getDeviceConfiguration();
            BufferedImage bi = dc.createCompatibleImage(orientation.getWidth(), orientation.getHeight(),
                                                        Transparency.TRANSLUCENT);
            Graphics2D g2 = bi.createGraphics();
            g2.rotate(orientation.getRotation(), 5, 4);
            paintArrow(g2,0,0);
            paintArrow(g2,4,0);
            g2.dispose();
            orientation.setImage(bi);
        }

        g2d.drawImage(orientation.getImage(),x,y,null);
    }

    protected void paintArrow(Graphics2D g2d, int x, int y) {
        // Draw blue arrow
        g2d.setColor(GRAY);
        g2d.drawLine(x+4, y+1, x+1, y+4);
        g2d.drawLine(x+4, y+2, x+2, y+4);
        g2d.drawLine(x+2, y+5, x+4, y+7);
        g2d.drawLine(x+3, y+5, x+4, y+6);

        // Draw White border
        g2d.setColor(WHITE_60A);
        g2d.drawLine(x+4,y,  x,  y+4);
        g2d.drawLine(x+1,y+5,x+4,y+8);
        g2d.drawLine(x+5,y,  x+5,y+2);
        g2d.drawLine(x+4,y+3,x+3,y+4);
        g2d.drawLine(x+4,y+5,x+5,y+6);
        g2d.drawLine(x+5,y+7,x+5,y+8);
    }
}
