package org.officelaf.util;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.CommandButtonLayoutManager;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.model.PopupButtonModel;

// TODO Revisit for button strip painting
// TODO make colors static final

public class CommandButtonPainter {
    public static final Color NORMAL_BORDER_COLOR = new Color(0xb5bfc2);
    public static final Color TEXT_COLOR = new Color(0x464646);
    public static final Color DISABLED_TEXT_COLOR = Color.GRAY;

    static class RolloverColors {
        /* The rollover background gradient */
        protected static final Color[] BG = new Color[] {
                new Color(0xfffcd9), new Color(0xffe78f), new Color(0xffd74a), new Color(0xffe794)};

        /* The rollover border gradient in BIG state */
        protected static final Color[] BORDER_BIG = new Color[] {
                new Color(0xddcf9b), new Color(0xc0a776), new Color(0xded6bb)};

        /* The rollover border gradient in SMALL/MEDIUM state */
        protected static final Color[] BORDER_SMALL = new Color[] {
                new Color(0xdbce99), new Color(0xb79e73), new Color(0xc0b196)};

        /* The rollover gradient for the top, left and right border highlight */
        protected static final Color[] HIGHLIGHT1 =
                new Color[] {new Color(255, 255, 255, 222), new Color(254, 247, 201, 222)};

        /* The rollover gradient for the bottom border highlight */
        protected static final Color[] HIGHLIGHT2 =
                new Color[] {new Color(0xfffacf), Color.WHITE, new Color(0xfffacf)};

        /* The rollover gradient for the left and right border highlight (applies only for BIG state) */
        protected static final Color[] HIGHLIGHT3 =
                new Color[] {new Color(0x00fff35d, true), new Color(0xfff35d)};

        /* The rollover gradient for the bottom border highlight (applies only for BIG state) */
        protected static final Color[] HIGHLIGHT4 =
                new Color[] {new Color(0xfff792), new Color(0x00fff792, true), new Color(0x00fff792, true), new Color(0xfff792)};

        /* The rollover top corner color (for smoothing the corners) */
        protected static final Color CORNER1 = new Color(0xeae2a8);

        /* The rollover bottom corner color (for smoothing the corners) */
        protected static final Color CORNER2 = new Color(0xf0e3bc);

        /* The rollover background gradient fractions in BIG state */
        protected static final float[] BG_FRACTIONS_BIG = {0, 0.375f, 0.376f, 1};

        /* The rollover background gradient fractions in SMALL/MEDIUM state */
        protected static final float[] BG_FRACTIONS_SMALL = {0, 0.49f, 0.5f, 1};

        /* The rollover border gradient fractions */
        protected static final float[] BORDER_FRACTIONS = {0, 0.5f, 1};

        /* The rollover gradient fractions for the top, left and right border highlight */
        protected static final float[] HIGHLIGHT1_FRACTIONS = new float[] {0, 1};

        /* The rollover gradient fractions for the bottom border highlight */
        protected static final float[] HIGHLIGHT2_FRACTIONS = new float[] {0, 0.5f, 1};

        /* The rollover gradient fractions for the left and right border highlights (applies only for BIG state) */
        protected static final float[] HIGHLIGHT3_FRACTIONS = {0.5f, 1};

        /* The rollover gradient fractions for the bottom border highlight (applies only for BIG state) */
        protected static final float[] HIGHLIGHT4_FRACTIONS = {0, 0.45f, 0.55f, 1};
    }

    static class CollapsedBandColors {
        /** The start color of the border gradient. */
        protected static final Color border1 = new Color(0xb6b7b7);

        /** The end color of the border gradient. */
        protected static final Color border2 = new Color(0x707070);

        /** The start color of the border gradient when in rollover state. */
        protected static final Color border1_over = new Color(0xbcc1d6);

        /** The end color of the border gradient when in rollover state. */
        protected static final Color border2_over = new Color(0x748dbb);

        /** The start color of the border gradient when in pressed state. */
        protected static final Color border1_pressed = new Color(0x7c7d7d);

        /** The middle color of the border gradient when in pressed state. */
        protected static final Color border2_pressed = new Color(0x949494);

        /** The end color of the border gradient when in pressed state. */
        protected static final Color border3_pressed = new Color(0xbababa);

        /** The first border shadow color when in pressed state. */
        protected static final Color border1_pressed_140 =
                new Color(border1_pressed.getRed(), border1_pressed.getGreen(), border1_pressed.getBlue(), 140);

        /** The second border shadow color when in pressed state. */
        protected static final Color border1_pressed_90 =
                new Color(border1_pressed.getRed(), border1_pressed.getGreen(), border1_pressed.getBlue(), 90);

        /** The third border shadow color when in pressed state. */
        protected static final Color border1_pressed_45 =
                new Color(border1_pressed.getRed(), border1_pressed.getGreen(), border1_pressed.getBlue(), 45);

        /** The start color of the border highlight gradient. */
        protected static final Color highlight1 = new Color(255, 255, 255, 127);

        /** The end color of the border highlight gradient. */
        protected static final Color highlight2 = new Color(220, 220, 220, 127);

        /** The start color of the border highlight gradient when in pressed state. */
        protected static final Color highlight1_pressed = new Color(255, 255, 255, 50);

        /** The end color of the border highlight gradient when in pressed state. */
        protected static final Color highlight2_pressed = new Color(255, 255, 255, 30);

        /** The background color. */
        protected static final Color background = new Color(207, 215, 229, 60);

        /** The background color when in pressed state. */
        protected static final Color background_pressed = new Color(125, 125, 125, 30);
    }

    private AbstractCommandButton commandButton;
    private boolean isRollover = false;
    private boolean isPressed = false;
    private boolean isSelected = false;
    private boolean isPopupRollover = false;
    private boolean isActionEnabled = false;
    private boolean isPopupEnabled = false;
    private boolean isPopupOnly = false;
    private boolean isInStrip = false;
    private boolean isFirstInStrip = false;
    private boolean isLastInStrip = false;
    private boolean isStripVertical = false;

    private boolean ignorePressed = false;

    private BufferedImage highlightImage;

    public CommandButtonPainter(AbstractCommandButton commandButton) {
        this(commandButton, false);
    }

    public CommandButtonPainter(AbstractCommandButton commandButton, boolean ignorePressed) {
        this.commandButton = commandButton;
        this.ignorePressed = ignorePressed;
    }

    protected void updateState() {
        ButtonModel actionModel = commandButton.getActionModel();
        PopupButtonModel popupModel = null;

        if (commandButton instanceof JCommandButton) {
            JCommandButton jcb = (JCommandButton) commandButton;
            popupModel = jcb.getPopupModel();
            isPopupOnly = jcb.getCommandButtonKind() == JCommandButton.CommandButtonKind.POPUP_ONLY;
        }

        isActionEnabled = actionModel != null && actionModel.isEnabled();
        isPopupEnabled = popupModel != null && popupModel.isEnabled();
        isPressed = !ignorePressed && (actionModel != null && actionModel.isPressed()) ||
                (popupModel != null && popupModel.isPressed());
        isSelected = (actionModel != null && actionModel.isSelected()) ||
                (popupModel != null && popupModel.isSelected());
        isRollover = (actionModel != null && actionModel.isRollover()) ||
                (popupModel != null && (popupModel.isRollover() || popupModel.isPopupShowing()));
        isPopupRollover = popupModel != null && popupModel.isRollover();

        if (commandButton.getParent() instanceof JCommandButtonStrip) {
            JCommandButtonStrip strip = (JCommandButtonStrip) commandButton.getParent();
            isInStrip = true;
            isFirstInStrip = strip.isFirst(commandButton);
            isLastInStrip = strip.isLast(commandButton);
            isStripVertical = strip.getOrientation() == JCommandButtonStrip.StripOrientation.VERTICAL;
        }
    }

    public void paintBackground(Graphics g, Rectangle toFill) {
        Graphics2D g2d = (Graphics2D) g.create();
        int x = toFill.x;
        int y = toFill.y;
        int width = toFill.width;
        int height = toFill.height;

        updateState();

        if (isPressed) {
            this.paintPressedBackground(g2d, x, y, width, height);
        } else if (isSelected) {
            this.paintSelectedBackground(g2d, toFill.x, toFill.y, toFill.width, toFill.height);
        } else if (isRollover) {
            this.paintRolloverBackground(g2d, x, y, width, height);
        } else if (!commandButton.isFlat()) {
            paintNormalBackground(g2d, x, y, width, height);
        }

        g2d.dispose();
    }

    protected void paintNormalBackground(Graphics2D g2d, int x, int y, int width, int height) {
        int r = x + width - 1;
        int b = y + height - 1;
        int x1, x2, y1, y2;

        // Draw the background
        x1 = isInStrip && !isStripVertical && !isFirstInStrip ? x : x + 1;
        y1 = y;
        x2 = isInStrip && !isLastInStrip && !isStripVertical ? r : r - 1;
        y2 = isInStrip && !isLastInStrip && isStripVertical ? b : b - 1;
        g2d.setPaint(new LinearGradientPaint(x1, y1, x1, y2, new float[] {0, 0.4f, 0.41f, 1},
                new Color[] {new Color(0xd6dedf), new Color(0xdbe2e4), new Color(0xd2d9db), new Color(0xe0e5e7)}));
        g2d.fillRect(x1, y1, x2-x1+1, y2-y1+1);

        // Draw the highlight
        setAlpha(g2d, 0.3f);
        g2d.setColor(Color.WHITE);

        // Top
        x1 = x + 1;
        y1 = isInStrip && isStripVertical && !isFirstInStrip ? y : y + 1;
        x2 = r - 1;
        y2 = y1;
        g2d.drawLine(x1, y1, x2, y2);

        // Bottom
        if (!isInStrip || !isStripVertical || (isLastInStrip && isStripVertical)) {
            x1 = x + 1;
            y1 = b - 1;
            x2 = r - 1;
            y2 = y1;
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Left
        x1 = isInStrip && !isStripVertical && !isFirstInStrip ? x : x + 1;
        y1 = y + 1;
        x2 = x1;
        y2 = b - 1;
        g2d.drawLine(x1, y1, x2, y2);

        // Right
        if (!isInStrip || isStripVertical || (isLastInStrip && !isStripVertical)) {
            x1 = r - 1;
            y1 = y + 1;
            x2 = x1;
            y2 = b - 1;
            g2d.drawLine(x1, y1, x2, y2);
        }

        resetAlpha(g2d);
        
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
//        g2d.setColor(Color.WHITE);
//        g2d.drawRect(x+1, y+1, width-3, height-3);
//        g2d.setComposite(old);

        // Draw the border
        g2d.setColor(NORMAL_BORDER_COLOR);

        // Top
        if (!isInStrip || !isStripVertical || (isFirstInStrip && isStripVertical)) {
            x1 = !isInStrip || isFirstInStrip ? x + 1 : x;
            y1 = y;
            x2 = !isInStrip || (isLastInStrip || (isFirstInStrip && isStripVertical)) ? r-1 : r;
            y2 = y1;
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Bottom
        x1 = !isInStrip || isFirstInStrip ? x + 1 : x;
        y1 = b;
        x2 = !isInStrip || (isLastInStrip || (isFirstInStrip && isStripVertical)) ? r-1 : r;
        y2 = y1;
        g2d.drawLine(x1, y1, x2, y2);

        // Left
        if (!isInStrip || isStripVertical || (isFirstInStrip && !isStripVertical)) {
            x1 = x;
            y1 = !isInStrip || isFirstInStrip ? y + 1 : y;
            x2 = x;
            y2 = !isInStrip || (isFirstInStrip || (isLastInStrip && isStripVertical)) ? b - 1 : b;
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Right
        x1 = r;
        y1 = !isInStrip || ((isLastInStrip && !isStripVertical) || (isFirstInStrip && isStripVertical)) ? y + 1 : y;
        x2 = r;
        y2 = !isInStrip || isLastInStrip ? b - 1 : b;
        if (!isLastInStrip && !isStripVertical) {
            setAlpha(g2d, 0.3f);
        }
        g2d.drawLine(x1, y1, x2, y2);
        if (!isLastInStrip && !isStripVertical) {
            resetAlpha(g2d);
        }

        // Draw semi-transparent pixels to smooth the corners
        setAlpha(g2d, 0.5f);
        if (!isInStrip || isFirstInStrip) {
            g2d.drawLine(x+1, y+1, x+1, y+1); // top left
        }
        if (!isInStrip || (isFirstInStrip && isStripVertical) || (isLastInStrip && !isStripVertical)) {
            g2d.drawLine(r-1, y+1, r-1, y+1); // top right
        }
        if (!isInStrip || isLastInStrip) {
            g2d.drawLine(r-1, b-1, r-1, b-1); // bottom right
        }
        if (!isInStrip || (isFirstInStrip && !isStripVertical) || (isLastInStrip && isStripVertical)) {
            g2d.drawLine(x+1, b-1, x+1, b-1); // bottom left
        }
        resetAlpha(g2d);
    }

    /**
     * Paints the background in selected state.
     *
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    protected void paintSelectedBackground(Graphics2D g, int x, int y, int width, int height) {
        CommandButtonDisplayState state = commandButton.getDisplayState();
        if (state == CommandButtonDisplayState.BIG) {
                if (!isSplit()) {
                    paintBigSelectedBackground(g, x, y, width, height);
                } else {
                    Rectangle actionRect = getActionClickArea();
                    Rectangle popupRect = getPopupClickArea();
                    g.setClip(actionRect);
                    paintBigSelectedBackground(g, x, y, width, height);
                    g.setClip(popupRect);
                    if (isRollover && !isPopupRollover) {
                        paintBigRolloverBackground(g, x, y, width, height, true, isPopupEnabled);
                    } else if (isPopupRollover) {
                        paintBigRolloverBackground(g, x, y, width, height, false, isPopupEnabled);
                    } else {
                        paintBigSelectedBackground(g, x, y, width, height);
                    }
                }
        } else {
            if (isInStrip) {
                paintNormalBackground(g, x, y, width, height);
            }
            if (!isSplit()) {
                paintSmallSelectedBackground(g, x, y, width, height);
            } else {
                Rectangle actionRect = getActionClickArea();
                Rectangle popupRect = getPopupClickArea();
                g.setClip(actionRect);
                paintSmallSelectedBackground(g, x, y, width, height);
                g.setClip(popupRect);
                if (isRollover && !isPopupRollover) {
                    paintSmallRolloverBackground(g, x, y, width, height, true, isPopupEnabled);
                } else if (isPopupRollover) {
                    paintSmallRolloverBackground(g, x, y, width, height, false, isPopupEnabled);
                } else {
                    paintSmallSelectedBackground(g, x, y, width, height);
                }
            }
        }
    }
    
    private Rectangle getActionClickArea() {
        if(commandButton == null)
            return new Rectangle(0, 0, 0, 0);
        CommandButtonLayoutManager.CommandButtonLayoutInfo info = commandButton.getUI().getLayoutInfo();
        if(info == null)
            return new Rectangle(0, 0, 0, 0);
        if(info.actionClickArea==null)
            return new Rectangle(0, 0, 0, 0);
        return new Rectangle(info.actionClickArea);
    }
    
    private Rectangle getPopupClickArea() {
        if(commandButton == null)
            return new Rectangle(0, 0, 0, 0);
        CommandButtonLayoutManager.CommandButtonLayoutInfo info = commandButton.getUI().getLayoutInfo();
        if(info == null)
            return new Rectangle(0, 0, 0, 0);
        if(info.popupClickArea==null)
            return new Rectangle(0, 0, 0, 0);
        return new Rectangle(info.popupClickArea);
    }

    private void paintSmallSelectedBackground(Graphics2D g2d, int x, int y, int width, int height) {
        int r = x + width - 1;
        int b = y + height - 1;

        // Draw the background
        int bgx1, bgx2, bgy1, bgy2;
        float[] bgFractions;
        if (!isInStrip) {
            bgx1 = x + 2;
            bgx2 = r - 2;
            bgy1 = y + 4;
            bgy2 = b - 2;
            bgFractions = new float[] {0, 0.499f, 0.5f, 1};
        } else {
            bgx1 = isFirstInStrip ? x + 1 : x;
            bgx2 = r - 1;
            bgy1 = y + 1;
            bgy2 = b - 1;
            bgFractions = isRollover ? new float[] {0, 0.35f, 0.351f, 1} : new float[] {0, 0.3f, 0.31f, 1};
        }
        int bgw = bgx2 - bgx1 + 1;
        int bgh = bgy2 - bgy1 + 1;
        if (!isInStrip) {

        } else {

        }
        Color[] bgGradient = isRollover ?
                new Color[] {new Color(0xF6AA69), new Color(0xF39954), new Color(0xF0822A), new Color(0xF4961C)} :
                new Color[] {new Color(0xfddab1), new Color(0xfbc380), new Color(0xf9ab48), new Color(0xfde294)};
        g2d.setPaint(new LinearGradientPaint(bgx1, bgy1, bgx1, bgy2, bgFractions, bgGradient));
        g2d.fillRect(bgx1, bgy1, bgw, bgh);

        // Draw the highlight
        if (!isInStrip) {
            g2d.drawImage(getHighlightImage(), x, y + 2, null);
        }

        // Draw left, top and right border
        if (!isInStrip) {
            g2d.setColor(new Color(0x9e8255));
            g2d.drawLine(x, y + 1, x, b - 1); // left
            if (!isRollover) {
                g2d.setColor(new Color(0xa78e66));
            }
            g2d.drawLine(x + 1, y, r - 1, y); // top
            g2d.drawLine(r, y + 1, r, b - 1); // right
        }

        // Draw the bottom border
        if (!isInStrip) {
            if (!isRollover) {
                g2d.setPaint(new LinearGradientPaint(x + 1, b, r - 1, b, new float[] {0, .5f, 1},
                        new Color[] {new Color(158, 130, 85, 127), new Color(197, 137, 37, 94), new Color(158, 130, 85, 127)}));
            } else {
                g2d.setColor(new Color(0xD4C5AD));
            }
            g2d.drawLine(x + 1, b, r - 1, b);
        }

        // Draw the inner shadow for the top border
        Color[] borderShadow = isRollover ?
                new Color[] {new Color(0xBD8C46), new Color(0xDEA457), new Color(0xF1AA65)} :
                new Color[] {new Color(0xcbb499), new Color(0xead1b2), new Color(0xf8dbb7)};
        Color[] borderShadow2 = isRollover ?
                new Color[] {new Color(0x9E8255), new Color(0xD09C55), new Color(0xE7AE5F)} :
                new Color[] {new Color(0xb29a78), new Color(0xe2ccb2), new Color(0xefd3b0)};
        for (int i = 0; i<borderShadow.length; i++) {
            if (!isInStrip) {
                int yy = y + 1 + i;
                g2d.setColor(borderShadow2[i]);
                g2d.drawLine(x + 1, yy, r - 1, yy);
                g2d.setColor(borderShadow[i]);
                g2d.drawLine(x + 2, yy, r - 2, yy);
            } else {
                if (i == 2) {
                    break;
                }
                int yy = y + i;
                int xx = isFirstInStrip ? x + 1 : x;
                g2d.setColor(borderShadow[i]);
                g2d.drawLine(xx, yy, r - 1, yy);
            }
        }

        // Draw the bottom border highlight
        if (!isInStrip) {
            Color[] bottomHighlight = isRollover ?
                    new Color[] {new Color(0xFFAD3A), new Color(0xFFC574), new Color(0xFFB348)}:
                    new Color[] {new Color(0xFBBF43), new Color(0xFCE2AC), new Color(0xFBC559)};
            g2d.setPaint(new LinearGradientPaint(x + 1, b - 1, r - 1, b - 1, new float[] {0, .5f, 1},
                    bottomHighlight));
            g2d.drawLine(x + 1, b - 1, r - 1, b - 1);
        }

        // Draw left and right border highlight
        if (!isInStrip) {
            if (!isRollover) {
                g2d.setPaint(new LinearGradientPaint(x + 1, y + 4, x + 1, b - 1, new float[] {0, 0.499f, 0.5f, 0.75f, 1},
                        new Color[] {new Color(0xf4d0a6), new Color(0xf1b25a),
                        new Color(0xf1b25a), new Color(0xf6bd5f), new Color(0xfcc044)}));
            } else {
                g2d.setPaint(new LinearGradientPaint(x + 1, y + 4, x + 1, b - 1, new float[] {0, 0.499f, 0.5f, 1},
                        new Color[] {new Color(0xF3B966), new Color(0xFAD187), new Color(0xFACB7B), new Color(0xFEAE38)}));
            }
            g2d.drawLine(x + 1, y + 4, x + 1, b - 1);
            g2d.drawLine(r - 1, y + 4, r - 1, b - 1);
        }

        // Draw bottom left and right border shadows
        if (!isInStrip) {
            if (!isRollover) {
                g2d.setColor(new Color(0xe6b152));
                g2d.drawLine(x + 1, b - 2, x + 1, b - 2);
                g2d.drawLine(r - 1, b - 2, r - 1, b - 2);
            }
            g2d.setColor(new Color(0xc8a368));
            g2d.drawLine(x + 1, b - 1, x + 1, b - 1);
            g2d.drawLine(r - 1, b - 1, r - 1, b - 1);
        }

        // Draw the hightlights in the bottom left and right corners
        if (!isRollover && !isInStrip) {
            int hl = (int) (width * 0.08) - 2;
            g2d.setColor(new Color(251, 197, 89, 153));
            g2d.drawLine(x + 2, b - 2, x + 2 + hl, b - 2);
            g2d.drawLine(r - 2, b - 2, r - 2 - hl, b - 2);
        }
    }

    private void paintBigSelectedBackground(Graphics2D g2d, int x, int y, int width, int height) {
        int r = x + width - 1;
        int b = y + height - 1;

        // Draw the background
        Color[] bgGradient = isRollover ?
                new Color[] {new Color(0xF9BA81), new Color(0xE38B4E), new Color(0xDE752C), new Color(0xF4BC5B)} :
                new Color[] {new Color(0xFFDDB9), new Color(0xFAA85B), new Color(0xF88D29), new Color(0xFDE499)};
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1, new float[] {0, 0.39f, 0.391f, 1},
                bgGradient));
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);

        // Draw the background highlight
        g2d.drawImage(getHighlightImage(), x, y + 2, null);

        // Draw the border
        Color[] borderColors = isRollover ?
                new Color[] {new Color(0x8E8165), new Color(0xB1A389)} :
                new Color[] {new Color(0x8E8165), new Color(0xC6C0B2)};
        g2d.setPaint(new LinearGradientPaint(x, y, x, b-2, new float[] {0.67f, 1},
                borderColors));
        g2d.drawLine(x, y + 1, x, b - 2); // left
        g2d.drawLine(r, y + 1, r, b - 2); // right
        g2d.drawLine(x + 1, y, r - 1, y); // top

        // Draw the bottom border
        if (isRollover) {
            g2d.setColor(new Color(0xD4C5AD));
            g2d.drawLine(x + 1, b, r - 1, b);
        }

        // Draw the top border shadow
        Color[] borderShadow = isRollover ?
                new Color[] {new Color(0xA8885E), new Color(0xD19B65), new Color(0xE9AA6B)} :
                new Color[] {new Color(0xB69A78), new Color(0xDDB889), new Color(0xF5C99A)};
        for (int i = 0; i<borderShadow.length; i++) {
            int yy = y + 1 + i;
            g2d.setColor(borderShadow[i]);
            g2d.drawLine(x + 1, yy, r - 1, yy);
        }

        // Draw the left and right border highlight
        float[] highlightFractions = isRollover ?
                new float[] {0, 0.5f, 1} :
                new float[] {0, 1};
        Color[] highlightColors = isRollover ?
                new Color[] {new Color(239, 161, 49, 204), new Color(0xFEF0D3), new Color(0xFFDF71)} :
                new Color[] {new Color(242, 173, 65, 204), new Color(0xFFCF2D)};
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1, highlightFractions, highlightColors));
        g2d.drawLine(x + 1, y + 1, x + 1, b - 1);
        g2d.drawLine(r - 1, y + 1, r - 1, b - 1);

        // Draw the bottom border highlight
        int bhy = isRollover ? b - 1 : b;
        g2d.setPaint(new LinearGradientPaint(x + 1, b, r - 1, b, new float[] {0, 0.5f, 1},
                new Color[] {new Color(0xFFCF2C), new Color(0xFFE9A0), new Color(0xFFCF2D)}));
        g2d.drawLine(x + 1, bhy, r - 1, bhy);

        // Smoothen the corners
        g2d.setColor(isRollover ? new Color(0x97896D) : new Color(0xB5A791));
        g2d.drawLine(x + 1, y + 1, x + 1, y + 1);
        g2d.drawLine(r - 1, y + 1, r - 1, y + 1);
        g2d.setColor(isRollover ? new Color(0xCCBEA5 ) :new Color(255, 220, 101, 92));
        g2d.drawLine(x, b - 1, x, b - 1);
        g2d.drawLine(r, b - 1, r, b - 1);
        if (isRollover) {
            g2d.setColor(new Color(0xE1C271));
            g2d.drawLine(x + 1, b - 1, x + 1, b - 1);
            g2d.drawLine(r - 1, b - 1, r - 1, b - 1);
        } else {
            g2d.setColor(new Color(0xFDD75D));
            g2d.drawLine(x + 2, b - 1, x + 2, b - 1);
            g2d.drawLine(r - 2, b - 1, r - 2, b - 1);
        }
    }

    /**
     * Paints the background in selected state.
     *
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    protected void paintPressedBackground(Graphics2D g, int x, int y, int width, int height) {
        Rectangle actionRect = getActionClickArea();
        Rectangle popupRect = getPopupClickArea();

        if (commandButton.getDisplayState() == CommandButtonDisplayState.BIG) {
            if (!isSplit()) {
                this.paintBigPressedBackground(g, x, y, width, height);
            } else {
                actionRect.height += 2;
                popupRect.y += 2;
                popupRect.height -= 2;

                g.setClip(actionRect);
                if (isPopupRollover) {
                    this.paintBigRolloverBackground(g, x, y, width, height, true, isActionEnabled);
                } else {
                    this.paintBigPressedBackground(g, x, y, width, height);

                    // Paint a white overlay over the line at the bottom of the action area
                    g.setColor(new Color(0x99ffffff, true));
                    g.drawLine(x+1, actionRect.height-1, x+width-2, actionRect.height-1);
                }

                g.setClip(popupRect);
                if (isPopupRollover) {
                    this.paintBigPressedBackground(g, x, y, width, height);
                } else {
                    this.paintBigRolloverBackground(g, x, y, width, height, true, isPopupEnabled);
                }
            }
        } else {
            if (isInStrip) {
                paintNormalBackground(g, x, y, width, height);
            }

            if (!isSplit()) {
                paintSmallPressedBackground(g, x, y, width, height);
            } else {
                g.setClip(actionRect);
                if (isPopupRollover) {
                    paintSmallRolloverBackground(g, x, y, width, height, true, isActionEnabled);
                } else {
                    paintSmallPressedBackground(g, x, y, width, height);
                }

                g.setClip(popupRect);
                if (isPopupRollover) {
                    paintSmallPressedBackground(g, x, y, width, height);
                } else {
                    paintSmallRolloverBackground(g, x, y, width, height, true, isPopupEnabled);
                }
            }
        }
    }

    private void paintSmallPressedBackground(Graphics2D g2d, int x, int y, int width, int height) {
        int r = x + width - 1; // rightmost coordinate
        int b = y + height - 1; // bottommost coordinate

        // Paint background
        int bx1 = isInStrip && !isFirstInStrip ? x : x + 1;
        int bx2 = r - 1;
        int bw = bx2 - bx1 + 1;
        if (!isInStrip) {
            g2d.setPaint(new LinearGradientPaint(bx1, y + 1, bx1, b - 1, new float[] {0, 0.6f, 0.61f, 1},
                    new Color[] {new Color(0xF4A770), new Color(0xE87E40), new Color(0xDE550A), new Color(0xF58D3F)}));
        } else {
            g2d.setPaint(new LinearGradientPaint(bx1, y + 1, bx1, b - 1, new float[] {0.2f, 0.4f, 0.41f, 1},
                    new Color[] {new Color(0xfcb16d), new Color(0xff9a23), new Color(0xff8d05), new Color(0xffc450)}));
        }
        g2d.fillRect(bx1, y + 1, bw, height - 2);

        // Paint the highlight
        if (!isInStrip) {
            g2d.drawImage(getHighlightImage(), x, y + 2, null);
        }

        // Paint the border
        g2d.setPaint(new Color(0x7B6645));
        if (!isInStrip) {
            g2d.drawLine(x + 1, y, r - 1, y);
            g2d.drawLine(x, y + 1, x, b - 1);
            g2d.drawLine(r, y + 1, r, b - 1);
        } else {
            int x1 = isFirstInStrip ? x + 2 : x;
            int x2 = isLastInStrip  ? r - 2 : r - 1;
            g2d.drawLine(x1, y, x2, y);
            g2d.drawLine(x1, b, x2, b);
        }

        // Paint the top border shadow
        int[] transparency = {153, 84, 22, 13};
        for(int i=0; i<transparency.length; i++) {
            int yy = y + 1 + i;
            int x1 = isFirstInStrip ? x + 1 : x;
            g2d.setColor(new Color(139, 118, 84, transparency[i]));
            g2d.drawLine(x1, yy, r - 1, yy);
        }

        // Paint the border highlight
        if (!isInStrip) {
            int hl = (int) (width * 0.08);
            g2d.setColor(new Color(253, 173, 3, 92));
            g2d.drawLine(x, b - 1, x + hl, b - 1);
            g2d.drawLine(r, b - 1, r - hl, b - 1);
            g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b, new float[] {0, 1},
                    new Color[] {new Color(253, 173, 3, 25), new Color(253, 173, 3)}));
            g2d.drawLine(x + 1, y + 1, x + 1, b);
            g2d.drawLine(r - 1, y + 1, r - 1, b);
            g2d.drawLine(x + 1, b, r - 1, b);
        } else {
            g2d.setColor(new Color(253, 173, 3));
            int x1 = isFirstInStrip ? x + 1 : x;
            g2d.drawLine(x1, b - 1, r - 1, b - 1);
        }

        // Smoothen the corners
        if (!isInStrip) {
            g2d.setColor(new Color(0x8B6F4D));
            g2d.drawLine(x + 1, y + 1, x + 1, y + 1);
            g2d.drawLine(r - 1, y + 1, r - 1, y + 1);
        }
    }

    private void paintBigPressedBackground(Graphics2D g2d, int x, int y, int width, int height) {
        int r = x + width - 1; // rightmost coordinate
        int b = y + height - 1; // bottommost coordinate

        // Paint background
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1, new float[] {0, 0.409f, 0.41f, 1},
                new Color[] {new Color(0xFEB76C), new Color(0xFCA060), new Color(0xFB8A3C), new Color(0xFEBB60)}));
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);

        // Paint the highlight
        g2d.drawImage(getHighlightImage(), x, y + 2, null);

        // Paint the border
        g2d.setPaint(new LinearGradientPaint(x, y, x, b - 2, new float[] {0.5f, 1},
                new Color[] {new Color(0x8B7654), new Color(0xC4B9A8)}));
        g2d.drawLine(x + 1, y, r - 1, y);
        g2d.drawLine(x, y + 1, x, b - 2);
        g2d.drawLine(r, y + 1, r, b - 2);

        // Paint the top border shadow
        int[] transparency = {153, 84, 43, 22, 13};
        for(int i=0; i<transparency.length; i++) {
            int yy = y + 1 + i;
            g2d.setColor(new Color(139, 118, 84, transparency[i]));
            g2d.drawLine(x + 1, yy, r - 1, yy);
        }

        // Paint the left and right border shadows
        g2d.setPaint(new LinearGradientPaint(x, y, x, b - 2, new float[] {0.5f, 1},
                new Color[] {new Color(139, 118, 84, 20), new Color(196, 185, 168, 20)}));
        g2d.drawLine(x + 2, y + 1, x + 2, b - 1);
        g2d.drawLine(r - 2, y + 1, r - 2, b - 1);

        // Paint the border highlights
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b, new float[] {0, 1},
                new Color[] {new Color(253, 173, 17, 25), new Color(253, 173, 17)}));
        g2d.drawLine(x + 1, y + 1, x + 1, b);
        g2d.drawLine(r - 1, y + 1, r - 1, b);
        g2d.drawLine(x + 1, b, r - 1, b);
        g2d.drawLine(x + 2, b - 1, x + 2, b - 1);
        g2d.drawLine(r - 2, b - 1, r - 2, b - 1);

        // Smoothen the corners
        g2d.setColor(new Color(253, 173, 17, 127));
        g2d.drawLine(x, b - 1, x, b - 1);
        g2d.drawLine(r, b - 1, r, b - 1);
        g2d.setColor(new Color(0xAC9674));
        g2d.drawLine(x + 1, y + 1, x + 1, y + 1);
        g2d.drawLine(r - 1, y + 1, r - 1, y + 1);
    }

    /**
     * Paints the ribbon button background.
     *
     * @param g the graphics context
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    protected void paintRolloverBackground(Graphics2D g, int x, int y, int width, int height) {
        Rectangle actionRect = getActionClickArea();
        Rectangle popupRect = getPopupClickArea();
        
        if (CommandButtonDisplayState.BIG == commandButton.getDisplayState()) {
            if (!isSplit()) {
                paintBigRolloverBackground(g, x, y, width, height, false, isActionEnabled || isPopupOnly && isPopupEnabled);
            } else {
                // Increase the height of the action rect if the popup area is rollover (to comply with Office 2007)
                if (isPopupRollover) {
                    actionRect.height += 2;
                    popupRect.y += 2;
                    popupRect.height -= 2;
                }

                // Paint the action area
                g.setClip(actionRect);
                paintBigRolloverBackground(g, x, y, width, height, isPopupRollover, isActionEnabled);

                // Paint the popup area
                g.setClip(popupRect);
                paintBigRolloverBackground(g, x, y, width, height, !isPopupRollover, isPopupEnabled);
            }
        } else {
            if (isInStrip) {
                paintNormalBackground(g, x, y, width, height);
                // TODO take into account if the strip is vertical
//                if (!isSplit()) {
//                    Rectangle rect = isPopupOnly ? popupRect : actionRect;
//                    rect.x += isFirstInStrip ? 1 : 0;
//                    rect.y += 1;
//                    rect.width -= isFirstInStrip ? 2 : 1;
//                    rect.height -= isFirstInStrip ? 2 : 1;
//                    g.setClip(rect);
//                } else {
//                    actionRect.x += isFirstInStrip ? 1 : 0;
//                    actionRect.y += 1;
//                    actionRect.width -= isFirstInStrip ? 1 : 0;
//                    actionRect.height -= 2;
//                    popupRect.y += 1;
//                    popupRect.width -= 1;
//                    popupRect.height -= 2;
//                }
            }

            if (!isSplit()) {
                paintSmallRolloverBackground(g, x, y, width, height, false, isActionEnabled || isPopupOnly && isPopupEnabled);
            } else {
                // Paint the action area
                g.setClip(actionRect);
                paintSmallRolloverBackground(g, x, y, width, height, isPopupRollover, isActionEnabled);

                // Paint the popup area
                g.setClip(popupRect);
                paintSmallRolloverBackground(g, x, y, width, height, !isPopupRollover, isPopupEnabled);
            }
        }
    }

    protected void paintBigRolloverBackground(Graphics2D g, int x, int y, int width, int height, boolean overlay, boolean paintAsEnabled) {
        Graphics2D g2d = g;
        BufferedImage img = null;
        if (!paintAsEnabled) {
            img = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d = img.createGraphics();
            g2d.setClip(g.getClip());
        }

        int r = x + width - 1;
        int b = y + height - 1;

        // Draw the background
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1, RolloverColors.BG_FRACTIONS_BIG, RolloverColors.BG));
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);

        // Draw the background highlight
        g2d.drawImage(getHighlightImage(), x, y + 2, null);

        if (overlay && paintAsEnabled) {
            g2d.setPaint(new Color(0x99ffffff, true));
            g2d.fillRect(x + 1, y + 1, width - 2, height - 2);
        }

        // Draw the border
        g2d.setPaint(new LinearGradientPaint(x, y, x, b, RolloverColors.BORDER_FRACTIONS, RolloverColors.BORDER_BIG));
        g2d.drawLine(x + 1, y, r - 1, y); // top
        g2d.drawLine(r, y + 1, r, b - 1); // right
        g2d.drawLine(x + 1, b, r - 1, b); // bottom
        g2d.drawLine(x, y + 1, x, b - 1); // left

        // Draw the border highlight
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1,
                RolloverColors.HIGHLIGHT1_FRACTIONS, RolloverColors.HIGHLIGHT1));
        g2d.drawLine(x + 2, y + 1, r - 2, y + 1); // top
        g2d.drawLine(x + 1, y + 2, x + 1, b - 2); // left;
        g2d.drawLine(r - 1, y + 2, r - 1, b - 2); // right;
        g2d.setPaint(new LinearGradientPaint(x + 2, b - 1, r - 2, b - 1,
                RolloverColors.HIGHLIGHT2_FRACTIONS, RolloverColors.HIGHLIGHT2));
        g2d.drawLine(x + 2, b - 1, r - 2, b - 1); // bottom

        // Draw the yellow highlight
        // Sides
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1,
                RolloverColors.HIGHLIGHT3_FRACTIONS, RolloverColors.HIGHLIGHT3));
        g2d.drawLine(x + 1, y + 1, x + 1, b - 1);
        g2d.drawLine(r - 1, y + 1, r - 1, b - 1);

        // Bottom
        g2d.setPaint(new LinearGradientPaint(x + 1, b - 1, r - 1, b - 1,
                RolloverColors.HIGHLIGHT4_FRACTIONS, RolloverColors.HIGHLIGHT4));
        g2d.drawLine(x + 1, b - 1, r - 1, b - 1);

        // Smoothen the corners
        g2d.setColor(RolloverColors.CORNER1);
        g2d.drawLine(x + 1, y + 1, x + 1, y + 1);
        g2d.drawLine(r - 1, y + 1, r - 1, y + 1);
        g2d.setColor(RolloverColors.CORNER2);
        g2d.drawLine(x + 1, b - 1, x + 1, b - 1);
        g2d.drawLine(r - 1, b - 1, r - 1, b - 1);

        if (!paintAsEnabled) {
            g2d.dispose();
            ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(colorSpace, null);
            g.drawImage(img, op, x, y);
        }
    }

    protected void paintSmallRolloverBackground(Graphics2D g, int x, int y, int width, int height, boolean overlay, boolean paintAsEnabled) {
        Graphics2D g2d = g;
        BufferedImage img = null;
        if (!paintAsEnabled) {
            img = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d = img.createGraphics();
            g2d.setClip(g.getClip());
        }

        int r = x + width - 1;
        int b = y + height - 1;

        // Draw the background
        // TODO take into account if the strip is vertical
        int x1 = isInStrip && !isFirstInStrip ? x : x + 1;
        int x2 = r - 1;
        int y1 = y + 1;
        int y2 = b - 1;
        int bgw = x2 - x1 + 1;
        int bgh = y2 - y1 + 1;
        g2d.setPaint(new LinearGradientPaint(x1, y1, x1, y2, RolloverColors.BG_FRACTIONS_SMALL, RolloverColors.BG));
        g2d.fillRect(x1, y1, bgw, bgh);

        // Draw the background highlight
        Shape oldClip = g2d.getClip();
        g2d.setClip(x1, y1, bgw, bgh);
        g2d.drawImage(getHighlightImage(), x, y + 2, null);
        g2d.setClip(oldClip);

        // Dim the background (when the button is split)
        if (overlay && paintAsEnabled) {
            g2d.setPaint(new Color(0x99ffffff, true));
            g2d.fillRect(x1, y1, bgw, bgh);
        }

        // Draw the border
        if (!isInStrip) {
            g2d.setPaint(new LinearGradientPaint(x, y, x, b, RolloverColors.BORDER_FRACTIONS, RolloverColors.BORDER_SMALL));
            g2d.drawLine(x + 1, y, r - 1, y); // top
            g2d.drawLine(r, y + 1, r, b - 1); // right
            g2d.drawLine(x + 1, b, r - 1, b); // bottom
            g2d.drawLine(x, y + 1, x, b - 1); // left
        }

        // Draw the border highlight
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, b - 1,
                RolloverColors.HIGHLIGHT1_FRACTIONS, RolloverColors.HIGHLIGHT1));
        g2d.drawLine(x1, y1, x2, y1); // top
        g2d.drawLine(x1, y1 + 1, x1, y2 - 1); // left;
        g2d.drawLine(x2, y1 + 1, x2, y2 - 1); // right;
        g2d.setPaint(new LinearGradientPaint(x + 2, b - 1, r - 2, b - 1,
                RolloverColors.HIGHLIGHT2_FRACTIONS, RolloverColors.HIGHLIGHT2));
        g2d.drawLine(x1, y2, x2, y2); // bottom

        // Smoothen the corners
        if (isInStrip) {
            // TODO take into account if the strip is vertical
            setAlpha(g2d, 0.5f);
            g2d.setColor(NORMAL_BORDER_COLOR);
            if (isFirstInStrip) {
                g2d.drawLine(x1, y1, x1, y1);
                g2d.drawLine(x1, y2, x1, y2);
            }
            if (isLastInStrip) {
                g2d.drawLine(x2, y1, x2, y1);
                g2d.drawLine(x2, y2, x2, y2);
            }
            resetAlpha(g2d);
        } else {
            g2d.setColor(RolloverColors.CORNER1);
            g2d.drawLine(x + 1, y + 1, x + 1, y + 1);
            g2d.drawLine(r - 1, y + 1, r - 1, y + 1);
            g2d.setColor(RolloverColors.CORNER2);
            g2d.drawLine(x + 1, b - 1, x + 1, b - 1);
            g2d.drawLine(r - 1, b - 1, r - 1, b - 1);
        }

        if (!paintAsEnabled) {
            g2d.dispose();
            ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(colorSpace, null);
            g.drawImage(img, op, x, y);
        }
    }

    protected boolean isSplit() {
        JCommandButton.CommandButtonKind kind = commandButton instanceof JCommandButton ?
                ((JCommandButton) commandButton).getCommandButtonKind() :
                JCommandButton.CommandButtonKind.ACTION_ONLY;
        return kind.hasAction() && kind.hasPopup();
    }

    protected BufferedImage getHighlightImage() {
        if (highlightImage == null || highlightImage.getWidth() != commandButton.getWidth()) {
            highlightImage = HighlightFactory.createHighlight(commandButton.getWidth(), (int) (commandButton.getHeight() * 1.5));
        }
        return highlightImage;
    }

    public void paintCollapsedBandButtonBackground(Graphics g, Rectangle toFill) {
        updateState();

        Graphics2D g2d = (Graphics2D) g;
        if (isPressed) {
            paintPressedCollapsedBandButtonBackground(g2d, toFill.x, toFill.y, toFill.width, toFill.height);
        } else {
            paintNormalCollapsedBandButtonBackground(g2d, toFill.x, toFill.y, toFill.width, toFill.height);
        }
    }

    protected void paintPressedCollapsedBandButtonBackground(Graphics2D g2d, int x, int y, int width, int height) {
        Object oldAa = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int right = x + width - 1;
        int bottom = y + height - 1;

        // Draw the background
        Color[] colors = new Color[] {new Color(0xf3f3f3), new Color(0xc8cdd4), new Color(0xb9c0c9), new Color(0xececec)};
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, bottom-1, new float[] {0, 0.17f, 0.171f, 1f},
                colors));
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g2d.setColor(new Color(0xaaaaaa));
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);
        g2d.setComposite(old);

        // Draw the shadows
        g2d.setColor(CollapsedBandColors.border1_pressed_140);
        g2d.drawLine(x + 1, y + 1, right - 1, y + 1); // top side
        g2d.setColor(CollapsedBandColors.border1_pressed_90);
        g2d.drawLine(x + 1, y + 1, x + 1, bottom - 1); // left side
        g2d.drawLine(x + 1, y + 2, right - 1, y + 2); // top side
        g2d.setColor(CollapsedBandColors.border1_pressed_45);
        g2d.drawLine(x + 2, y + 1, x + 2, bottom - 1); // left side
        g2d.drawLine(x + 1, y + 3, right - 1, y + 3); // top side

        // Draw the highlight
        g2d.setColor(CollapsedBandColors.highlight1_pressed);
        g2d.drawLine(right - 1, y + 1, right - 1, bottom - 1); // right side
        g2d.drawLine(x + 1, bottom - 1, right - 1, bottom - 1); // bottom side
        g2d.setColor(CollapsedBandColors.highlight2_pressed);
        g2d.drawLine(right - 2, y + 1, right - 2, bottom - 1); // right side
        g2d.drawLine(x + 1, bottom - 2, right - 1, bottom - 2); // bottom side

        // Draw the border
        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(new LinearGradientPaint(x, y, x, bottom, new float[] {0, 0.95f, 1},
                new Color[] {CollapsedBandColors.border1_pressed, CollapsedBandColors.border2_pressed, CollapsedBandColors.border3_pressed}));
        drawRoundedRect(g2d, x, y, right, bottom);
        g2d.setPaint(oldPaint);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAa);
    }

    protected void paintNormalCollapsedBandButtonBackground(Graphics2D g2d, int x, int y, int width, int height) {
        Object oldAa = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int right = x + width - 1;
        int bottom = y + height - 1;

        Color border1 = isRollover ? CollapsedBandColors.border1_over : CollapsedBandColors.border1;
        Color border2 = isRollover ? CollapsedBandColors.border1_over : CollapsedBandColors.border1;

        // Draw the background
        Color[] colors = isRollover ?
                new Color[] {new Color(0xffffff), new Color(0xd0d4d9), new Color(0xc0c7cf), new Color(0xf0f0f0)} :
                new Color[] {new Color(0xf3f3f3), new Color(0xc8cdd4), new Color(0xb9c0c9), new Color(0xececec)};
        g2d.setPaint(new LinearGradientPaint(x + 1, y + 1, x + 1, bottom-1, new float[] {0, 0.16f, 0.161f, 1f},
                colors));
        g2d.fillRect(x + 1, y + 1, width - 2, height - 2);

        // Draw the border
        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(new LinearGradientPaint(x, y, x, bottom, new float[] {0, 1}, new Color[] {border1, border2}));
        drawRoundedRect(g2d, x, y, right, bottom);
        g2d.setPaint(oldPaint);

        // Draw the highlight
        g2d.setColor(CollapsedBandColors.highlight1);
        g2d.drawLine(x + 2, y + 1, right - 2, y + 1); // top side
        g2d.drawLine(x + 1, y + 2, x + 1, bottom - 2); // left side
        if (!isRollover) {
            g2d.setColor(CollapsedBandColors.highlight2);
        }
        g2d.drawLine(right - 1, y + 2, right - 1, bottom - 2); // right side
        g2d.drawLine(x + 2, bottom - 1, right - 2, bottom - 1); // bottom side

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAa);
    }

    protected void drawRoundedRect(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1 + 2, y1, x2 - 2, y1); // top side
        g.drawLine(x1, y1 + 2, x1, y2 - 2); // left side
        g.drawLine(x1 + 2, y2, x2 - 2, y2); // bottom side
        g.drawLine(x2, y1 + 2, x2, y2 - 2); // right side
        g.drawLine(x1, y1 + 2, x1 + 2, y1); // top left corner
        g.drawLine(x2 - 2, y1, x2, y1 + 2); // top right corner
        g.drawLine(x2, y2 - 2, x2 - 2, y2); // bottom right corner
        g.drawLine(x1, y2 - 2, x1 + 2, y2); // bottom left corner
    }

    Object oldAa;
    protected void enableAA(Graphics2D g2d) {
        oldAa = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    protected void resetAA(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAa);
    }

    Composite oldComposite;
    protected void setAlpha(Graphics2D g2d, float alpha) {
        oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    protected void resetAlpha(Graphics2D g2d) {
        g2d.setComposite(oldComposite);
    }
}
