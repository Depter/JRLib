package org.officelaf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonGalleryUI;

public class OfficeRibbonGalleryUI extends BasicRibbonGalleryUI {
    public static ComponentUI createUI(JComponent c) {
        return new OfficeRibbonGalleryUI();
    }
}