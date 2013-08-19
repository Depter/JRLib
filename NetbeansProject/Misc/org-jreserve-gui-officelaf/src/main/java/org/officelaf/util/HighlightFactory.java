package org.officelaf.util;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.WeakHashMap;
import java.util.Map;

public class HighlightFactory {
    private static final Color DEFAULT_HIGHLIGHT_COLOR = new Color(254, 240, 175, 160);

    private static Map<Color, BufferedImage> templates;

    static {
        templates = new WeakHashMap<Color, BufferedImage>();
    }

    private static BufferedImage getHightlightTemplate(Color color) {
        if (!templates.containsKey(color)) {
            BufferedImage template = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = template.createGraphics();
            Color transparent = new Color(color.getRGB() & 0x00FFFFFF, true);
            g2d.setPaint(new RadialGradientPaint(50, 50, 50, new float[] {0, 1},
                    new Color[] {color, transparent}));
            g2d.fillRect(0, 0, 100, 100);
            g2d.dispose();
            templates.put(color, template);
        }
        return templates.get(color);
    }

    public static BufferedImage createHighlight(int width, int height) {
        return createHighlight(DEFAULT_HIGHLIGHT_COLOR, width, height);
    }

    public static BufferedImage createHighlight(Color highlightColor, int width, int height) {
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = b.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(getHightlightTemplate(highlightColor), 0, 0, width, height, null);
        g2d.dispose();
        return b;
    }
}
