package org.officelaf;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import org.netbeans.swing.tabcontrol.TabDisplayer;
import org.netbeans.swing.tabcontrol.plaf.AbstractTabCellRenderer;
import org.netbeans.swing.tabcontrol.plaf.TabControlButtonFactory;
import org.netbeans.swing.tabcontrol.plaf.TabPainter;

public class OfficeEditorTabCellRenderer2 extends AbstractTabCellRenderer {
    private static final TabPainter noClipPainter = new NoClipPainter();
    private static final TabPainter leftClipPainter = new ClipPainter(SwingConstants.LEFT);
    private static final TabPainter rightClipPainter = new ClipPainter(SwingConstants.RIGHT);

    private static final Color WHITE_0 = new Color(1f, 1f, 1f, 0f);

    public OfficeEditorTabCellRenderer2() {
        super(leftClipPainter, noClipPainter, rightClipPainter, new Dimension(32, 42));
    }

    @Override
    public Dimension getPadding() {
        Dimension d = super.getPadding();
        d.width = isShowCloseButton() && !Boolean.getBoolean("nb.tabs.suppressCloseButton") ? 32 : 16;
        return d;
    }

    @Override
    protected int getCaptionYAdjustment() {
        return isSelected() ? 0 : 2;
    }

    @Override
    protected int getIconYAdjustment() {
        return isSelected() ? -2 : 0;
    }

    @Override
    public Color getSelectedActivatedForeground() {
        return getTxtColor();
    }

    @Override
    public Color getSelectedForeground() {
        return getTxtColor();
    }

    private static Color getTxtColor() {
        Color result = UIManager.getColor("TabbedPane.foreground"); //NOI18N
        if (result == null) {
            result = new Color(0, 0, 0);
        }
        return result;
    }

    private static String findIconPath( OfficeEditorTabCellRenderer2 renderer ) {
        if (renderer.inCloseButton() && renderer.isPressed()) {
            return "org/netbeans/swing/tabcontrol/resources/xp_bigclose_pressed.png"; // NOI18N
        }
        if(renderer.inCloseButton()) {
            return "org/netbeans/swing/tabcontrol/resources/xp_bigclose_rollover.png"; // NOI18N
        }
        return "org/netbeans/swing/tabcontrol/resources/xp_bigclose_enabled.png"; // NOI18N
    }

    private static void paintInterior(Graphics g, Component c, Polygon interiorPolygon, Rectangle closeButtonRect,
                                      String closeButtonIconPath) {
        OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;
        Rectangle rect = interiorPolygon.getBounds();
        Graphics2D g2d = (Graphics2D) g;

        if (ren.isSelected()) {
            g2d.setColor(Color.WHITE);
        } else {
            Color[] gradient = ren.isArmed() ?
                    new Color[] {
                            OfficeViewTabDisplayerUIOld.BG1_FOCUSED_COLOR,
                            OfficeViewTabDisplayerUIOld.BG2_FOCUSED_COLOR,
                            OfficeViewTabDisplayerUIOld.BG3_FOCUSED_COLOR,
                            OfficeViewTabDisplayerUIOld.BG4_FOCUSED_COLOR
                    } :
                    new Color[] {
                            OfficeViewTabDisplayerUIOld.BG1_COLOR,
                            OfficeViewTabDisplayerUIOld.BG2_COLOR,
                            OfficeViewTabDisplayerUIOld.BG3_COLOR,
                            OfficeViewTabDisplayerUIOld.BG4_COLOR
                    };
            g2d.setPaint(new LinearGradientPaint(rect.x, rect.y, rect.x, rect.y+rect.height-1,
                    new float[] {0, 0.49f, 0.5f, 1}, gradient));
        }
        g2d.fillRect(rect.x, rect.y, rect.width, rect.height);

        if (g2d.hitClip(closeButtonRect.x, closeButtonRect.y, closeButtonRect.width, closeButtonRect.height)) {
            //paint close button
            Icon icon = TabControlButtonFactory.getIcon(closeButtonIconPath);
            if (!ren.isSelected() && !ren.isArmed()) {
                g2d = (Graphics2D) g2d.create();
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            }
            icon.paintIcon(ren, g2d, closeButtonRect.x, closeButtonRect.y);
            if (!ren.isSelected() && !ren.isArmed()) {
                g2d.dispose();
            }
        }
    }

    private static BufferedImage createFadeOutMask(int width, int height, int fadeSize, int direction) {
        BufferedImage gradient = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = gradient.createGraphics();
        float x1, x2, y1, y2; // (x1, y1) is the opaque part of the graident and (x2, y2) is the transparent part.
        boolean solid = false;
        switch (direction) {
            case SwingConstants.LEFT:
                x1 = 0;
                x2 = fadeSize;
                y1 = 0;
                y2 = 0;
                break;
            case SwingConstants.RIGHT:
                x1 = width - 1;
                x2 = width - 1 - fadeSize;
                y1 = 0;
                y2 = 0;
                break;
            default:
                x1 = x2 = y1 = y2 = 0;
                solid = true;
        }
        if (!solid) {
            GradientPaint paint = new GradientPaint(x1, y1, Color.WHITE, x2, y2, WHITE_0);
            g2d.setPaint(paint);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        gradient.flush();
        return gradient;
    }

    private static void applyAlphaMask(BufferedImage mask, BufferedImage buffer) {
        Graphics2D g2d = buffer.createGraphics();
        g2d.setComposite(AlphaComposite.DstOut);
        g2d.drawImage(mask, null, 0, 0);
        g2d.dispose();
    }

    private static class NoClipPainter implements TabPainter {
        private static final int closeIconRightInset = 2;

        @Override
        public Polygon getInteriorPolygon(Component c) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;

            Insets ins = getBorderInsets(c);
            Polygon p = new Polygon();
            int x = 0;
            int y = 0;

            int width = c.getWidth(); //ren.isRightmost() ? c.getWidth() - 1 : c.getWidth();
            int height = c.getHeight() - ins.bottom;

            if (!ren.isSelected()) {
                height -= 2;
                y += 2;
            } else {
                height += 1;
            }

            //just a plain rectangle
            p.addPoint(x, y + ins.top);
            p.addPoint(x + width, y + ins.top);
            p.addPoint(x + width, y + height - 1);
            p.addPoint(x, y + height - 1);
            return p;
        }

        @Override
        public void paintInterior(Graphics g, Component c) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;
            Rectangle closeButtonRect = new Rectangle();
            getCloseButtonRectangle(ren, closeButtonRect, new Rectangle(0, 0,
                                                          c.getWidth(),
                                                          c.getHeight()));
            OfficeEditorTabCellRenderer2.paintInterior(g, ren, getInteriorPolygon(ren), closeButtonRect, findIconPath(ren));
        }

        @Override
        public void getCloseButtonRectangle(JComponent jc, Rectangle rect, Rectangle bounds) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) jc;

            if (!ren.isShowCloseButton()) {
                rect.x = -100;
                rect.y = -100;
                rect.width = 0;
                rect.height = 0;
                return;
            }
            String iconPath = findIconPath(ren);
            Icon icon = TabControlButtonFactory.getIcon(iconPath);
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            rect.x = bounds.x + bounds.width - iconWidth - closeIconRightInset;
            rect.y = bounds.y + (Math.max(0, bounds.height / 2 - iconHeight / 2));
            rect.width = iconWidth;
            rect.height = iconHeight;

            if (!ren.isSelected()) {
                rect.y += 2;
            }
        }

        @Override
        public boolean supportsCloseButton(JComponent renderer) {
            return !(renderer instanceof TabDisplayer) || ((TabDisplayer) renderer).isShowCloseButton();
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;
            int yy = ren.isSelected() ? y : y + 2;
            int h = ren.isSelected() ? height : height-2;

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(x, yy);

            boolean isHighlighed = !ren.isSelected() && ren.isArmed();
            g2d.setColor(isHighlighed ?
                    OfficeViewTabDisplayerUIOld.BORDER_FOCUSED_COLOR :
                    OfficeViewTabDisplayerUIOld.BORDER_COLOR);

            // left side
            if (!ren.isPreviousTabSelected()) {
                g2d.drawLine(0, 0, 0, h-1);
            }

            // top side
            g2d.drawLine(0, 0, width-1, 0);

            // right side
            if (!ren.isNextTabSelected()) {
                g2d.drawLine(width-1, 0, width-1, h-1);
            }

            // bottom side
            if (!ren.isSelected()) {
                g2d.setColor(OfficeViewTabDisplayerUIOld.BORDER_COLOR);
                g2d.drawLine(0, h-1, width-1, h-1);
            }
            
            // border highlight
            if (!ren.isSelected()) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f));
                g2d.setColor(OfficeViewTabDisplayerUIOld.BORDER_HIGHLIGHT_COLOR);
                if (!ren.isPreviousTabSelected()) {
                    g2d.drawLine(1, h-2, 1, 1);
                }

                g2d.drawLine(2, 1, width-2, 1);

                if (!ren.isNextTabSelected()) {
                    g2d.drawLine(width-2, 2, width-2, h-2);
                }
            }

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;
            Rectangle rect = new Rectangle();
            getCloseButtonRectangle(ren, rect, new Rectangle(0, 0, ren.getWidth(), ren.getHeight()));
            return new Insets(0, 3, 0, rect.width > 0 ? rect.width + closeIconRightInset : 0);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }

    private static class ClipPainter implements TabPainter {
        private final NoClipPainter delegate = new NoClipPainter();
        private final int direction;

        private ClipPainter(int direction) {
            this.direction = direction;
        }

        @Override
        public Polygon getInteriorPolygon(Component c) {
            return delegate.getInteriorPolygon(c);
        }

        @Override
        public void paintInterior(Graphics g, Component c) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;
            Rectangle closeButtonRect = new Rectangle();
            getCloseButtonRectangle(ren, closeButtonRect, new Rectangle(0, 0,
                                                          c.getWidth(),
                                                          c.getHeight()));

            if (ren.isSelected()) {
                // paint the tab with no fade out
                OfficeEditorTabCellRenderer2.paintInterior(g, ren, getInteriorPolygon(ren), closeButtonRect, findIconPath(ren));
            } else {
                // Paint the tab and fade out the edge
                BufferedImage buffer = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D bg = buffer.createGraphics();
                OfficeEditorTabCellRenderer2.paintInterior(bg, ren, getInteriorPolygon(ren), closeButtonRect, findIconPath(ren));
                bg.dispose();

                BufferedImage mask = createFadeOutMask(c.getWidth(), c.getHeight(), c.getWidth()/2, direction);
                applyAlphaMask(mask, buffer);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(buffer, null, 0, 0);
            }
        }

        @Override
        public void getCloseButtonRectangle(JComponent jc, Rectangle rect, Rectangle bounds) {
            if (direction == SwingConstants.RIGHT) {
                rect.x = -100;
                rect.y = -100;
                rect.width = 0;
                rect.height = 0;
            } else {
                delegate.getCloseButtonRectangle(jc, rect, bounds);
            }
        }

        @Override
        public boolean supportsCloseButton(JComponent component) {
            return direction != SwingConstants.RIGHT && delegate.supportsCloseButton(component);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            OfficeEditorTabCellRenderer2 ren = (OfficeEditorTabCellRenderer2) c;
            if (ren.isSelected()) {
                delegate.paintBorder(c, g, x, y, width, height);
            } else {
                BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D bg = buffer.createGraphics();
                delegate.paintBorder(c, bg, x, y, width, height);
                bg.dispose();

                BufferedImage mask = createFadeOutMask(width, height-1, width/2, direction);
                applyAlphaMask(mask, buffer);

                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(buffer, null, 0, 0);
            }
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return delegate.getBorderInsets(c);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
