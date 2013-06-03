package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonGalleryUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class OfficeRibbonGalleryUI extends BasicRibbonGalleryUI {
    public static ComponentUI createUI(JComponent c) {
        return new OfficeRibbonGalleryUI();
    }
}