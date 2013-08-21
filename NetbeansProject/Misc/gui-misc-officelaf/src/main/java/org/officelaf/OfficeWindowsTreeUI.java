package org.officelaf;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class OfficeWindowsTreeUI extends WindowsTreeUI {
    public static ComponentUI createUI(JComponent c) {
        return new OfficeWindowsTreeUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        tree.setInvokesStopCellEditing(true);
    }
}
