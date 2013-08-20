package org.officelaf.ribbon;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.HashMap;
import java.util.Map;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;


/**
 * @author Gunnar A. Reinseth
 */
public class TwoSizedIcon implements ResizableIcon {
    enum V {
        SMALL, SMALL_DISABLED, LARGE, LARGE_DISABLED;

        boolean isDisabled() {
            switch (this) {
                case SMALL_DISABLED:
                case LARGE_DISABLED:
                    return true;
                default:
                    return false;
            }
        }

        V getDisabled() {
            switch (this) {
                case SMALL:
                    return SMALL_DISABLED;
                case LARGE:
                    return LARGE_DISABLED;
                default:
                    return this;
            }
        }

        V getNormal() {
            switch (this) {
                case SMALL_DISABLED:
                    return SMALL;
                case LARGE_DISABLED:
                    return LARGE;
                default:
                    return this;
            }
        }
    }

    private V current;
    private Map<V, Icon> versions;

    private boolean paintAsDisabled;

    private Dimension size = new Dimension(0, 0);

    public TwoSizedIcon(Action fromAction) {
        this(fromAction, false);
    }

    public TwoSizedIcon(Action fromAction, boolean paintAsDisabled) {
        this((Icon) fromAction.getValue(Action.SMALL_ICON),
                (Icon) fromAction.getValue(Action.LARGE_ICON_KEY), paintAsDisabled);
    }

    public TwoSizedIcon(Icon smallIcon, Icon largeIcon) {
        this(smallIcon, largeIcon, false);
    }

    public TwoSizedIcon(Icon smallIcon, Icon largeIcon, boolean paintAsDisabled) {
        versions = new HashMap<V, Icon>(4);

        if (smallIcon != null) {
            versions.put(V.SMALL, smallIcon);
        }
        if (largeIcon != null) {
            versions.put(V.LARGE, largeIcon);
        }

        this.paintAsDisabled = paintAsDisabled;
        setSize(16);
    }

    public void revertToOriginalDimension() {
    }

    public void setDimension(Dimension newDimension) {
        setSize(newDimension.height);
    }

    public void setHeight(int height) {
        setSize(height);
    }

    public void setWidth(int width) {
        setSize(width);
    }

    protected void setSize(int size) {
        if (versions.containsKey(V.LARGE) && versions.containsKey(V.SMALL)) {
            Icon smallIcon = versions.get(V.SMALL);
            current = size <= smallIcon.getIconHeight() ? V.SMALL : V.LARGE;
        } else if (versions.containsKey(V.LARGE) && size >= versions.get(V.LARGE).getIconHeight()) {
            current = V.LARGE;
        } else if (versions.containsKey(V.SMALL) && size >= versions.get(V.SMALL).getIconHeight()) {
            current = V.SMALL;
        } else {
            current = null;
        }

        if (current != null && paintAsDisabled) {
            current = current.getDisabled();
        }

        this.size.height = size;
        this.size.width = size;
    }

    public int getIconHeight() {
        return size.height;
    }

    public int getIconWidth() {
        return size.width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Icon icon = getIcon(g);
        if (icon != null) {
            Rectangle r = new Rectangle(x, y, size.width, size.height);
            int xx = icon.getIconWidth() != size.width ?
                    Math.max(x, (int) r.getCenterX() - icon.getIconWidth()/2) : x;
            int yy = icon.getIconHeight() != size.height ?
                    Math.max(y, (int) r.getCenterY() - icon.getIconHeight()/2) : y;

            icon.paintIcon(c, g, xx, yy);
        }
    }

    private Icon getIcon(Graphics g) {
        if (current == null) {
            return null;
        }

        Icon icon;
        if (paintAsDisabled && versions.get(current) == null) {
            Icon normalIcon = versions.get(current.getNormal());

            Graphics2D g2d = (Graphics2D) g;
            BufferedImage srcImage = g2d.getDeviceConfiguration()
                    .createCompatibleImage(normalIcon.getIconWidth(), normalIcon.getIconHeight(), Transparency.TRANSLUCENT);
            Graphics2D sg = srcImage.createGraphics();
            sg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

//            Rectangle r = new Rectangle(0, 0, size.width, size.height);
//            int x = normalIcon.getIconWidth() != size.width ?
//                    Math.max(0, (int) r.getCenterX() - normalIcon.getIconWidth()/2) : 0;
//            int y = normalIcon.getIconHeight() != size.height ?
//                    Math.max(x, (int) r.getCenterY() - normalIcon.getIconHeight()/2) : 0;
            normalIcon.paintIcon(null, sg, 0, 0);

            sg.dispose();

            ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(colorSpace, null);
            icon = new ImageIcon(op.filter(srcImage, null));
            versions.put(current, icon);
        }

        return versions.get(current);
    }
}
