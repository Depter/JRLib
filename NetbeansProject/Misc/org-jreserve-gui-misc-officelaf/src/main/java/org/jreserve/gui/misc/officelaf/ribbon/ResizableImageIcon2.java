package org.jreserve.gui.misc.officelaf.ribbon;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.jreserve.gui.misc.officelaf.icons.IconService;

/**
 *
 * @author gunnar
 */
public class ResizableImageIcon2 implements ResizableIcon {
    public static final int SMALL = 16;
    public static final int MEDIUM = 24;
    public static final int LARGE = 32;
    public static final int XL = 48;
    public static final int XXL = 64;

    private String filename;
    private Dimension currentSize;
    private int initialSize;
    private Map<Integer, Icon> sizes;
    private boolean paintAsDisabled;

    public ResizableImageIcon2(String filename) {
        this(filename, SMALL);
    }

    public ResizableImageIcon2(String filename, int initialSize) {
        this(filename, initialSize, false);
    }

    public ResizableImageIcon2(String filename, int initialSize, boolean paintAsDisabled) {
        this.filename = filename;
        this.initialSize = initialSize;
        this.currentSize = new Dimension(initialSize, initialSize);
        this.sizes = new HashMap<Integer, Icon>();
        this.paintAsDisabled = paintAsDisabled;
    }

    public String getFilename() {
        return filename;
    }

    public void revertToOriginalDimension() {
        currentSize.width = initialSize;
        currentSize.height = initialSize;
    }

    public void setDimension(Dimension dimension) {
        if (!currentSize.equals(dimension)) {
            currentSize.width = dimension.width;
            currentSize.height = dimension.height;
        }
    }

    public void setHeight(int height) {
        currentSize.height = height;
        currentSize.width = height;
    }

    public void setWidth(int width) {
        currentSize.width = width;
        currentSize.height = width;
    }

    public int getIconHeight() {
        return currentSize.height;
    }

    public int getIconWidth() {
        return currentSize.width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        int size = getNearestSize(Math.max(currentSize.width, currentSize.height));
        Icon icon = getIcon(size, c,g);
        if (icon == null) {
            System.err.println("No sizes available for icon: " + filename);
        } else {
            int xx = x;
            int yy = y;
            // Center the icon if it is smaller than the current size
            if (size != currentSize.width || size != currentSize.height) {
                xx += size == currentSize.width ? 0 : (int) Math.floor((currentSize.width - size) / 2);
                yy += size == currentSize.height ? 0 : (int) Math.floor((currentSize.height - size) / 2);
            }
            icon.paintIcon(c, g, xx, yy);
        }
    }

    private int getNearestSize(int size) {
        int[] sizeArray = {SMALL, MEDIUM, LARGE, XL, XXL};
        for (int i = sizeArray.length; i > 0; i--) {
            int s = sizeArray[i - 1];
            if (size >= s && sizeExists(s)) {
                return s;
            }
        }
        return SMALL;
    }

    private boolean sizeExists(int size) {
        if (sizes.containsKey(size) && sizes.get(size) != null) {
            return true;
        }
        return IconService.getInstance().hasIcon(getFilename(), size);
    }

    private Icon getIcon(int size, Component c, Graphics g) {
        Icon retVal = null;
        
        if (!sizes.containsKey(size)) {
            retVal = IconService.getInstance().getIcon(filename, size);
            if (paintAsDisabled) {
                Graphics2D g2d = (Graphics2D) g;
                BufferedImage srcImage = g2d.getDeviceConfiguration().createCompatibleImage(retVal.getIconWidth(), retVal.getIconHeight(), Transparency.TRANSLUCENT);
                Graphics2D sg = srcImage.createGraphics();
                sg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                retVal.paintIcon(c, sg, 0, 0);

                sg.dispose();
                ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                ColorConvertOp op = new ColorConvertOp(colorSpace, null);
                retVal = new ImageIcon(op.filter(srcImage, null));
            }
            sizes.put(size, retVal);
        }
        
        return sizes.get(size);
    }
}
