package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class SplitPaneVerticalState extends State {
    SplitPaneVerticalState() {
        super("Vertical");
    }

    @Override protected boolean isInState(JComponent c) {

                        return c instanceof JSplitPane && (((JSplitPane)c).getOrientation() == 1);
    }
}

