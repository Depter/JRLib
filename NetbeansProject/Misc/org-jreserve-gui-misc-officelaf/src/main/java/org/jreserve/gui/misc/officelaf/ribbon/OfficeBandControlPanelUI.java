package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.internal.ui.ribbon.BasicBandControlPanelUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class OfficeBandControlPanelUI extends BasicBandControlPanelUI {
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent c) {
        return new OfficeBandControlPanelUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        controlPanel.setOpaque(Boolean.FALSE);
    }

    @Override
    protected void paintBandBackground(Graphics graphics, Rectangle toFill) {
        /*Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g2d.setColor(Color.RED);
        g2d.fill(toFill);
        g2d.dispose();*/
    }

    @Override
    public int getLayoutGap() {
        return 0;
    }
}
