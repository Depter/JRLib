package org.jreserve.opaquenimbus;

import java.awt.*;
import javax.swing.*;

/**
 */
class ToolBarEastState extends State {
    ToolBarEastState() {
        super("East");
    }

    @Override protected boolean isInState(JComponent c) {

        JToolBar toolbar = (JToolBar)c;
        return OpaqueNimbusLookAndFeel.resolveToolbarConstraint(toolbar) == BorderLayout.EAST;
               
    }
}

