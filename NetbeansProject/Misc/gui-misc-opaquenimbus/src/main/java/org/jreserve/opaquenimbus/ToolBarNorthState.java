package org.jreserve.opaquenimbus;

import java.awt.*;
import javax.swing.*;

/**
 */
class ToolBarNorthState extends State {
    ToolBarNorthState() {
        super("North");
    }

    @Override protected boolean isInState(JComponent c) {

        JToolBar toolbar = (JToolBar)c;
        return OpaqueNimbusLookAndFeel.resolveToolbarConstraint(toolbar) == BorderLayout.NORTH;
               
    }
}

