package org.officelaf.icons;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author mikael
 */
public class IconWrapper implements ExtendedIcon {
    Icon icon;
    String name;
    boolean scaleable;

    public IconWrapper(Icon icon, String name, boolean scaleable) {
        this.icon = icon;
        this.name = name;
        this.scaleable = scaleable;
    }



    @Override
    public boolean isScaleable() {
        return scaleable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        icon.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
