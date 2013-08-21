package org.jreserve.opaquenimbus;

import java.awt.*;
import javax.swing.*;

/**
 */
class ToolBarWestState extends State {
    ToolBarWestState() {
        super("West");
    }

    @Override protected boolean isInState(JComponent c) {

        JToolBar toolbar = (JToolBar)c;
        return OpaqueNimbusLookAndFeel.resolveToolbarConstraint(toolbar) == BorderLayout.WEST;
               
    }
}

