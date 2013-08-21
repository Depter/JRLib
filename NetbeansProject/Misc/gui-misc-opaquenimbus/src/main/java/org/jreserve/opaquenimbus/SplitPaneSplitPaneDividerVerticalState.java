package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class SplitPaneSplitPaneDividerVerticalState extends State {
    SplitPaneSplitPaneDividerVerticalState() {
        super("Vertical");
    }

    @Override protected boolean isInState(JComponent c) {

                        return c instanceof JSplitPane && (((JSplitPane)c).getOrientation() == 1);
    }
}

