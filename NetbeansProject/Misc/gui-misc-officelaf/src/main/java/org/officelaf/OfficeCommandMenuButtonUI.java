package org.officelaf;

import org.officelaf.util.CommandButtonPainter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.internal.ui.common.BasicCommandMenuButtonUI;

public class OfficeCommandMenuButtonUI extends BasicCommandMenuButtonUI {
    private CommandButtonPainter painter;

    public static ComponentUI createUI(JComponent c) {
        return new OfficeCommandMenuButtonUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        this.painter = new CommandButtonPainter(commandButton);
    }

    @Override
    protected void paintButtonBackground(Graphics g, Rectangle toFill) {
        painter.paintBackground(g, toFill);
    }

    @Override
    protected void paintButtonHorizontalSeparator(Graphics g, Rectangle rect) {
        g.setColor(OfficeCommandButtonUI.SEPARATOR_COLOR);
        g.drawLine(1, rect.y, commandButton.getBounds().width-2, rect.y);
    }

//    @Override
//    protected void paintButtonVerticalSeparator(Graphics g, int separatorX) {
//        g.setColor(OfficeCommandButtonUI.SEPARATOR_COLOR);
//        g.drawLine(separatorX, 1, separatorX, commandButton.getBounds().height-2);
//    }

    @Override
    protected ResizableIcon createPopupActionIcon() {
        return new PopupArrowIcon(((JCommandButton)this.commandButton).getPopupOrientationKind());
    }

    @Override
    protected int getLayoutGap() {
        return 2;
    }

    @Override
    protected Color getForegroundColor(boolean isTextPaintedEnabled) {
        return isTextPaintedEnabled ? CommandButtonPainter.TEXT_COLOR : CommandButtonPainter.DISABLED_TEXT_COLOR;
    }

    public static class PopupArrowIcon implements ResizableIcon {
        private static final Color C1 = new Color(0x4e4e4f);
        private static final Color C2 = new Color(0x282828);
        private static final int ICON_WIDTH = 7;
        private static final int ICON_HEIGHT = 4;

        private final JCommandButton.CommandButtonPopupOrientationKind orientationKind;

        public PopupArrowIcon(JCommandButton.CommandButtonPopupOrientationKind orientationKind) {
            this.orientationKind = orientationKind;
        }

        @Override
        public void setDimension(Dimension newDimension) {
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            if (orientationKind == JCommandButton.CommandButtonPopupOrientationKind.DOWNWARD) {
                g2d.setPaint(new LinearGradientPaint(0, 0, 0, ICON_HEIGHT-1, new float[] {0, 1}, new Color[] {
                        C1, C2
                }));
                g.drawLine(x, y, x+6, y);
                g.drawLine(x+1, y+1, x+5, y+1);
                g.drawLine(x+2, y+2, x+4, y+2);
                g.drawLine(x+3, y+3, x+3, y+3);
            } else {
                g2d.setPaint(new LinearGradientPaint(0, 0, ICON_WIDTH-1, 0, new float[] {0, 1}, new Color[] {
                        C1, C2
                }));
                g.drawLine(x, y, x, y+6);
                g.drawLine(x+1, y+1, x+1, y+5);
                g.drawLine(x+2, y+2, x+2, y+4);
                g.drawLine(x+3, y+3, x+3, y+3);
            }
        }

        @Override
        public int getIconWidth() {
            return orientationKind == JCommandButton.CommandButtonPopupOrientationKind.DOWNWARD ?
                    ICON_WIDTH :
                    ICON_HEIGHT;
        }

        @Override
        public int getIconHeight() {
            return orientationKind == JCommandButton.CommandButtonPopupOrientationKind.DOWNWARD ?
                    ICON_HEIGHT :
                    ICON_WIDTH;
        }
    }

}
