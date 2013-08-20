package org.jreserve.opaquenimbus;

import java.awt.*;
import javax.swing.*;

/**
 */
class InternalFrameInternalFrameTitlePaneInternalFrameTitlePaneIconifyButtonWindowNotFocusedState extends State {
    InternalFrameInternalFrameTitlePaneInternalFrameTitlePaneIconifyButtonWindowNotFocusedState() {
        super("WindowNotFocused");
    }

    @Override protected boolean isInState(JComponent c) {

                               Component parent = c;
                               while (parent.getParent() != null) {
                                   if (parent instanceof JInternalFrame) {
                                       break;
                                   }
                                   parent = parent.getParent();
                               }
                               if (parent instanceof JInternalFrame) {
                                   return !(((JInternalFrame)parent).isSelected());
                               }
                               return false;
    }
}

