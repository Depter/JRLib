package org.officelaf;

import org.officelaf.util.CommandButtonPainter;
import org.officelaf.util.HighlightFactory;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonTaskToggleButtonUI;

public class OfficeRibbonTaskToggleButtonUI extends BasicRibbonTaskToggleButtonUI {

    private static final Color normalBackground = new Color(0x716c5f);
    private static final Color normalBorder = new Color(0x969695);
    private static final Color normalBorderShadow = new Color(0x909090);
    private static final Color normalHighlight = new Color(0xecc328);
    private static final Color normalHighlight2 = new Color(0x989896);
    private static final Color selectedBackground1 = new Color(0xf1f2f2);
    private static final Color selectedBackground2 = new Color(0xd6d9df);
    private static final Color selectedHighlight1 = new Color(255, 255, 255, 104);
    private static final Color selectedHighlight2 = new Color(255, 255, 255, 41);
    private static final Color selectedBorder = new Color(0xbebebe);
    private static final Color selectedBorderShadow = new Color(0xc0c2c2);
    private static final Color selectedBorder2 = new Color(0xbe, 0xbe, 0xbe, 127);
    private static final Color selectedBorder3 = new Color(0xbe, 0xbe, 0xbe, 13);
    private static final Color cyan = new Color(0xc7fafe);
    private static final Color cyan_127 = getAlpha(cyan, 127);
    private static final Color cyan_0 = getAlpha(cyan, 0);
    private static final Color balck_17 = new Color(0, 0, 0, 17);
    private static final Color black_0 = new Color(0, 0, 0, 0);

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent c) {
        return new OfficeRibbonTaskToggleButtonUI();
    }

    @Override
    protected Color getForegroundColor(boolean isTextPaintedEnabled) {
        return isTextPaintedEnabled ? CommandButtonPainter.TEXT_COLOR : CommandButtonPainter.DISABLED_TEXT_COLOR;
    }

    @Override
    protected void paintButtonBackground(Graphics graphics, Rectangle toFill) {
        // Paint the background and set the foreground color
        ButtonModel model = commandButton.getActionModel();
        toFill.height -= 10; // the update() method adds 10 pixels to the height of the rectangle for some reason.
        if (model.isArmed() && model.isPressed() || model.isSelected()) {
            paintSelectedBackground(graphics, commandButton, toFill);
            graphics.setColor(Color.BLACK);
        } else {
            paintNormalBackground(graphics, commandButton, toFill);
            graphics.setColor(Color.WHITE);
        }
    }

    protected void paintNormalBackground(Graphics g, AbstractCommandButton button, Rectangle rect) {
        if (!button.getActionModel().isRollover()) {
            return; // no background when the mouse isn't over the button
        }

        int x = rect.x;
        int y = rect.y;
        int r = rect.x + rect.width - 1;
        int b = rect.y + rect.height - 1;

        Graphics2D g2d = (Graphics2D) g;

        // Paint the background
        g2d.setColor(normalBackground);
        g2d.fillRect(x + 3, y + 1, rect.width - 6, rect.height - 2);

        // Paint the border
        drawBorder(g2d, normalBorder, normalBorderShadow, x, y, r, b);

        int highlightHeight = (int) (rect.height * 0.9);
        Shape oldClip = g2d.getClip();

        BufferedImage highlight = HighlightFactory.createHighlight(normalHighlight, rect.width, highlightHeight);
        g2d.setClip(x + 2, y, rect.width - 4, rect.height - 1);
        g2d.drawImage(highlight, x, b - (highlightHeight / 2), null);

        highlight = HighlightFactory.createHighlight(normalHighlight2, rect.width + 16, highlightHeight);
        g2d.setClip(x + 3, y + 1, rect.width - 6, rect.height - 2);
        g2d.drawImage(highlight, x - 8, y - (highlightHeight / 2), null);

        g2d.setClip(oldClip);
    }

    protected void paintSelectedBackground(Graphics g, AbstractCommandButton button, Rectangle rect) {
        Graphics2D g2d = (Graphics2D) g;

        int x = rect.x;
        int y = rect.y;
        int r = rect.x + rect.width - 1;
        int b = rect.y + rect.height - 1;

        // Paint the main background gradient
        g2d.setPaint(new LinearGradientPaint(x, y + 1, x, b,
                new float[]{0, 1},
                new Color[]{selectedBackground1, selectedBackground2}));
        g2d.fillRect(x + 3, y + 1, rect.width - 6, rect.height - 2);
        g2d.drawLine(x + 2, b, r - 2, b);

        // Paint the white highlight
        Color white_104 = selectedHighlight1;
        Color white_41 = selectedHighlight2;
        g2d.setColor(white_104);
        g2d.fillRect(x + 3, y + 1, rect.width - 6, 3); // top box
        g2d.fillRect(x + 3, y + 4, 2, rect.height - 6); // left box
        g2d.fillRect(r - 4, y + 4, 2, rect.height - 6); // right box
        g2d.setColor(white_41);
        g2d.drawLine(x + 3, b - 1, x + 4, b - 1); // bottom left fadout
        g2d.drawLine(x + 2, b, x + 3, b);
        g2d.drawLine(r - 3, b - 1, r - 4, b - 1); // bottom right fadeout
        g2d.drawLine(r - 2, b, r - 3, b);

        // Paint the border
        drawBorder(g2d, selectedBorder, selectedBorderShadow, x, y, r, b);

        // Paint the top corners of the border
//        Color gray2 = new Color(0xc0c2c2);
//        Color gray2_127 = new Color(0xc0, 0xc2, 0xc2, 127);
//        g2d.setColor(gray2);
//        g2d.drawLine(x+3, y+1, x+3, y+1);
//        g2d.drawLine(r-3, y+1, r-3, y+1);
//        g2d.setColor(gray2_127);
//        g2d.drawLine(x+3, y+2, x+3, y+2);
//        g2d.drawLine(x+4, y+1, x+4, y+1);
//        g2d.drawLine(r-3, y+2, r-3, y+2);
//        g2d.drawLine(r-4, y+1, r-4, y+1);

        // Paint the bottom corners of the border
        Color gray_127 = selectedBorder2;
        Color gray_13 = selectedBorder3;
        g2d.setColor(gray_127);
        g2d.drawLine(x + 1, b - 1, x + 1, b - 1);
        g2d.drawLine(r - 1, b - 1, r - 1, b - 1);
        g2d.setColor(gray_13);
        g2d.drawLine(x, b - 1, x, b - 1);
        g2d.drawLine(r, b - 1, r, b - 1);
        g2d.drawLine(x + 1, b - 2, x + 1, b - 2);
        g2d.drawLine(r - 1, b - 2, r - 1, b - 2);

        // Paint the cyan highlight
        // Left
        g2d.setColor(cyan);
        g2d.drawLine(x + 3, y + 3, x + 3, b - 2); // left line
        g2d.setColor(cyan_127);
        g2d.drawLine(x + 3, y + 2, x + 3, y + 2); // top fadeout

        // Right
        g2d.setPaint(new LinearGradientPaint(x, y, x, y + 7, new float[]{0, 1}, new Color[]{cyan_0, cyan}));
        g2d.drawLine(r - 3, y + 3, r - 3, b - 2);

        // Bottom fadeouts
        g2d.setColor(cyan_127);
        g2d.drawLine(x + 2, b - 1, x + 3, b - 1); // bottom left fadeout
        g2d.drawLine(x + 1, b, x + 2, b);
        g2d.drawLine(r - 2, b - 1, r - 3, b - 1); // bottom right fadeout
        g2d.drawLine(r - 1, b, r - 2, b);

        // Top
        g2d.setPaint(new LinearGradientPaint(x + 3, y + 1, r - 3, y + 1, new float[]{0, 1}, new Color[]{cyan_127, cyan_0}));
        g2d.drawLine(x + 4, y + 1, r - 4, y + 1);

        // Paint the shadows
        g2d.setPaint(new LinearGradientPaint(x, y, x, b, new float[]{0, 0.5f}, new Color[]{black_0, balck_17}));
        g2d.drawLine(x + 1, y, x + 1, b - 3);
        g2d.drawLine(r - 1, y, r - 1, b - 3);
    }

    private void drawBorder(Graphics2D g2d, Color base, Color cornerShadow, int x, int y, int r, int b) {
        float[] borderFractions = {0, 1};
        Color[] borderColors = new Color[]{getAlpha(base, 0), base};
        g2d.setPaint(new LinearGradientPaint(x + 2, y, x + 6, y, borderFractions, borderColors));
        g2d.drawLine(x + 2, y, r - 7, y); // top left fadeout and top line
        g2d.setPaint(new LinearGradientPaint(r - 2, y, r - 6, y, borderFractions, borderColors));
        g2d.drawLine(r - 2, y, r - 6, y); // top right fadeout
        g2d.setPaint(new LinearGradientPaint(x + 1, y, x + 1, y + 4, borderFractions, borderColors));
        g2d.drawLine(x + 2, y, x + 2, b - 1); // left line
        g2d.drawLine(r - 2, y, r - 2, b - 1); // right line

        g2d.setColor(cornerShadow);
        g2d.drawLine(x + 3, y + 1, x + 3, y + 1);
        g2d.drawLine(r - 3, y + 1, r - 3, y + 1);
        g2d.setColor(getAlpha(cornerShadow, 127));
        g2d.drawLine(x + 3, y + 2, x + 3, y + 2);
        g2d.drawLine(x + 4, y + 1, x + 4, y + 1);
        g2d.drawLine(r - 3, y + 2, r - 3, y + 2);
        g2d.drawLine(r - 4, y + 1, r - 4, y + 1);

    }

    private static Color getAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
