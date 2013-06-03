package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

import org.pushingpixels.flamingo.internal.ui.common.BasicCommandButtonUI;
import org.pushingpixels.flamingo.internal.ui.common.BasicCommandButtonListener;

import org.jreserve.gui.misc.officelaf.util.CommandButtonPainter;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 * @author gunnar
 */
public class OfficeCommandButtonUI extends BasicCommandButtonUI {
    static final Color SEPARATOR_COLOR = new Color(0xd4c080);

    protected CommandButtonPainter painter;

    public static ComponentUI createUI(JComponent c) {
        return new OfficeCommandButtonUI();
    }

    protected boolean hasPopup() {
        return this.popupActionIcon != null;
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        painter = new CommandButtonPainter(commandButton);
        commandButton.setBorder(new BorderUIResource.EmptyBorderUIResource(3, 3, 3, 3));
    }

    @Override
    protected BasicCommandButtonListener createButtonListener(AbstractCommandButton b) {
        return new BasicCommandButtonListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    super.mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        };
    }

    @Override
    protected void paintButtonBackground(Graphics graphics, Rectangle toFill) {
        painter.paintBackground(graphics, toFill);
    }

    @Override
    protected void paintButtonHorizontalSeparator(Graphics g, Rectangle rect) {
        g.setColor(SEPARATOR_COLOR);
        g.drawLine(1, rect.y, commandButton.getBounds().width-2, rect.y);
    }

    @Override
    protected void paintButtonVerticalSeparator(Graphics g, Rectangle separatorArea) {
        g.setColor(SEPARATOR_COLOR);
        g.drawLine(separatorArea.x, 1, separatorArea.x, commandButton.getBounds().height-2);
    }

    @Override
    protected ResizableIcon createPopupActionIcon() {
        return new PopupArrowIcon(((JCommandButton)this.commandButton).getPopupOrientationKind());
    }

    @Override
    protected void paintPopupActionIcon(Graphics g, Rectangle rect) {
        boolean popupEnabled = commandButton instanceof JCommandButton
                && ((JCommandButton) commandButton).getPopupModel().isEnabled();
        Graphics2D g2d = (Graphics2D) g.create();
        if (!popupEnabled) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        popupActionIcon.paintIcon(commandButton, g2d, rect.x, rect.y);
        g2d.dispose();
    }

    @Override
    protected int getLayoutGap() {
        return 2;
    }

    @Override
    protected Color getForegroundColor(boolean isTextPaintedEnabled) {
        return isTextPaintedEnabled ? CommandButtonPainter.TEXT_COLOR : CommandButtonPainter.DISABLED_TEXT_COLOR;
    }

    /*@Override
    public Rectangle getActionClickArea() {
        Rectangle retVal = super.getActionClickArea();
        return retVal != null ? retVal : new Rectangle();
    }*/

    public static class PopupArrowIcon implements ResizableIcon {
        private static final Color C = new Color(0x7c7c7c);
        private static final Dimension TRIANGLE = new Dimension(5, 3);

        private final Dimension dim = new Dimension();
        private final JCommandButton.CommandButtonPopupOrientationKind orientationKind;

        public PopupArrowIcon(JCommandButton.CommandButtonPopupOrientationKind orientationKind) {
            this.orientationKind = orientationKind;
        }

        public void setDimension(Dimension newDimension) {
            dim.width = newDimension.width;
            dim.height = newDimension.height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            int xx = dim.width > TRIANGLE.width ? x + dim.width / 2 - TRIANGLE.width / 2 : x;
            int yy = dim.height > TRIANGLE.height ? y + dim.height / 2 - TRIANGLE.height / 2 : y;

            Composite old = g2d.getComposite();
            g2d.translate(0, 1);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.WHITE);
            drawTriangle(g2d, xx, yy);
            g2d.setComposite(old);
            g2d.translate(0, -1);

            g2d.setColor(C);
            drawTriangle(g2d, xx, yy);
        }



        private void drawTriangle(Graphics2D g, int x, int y) {
            if (orientationKind == JCommandButton.CommandButtonPopupOrientationKind.DOWNWARD) {
                g.drawLine(x, y, x+4, y);
                g.drawLine(x+1, y+1, x+3, y+1);
                g.drawLine(x+2, y+2, x+2, y+2);
            } else {
                g.drawLine(x, y, x, y+4);
                g.drawLine(x+1, y+1, x+1, y+3);
                g.drawLine(x+2, y+2, x+2, y+2);
            }
        }

        public int getIconWidth() {
            return dim.width;
        }

        public int getIconHeight() {
            return dim.height;
        }
    }
}
