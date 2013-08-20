package org.officelaf.icons;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author mikael
 */
public class LazyIcon implements ExtendedIcon {
    Icon icon;
    String name;
    boolean scaleable;
    LazyLoader<Icon> loader;
    int width, height;
    
    public LazyIcon(String name, int width, int height, boolean scaleable, LazyLoader<Icon> loader) {
        this.name = name;
        this.scaleable = scaleable;
        this.loader = loader;
        this.width = width;
        this.height = height;
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
        loader.getResource().paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
