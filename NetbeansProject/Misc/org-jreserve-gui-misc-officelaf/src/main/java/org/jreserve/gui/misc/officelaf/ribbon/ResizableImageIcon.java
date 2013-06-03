package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.gui.misc.officelaf.icons.IconService;

/**
 * @author Gunnar A. Reinseth
 */
public class ResizableImageIcon extends ImageIcon implements ResizableIcon {
    public static final int SMALL = 16;
    public static final int MEDIUM = 24;
    public static final int LARGE = 32;
    public static final int XL = 48;
    public static final int XXL = 64;

    protected String name;
    protected Map<Integer, ImageIcon> sizes;
    protected Dimension size = new Dimension(0, 0);
    protected ImageIcon delegate;
    protected boolean keepAspect = true;

    public ResizableImageIcon(String name) {
        this(name, SMALL);
    }

    public ResizableImageIcon(String name, int size) {
        this.name = name;
        this.sizes = new HashMap<Integer, ImageIcon>();
        setDimension(new Dimension(size, size));
    }

    public String getName() {
        return name;
    }

    public void revertToOriginalDimension() {
    }

    public void setDimension(Dimension dimension) {
        /*int size = Math.max(dimension.width, dimension.height);
        if(size < SMALL) {
            throw new IllegalArgumentException("size is smaller than the minimum: " + SMALL);
        }*/
        if (!this.size.equals(dimension)) {
            this.size.width = dimension.width;
            this.size.height = dimension.height;
            this.delegate = null;
        }
    }

    public void setHeight(int i) {
        if (keepAspect) {
            setDimension(new Dimension(i, i));
        } else {
            setDimension(new Dimension(size.width, i));
        }
    }

    public void setWidth(int i) {
        if (keepAspect) {
            setDimension(new Dimension(i, i));
        } else {
            setDimension(new Dimension(i, size.height));
        }
    }

    protected ImageIcon getDelegate() {
        if (delegate == null) {
            int size = Math.max(this.size.width, this.size.height);
            size = getNearestSize(size);

            if (!sizes.containsKey(size)) {
                sizes.put(size, loadImage(size));
            }

            ImageIcon original = sizes.get(size);
            if (this.size.width != size || this.size.height != size) {
                int x = size == this.size.width ? 0 : (int) Math.floor((this.size.width - size) / 2);
                int y = size == this.size.height ? 0 : (int) Math.floor((this.size.height - size) / 2);
                BufferedImage buffer = new BufferedImage(this.size.width, this.size.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = buffer.createGraphics();
                g2d.drawImage(original.getImage(), x, y, null);
                g2d.dispose();
                delegate = new ImageIcon(buffer);
            } else {
                delegate = original;
            }
        }

        return delegate;
    }

    protected int getNearestSize(int size) {
        int[] sizeArray = {SMALL, MEDIUM, LARGE, XL, XXL};
        for (int i = sizeArray.length; i > 0; i--) {
            int s = sizeArray[i-1];
            if (size >= s && sizeExists(s)) {
                return s;
            }
        }
        return SMALL;
    }

    protected boolean sizeExists(int size) {
        return IconService.getInstance().hasIcon(getName(), size);
    }

    protected ImageIcon loadImage(int size) {
        return (ImageIcon) IconService.getInstance().getIcon(getName(), size);
    }

    public AccessibleContext getAccessibleContext() {
        return getDelegate().getAccessibleContext();
    }

    public String getDescription() {
        return getDelegate().getDescription();
    }

    public int getIconHeight() {
        return getDelegate().getIconHeight();
    }

    public int getIconWidth() {
        return getDelegate().getIconWidth();
    }

    public Image getImage() {
        return getDelegate().getImage();
    }

    public int getImageLoadStatus() {
        return getDelegate().getImageLoadStatus();
    }

    public ImageObserver getImageObserver() {
        return getDelegate().getImageObserver();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        getDelegate().paintIcon(c, g, x, y);
    }

    public void setDescription(String description) {
        getDelegate().setDescription(description);
    }

    public void setImage(Image image) {
        getDelegate().setImage(image);
    }

    public void setImageObserver(ImageObserver observer) {
        getDelegate().setImageObserver(observer);
    }

    public String toString() {
        return getDelegate().toString();
    }
}
