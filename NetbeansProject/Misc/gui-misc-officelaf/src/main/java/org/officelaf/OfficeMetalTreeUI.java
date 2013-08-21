package org.officelaf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTreeUI;

public class OfficeMetalTreeUI extends MetalTreeUI {
    public static ComponentUI createUI(JComponent x) {
        return new OfficeMetalTreeUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        tree.setInvokesStopCellEditing(true);
    }
}
