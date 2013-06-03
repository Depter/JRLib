package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.common.popup.PopupPanelCallback;
import org.pushingpixels.flamingo.api.ribbon.AbstractRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;
import org.pushingpixels.flamingo.internal.ui.ribbon.AbstractBandControlPanel;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonBandUI;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class OfficeRibbonBandUI extends BasicRibbonBandUI {
    /** The start color of the border gradient. */
    protected static final Color borderColor1 = new Color(0xb6b7b7);

    /** The end color of the border gradient. */
    protected static final Color borderColor2 = new Color(0x818181);

    /** The border highlight color. */
    protected static final Color highlightColor = new Color(255, 255, 255, 127);

    /** The start color of the titlebar background gradient. */
    protected static final Color titleBg1 = new Color(0xb6b8b8);

    /** The end color of the titlebar background gradient. */
    protected static final Color titleBg2 = new Color(0x9c9e9e);

    /** The start color of the titlebar background gradient when in rollover state. */
    protected static final Color titleBg1_over = new Color(0xaaabab);

    /** The end color of the titlebar background gradient. */
    protected static final Color titleBg2_over = new Color(0x6d6e6e);

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent c) {
        return new OfficeRibbonBandUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        ribbonBand.setOpaque(Boolean.FALSE);
        ribbonBand.setForeground(new ColorUIResource(Color.WHITE));
    }

    @Override
    protected void installComponents() {
        super.installComponents();
        ResizableIcon icon = new CollapsedBandIcon(this.ribbonBand.getIcon());
        this.collapsedButton.setUI(new CollapsedRibbonBandButtonUI());
        this.collapsedButton.setIcon(icon);
        this.collapsedButton.setDisabledIcon(icon);
//        this.collapsedButton.setBorder(new CollapsedBandBorder());
    }

   
    @Override
    protected void paintBandBackground(Graphics graphics, Rectangle toFill) {

        if (ribbonBand.getCurrentResizePolicy() instanceof IconRibbonBandResizePolicy) {
            return;
        }

        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = toFill.y;
        int x = toFill.x;
        int width = toFill.width;
        int height = toFill.height;
        int bottom = y + height - 1;

        int x3 = x + width - 2;
        int x2 = x3 - 3;
        int x1 = x + 3;
        int y3 = y + height - 2;
        int y2 = y3 - 3;
        int y1 = y + 3;

        // Paint the background
        if (isUnderMouse) {
            g2d.setColor(highlightColor);
            g2d.fillRect(x + 1, y + 1, width - 3, height - 3);
        }

        // Draw the border
        g2d.setPaint(new LinearGradientPaint(x, y, x, y3, new float[]{0, 1}, new Color[]{borderColor1, borderColor2}));
        g2d.drawLine(x1,   y,    x2,   y);    // top side
        g2d.drawLine(x,    y1,   x,    y2);   // left side
        g2d.drawLine(x1,   y3,   x2,   y3);   // bottom side
        g2d.drawLine(x3,   y1,   x3,   y2);   // right side
        g2d.drawLine(x,    y1-1, x1-1, y);    // top left corner
        g2d.drawLine(x2+1, y,    x3,   y1-1); // top right corner
        g2d.drawLine(x3,   y2+1, x2+1, y3);   // bottom right corner
        g2d.drawLine(x,    y2+1, x1-1, y3);   // bottom left corner

        // Draw the highlight
        g2d.setColor(highlightColor);
        g2d.drawLine(x1,   y+1,  x2+1, y+1);  // top side
        g2d.drawLine(x3+1, y1-1, x3+1, y3-2); // right side
        g2d.drawLine(x+1,  y1,   x+1,  y2+1); // left side
        g2d.drawLine(x+1,  y1-1, x1-1, y+1);  // top left corner
        g2d.drawLine(x3+1, y3-1, x3-1, y3+1); // bottom right corner
        if (isUnderMouse) {
            g2d.drawLine(x1, bottom, x2 + 1, bottom); // bottom side
        } else {
            g2d.drawLine((int) ((x2 + 1) * 0.25), y3 + 1, x2 + 1, y3 + 1); // bottom side (start at 1/4 of the way)
        }

        // Draw the title background
        int titleHeight = getBandTitleHeight();
        Rectangle titleRect = new Rectangle(x+1, y+height-titleHeight, width-3, titleHeight-2);
        Color bg1 = isUnderMouse ? titleBg1_over : titleBg1;
        Color bg2 = isUnderMouse ? titleBg2_over : titleBg2;
        g2d.setPaint(new GradientPaint(titleRect.x, titleRect.y, bg1, titleRect.x, (float) titleRect.getMaxY() - 1, bg2));
        g2d.fillRect(titleRect.x, titleRect.y, titleRect.width, titleRect.height-1);
        g2d.drawLine(titleRect.x+1, (int) titleRect.getMaxY()-1, (int) titleRect.getMaxX()-2, (int) titleRect.getMaxY()-1);


        g2d.dispose();
    }


    // TODO flamingo 4.2 -> 5.0 Check if this works
    boolean isUnderMouse = false;
    @Override
    public void trackMouseCrossing(boolean isMouseIn) {
        super.trackMouseCrossing(isMouseIn);
        isUnderMouse = isMouseIn;
    }

    protected void paintBandTitleBackground(Graphics g, Rectangle titleRectangle, String title) {
    }

    @Override
    protected LayoutManager createLayoutManager() {
        return new RibbonBandLayout();
    }

    // Copied and pasted from BasicRibbonBandUI to fix one issue:
    // When collapsed, the "collapsed button" doesn't get all
    // of the space of the band. In the OfficeLAF UI implementation of
    // the Ribbon, this important since only the button is painted in collapsed state.
    private class RibbonBandLayout implements LayoutManager {

        /*
        * (non-Javadoc)
        *
        * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
        * java.awt.Component)
        */
        public void addLayoutComponent(String name, Component c) {
        }

        /*
        * (non-Javadoc)
        *
        * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
        */
        public void removeLayoutComponent(Component c) {
        }

        /*
        * (non-Javadoc)
        *
        * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
        */
        public Dimension preferredLayoutSize(Container c) {
            Insets ins = c.getInsets();
            AbstractBandControlPanel controlPanel = ribbonBand
                    .getControlPanel();
            boolean useCollapsedButton = (controlPanel == null)
                    || (!controlPanel.isVisible());
            int width = useCollapsedButton ? collapsedButton.getPreferredSize().width
                    : controlPanel.getPreferredSize().width;
            int height = (useCollapsedButton ? collapsedButton
                    .getPreferredSize().height : controlPanel
                    .getPreferredSize().height)
                    + getBandTitleHeight();

            // System.out.println(ribbonBand.getTitle() + ":" + height);

            // Preferred height of the ribbon band is:
            // 1. Insets on top and bottom
            // 2. Preferred height of the control panel
            // 3. Preferred height of the band title panel
            return new Dimension(width + 2 + (!useCollapsedButton ? (ins.left + ins.right) : 0), height
                    + ins.top + ins.bottom);
        }

        /*
        * (non-Javadoc)
        *
        * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
        */
        public Dimension minimumLayoutSize(Container c) {
            Insets ins = c.getInsets();
            AbstractBandControlPanel controlPanel = ribbonBand
                    .getControlPanel();
            boolean useCollapsedButton = (controlPanel == null)
                    || (!controlPanel.isVisible());
            int width = useCollapsedButton ? collapsedButton.getMinimumSize().width
                    : controlPanel.getMinimumSize().width;
            int height = useCollapsedButton ? collapsedButton.getMinimumSize().height
                    + getBandTitleHeight()
                    : controlPanel.getMinimumSize().height
                    + getBandTitleHeight();

            // System.out.println(useCollapsedButton + ":" + height);

            // Preferred height of the ribbon band is:
            // 1. Insets on top and bottom
            // 2. Preferred height of the control panel
            // 3. Preferred height of the band title panel
            return new Dimension(width + 2 + (!useCollapsedButton ? (ins.left + ins.right) : 0), height
                    + ins.top + ins.bottom);

        }

        /*
        * (non-Javadoc)
        *
        * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
        */
        public void layoutContainer(Container c) {
            if (!c.isVisible())
                return;
            Insets ins = c.getInsets();

            int availableHeight = c.getHeight() - ins.top - ins.bottom;
            RibbonBandResizePolicy resizePolicy = ((AbstractRibbonBand) c)
                    .getCurrentResizePolicy();

            if (resizePolicy instanceof IconRibbonBandResizePolicy) {
                collapsedButton.setVisible(true);
                int w = collapsedButton.getPreferredSize().width;
                // System.out.println("Width for collapsed of '"
                // + ribbonBand.getTitle() + "' is " + w);
                collapsedButton.setBounds(0, 0, c.getWidth(), c
                        .getHeight());

                // System.out.println("Collapsed " + collapsedButton.getHeight()
                // + ":" + c.getHeight());

                if (collapsedButton.getPopupCallback() == null) {
                    final AbstractRibbonBand popupBand = ribbonBand.cloneBand();
                    popupBand.setControlPanel(ribbonBand.getControlPanel());
                    java.util.List<RibbonBandResizePolicy> resizePolicies = ribbonBand
                            .getResizePolicies();
                    popupBand.setResizePolicies(resizePolicies);
                    RibbonBandResizePolicy largest = resizePolicies.get(0);
                    popupBand.setCurrentResizePolicy(largest);
                    final Dimension size = new Dimension(4 + largest
                            .getPreferredWidth(availableHeight, 4), ins.top
                            + ins.bottom
                            + Math.max(c.getHeight(),
                            ribbonBand.getControlPanel()
                                    .getPreferredSize().height
                                    + getBandTitleHeight()));
                    // System.out.println(ribbonBand.getTitle() + ":"
                    // + size.getHeight());
                    collapsedButton.setPopupCallback(new PopupPanelCallback() {
                        @Override
                        public JPopupPanel getPopupPanel(
                                JCommandButton commandButton) {
                            return new CollapsedButtonPopupPanel(popupBand,
                                    size);
                        }
                    });
                    // collapsedButton.setGallery(new JPopupGallery(ribbonBand
                    // .getControlPanel(), size));
                    ribbonBand.setControlPanel(null);
                    ribbonBand.setPopupRibbonBand(popupBand);
                }

                if (expandButton != null)
                    expandButton.setBounds(0, 0, 0, 0);

                return;
            }

            if (collapsedButton.isVisible()) {
                // was icon and now is normal band - have to restore the
                // control panel
                CollapsedButtonPopupPanel popupPanel = (collapsedButton
                        .getPopupCallback() != null) ? (CollapsedButtonPopupPanel) collapsedButton
                        .getPopupCallback().getPopupPanel(collapsedButton)
                        : null;
                if (popupPanel != null) {
                    AbstractRibbonBand bandFromPopup = (AbstractRibbonBand) popupPanel
                            .removeComponent();
                    ribbonBand.setControlPanel(bandFromPopup.getControlPanel());
                    ribbonBand.setPopupRibbonBand(null);
                    collapsedButton.setPopupCallback(null);
                }
            }
            collapsedButton.setVisible(false);

            AbstractBandControlPanel controlPanel = ribbonBand
                    .getControlPanel();
            controlPanel.setVisible(true);
            controlPanel.setBounds(ins.left, ins.top, c.getWidth() - ins.left
                    - ins.right, c.getHeight() - getBandTitleHeight() - ins.top
                    - ins.bottom);
            controlPanel.doLayout();

            if (expandButton != null) {
                int ebpw = expandButton.getPreferredSize().width;
                int ebph = expandButton.getPreferredSize().height;
                int maxHeight = getBandTitleHeight() - 4;
                if (ebph > maxHeight)
                    ebph = maxHeight;

                int expandButtonBottomY = c.getHeight()
                        - (getBandTitleHeight() - ebph) / 2;

                expandButton.setBounds(c.getWidth() - ins.right - ebpw,
                        expandButtonBottomY - ebph, ebpw, ebph);
            }
        }
    }

    

    public static class CollapsedRibbonBandButtonUI extends OfficeCommandButtonUI {
        @Override
        protected void paintButtonBackground(Graphics graphics, Rectangle toFill) {
            painter.paintCollapsedBandButtonBackground(graphics, toFill);
        }

        protected boolean isPaintingBackground() {
            return true;
        }
    }
}
