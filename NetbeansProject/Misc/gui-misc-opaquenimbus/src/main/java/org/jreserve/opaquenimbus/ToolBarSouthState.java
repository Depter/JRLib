package org.jreserve.opaquenimbus;

import java.awt.*;
import javax.swing.*;

/**
 */
class ToolBarSouthState extends State {
    ToolBarSouthState() {
        super("South");
    }

    @Override protected boolean isInState(JComponent c) {

        JToolBar toolbar = (JToolBar)c;
        return OpaqueNimbusLookAndFeel.resolveToolbarConstraint(toolbar) == BorderLayout.SOUTH;
               
    }
}

