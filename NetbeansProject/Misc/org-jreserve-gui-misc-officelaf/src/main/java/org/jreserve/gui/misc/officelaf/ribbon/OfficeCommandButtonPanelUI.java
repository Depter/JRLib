package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.internal.ui.common.BasicCommandButtonPanelUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class OfficeCommandButtonPanelUI extends BasicCommandButtonPanelUI {
    public static ComponentUI createUI(JComponent c) {
        return new OfficeCommandButtonPanelUI();
    }

    @Override
    protected Insets getGroupInsets() {
        //return new Insets(0, 0, 0, 0);
        return super.getGroupInsets();
    }
}
