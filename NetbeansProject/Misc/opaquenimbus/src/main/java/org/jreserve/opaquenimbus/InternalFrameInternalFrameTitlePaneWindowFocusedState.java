package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class InternalFrameInternalFrameTitlePaneWindowFocusedState extends State {
    InternalFrameInternalFrameTitlePaneWindowFocusedState() {
        super("WindowFocused");
    }

    @Override protected boolean isInState(JComponent c) {

                         return c instanceof JInternalFrame && ((JInternalFrame)c).isSelected();
    }
}

