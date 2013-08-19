package org.officelaf.icons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;



/**
 *
 * @author gunnar
 */
public class Icons {
    public static boolean hasIcon(String name, int size) {
        return Icons.class.getResource(getIconPath(name, size)) != null;
    }
    
    private static String getIconPath(String name, int size) {
        String path = "/com/exie/icons/";
        if (isSvg(name)) {
            path += "svg/";
        } else {
            path += size + "x" + size + "/";
        }
        return path + name;
    }
    
    private static boolean isSvg(String name) {
        return name.toLowerCase().endsWith("svg");
    }
    
    public static Icon getIcon(String name) {
        return getIcon(name, 16);
    }

    //-Dnb.forceui=Vista
    public static Icon getIcon(String name, int size) {
        Icon retVal = null;

        String path = getIconPath(name, size);
        URL url = Icons.class.getResource(path);
        if (url != null && !isSvg(name)) {
            retVal = new ImageIcon(url);
        }

        if(retVal == null) {
            System.out.println("Missing icon: " + path + " size: " + size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            BufferedImage dummy = ge.getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(size, size);
            Graphics2D g2d = dummy.createGraphics();
            g2d.setColor(Color.RED);
            g2d.fillRect(0,0,size-1, size-1);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Error", 2, 2);
            g2d.dispose();
            retVal = new ImageIcon(dummy);
        }

        return retVal;
    }
}
