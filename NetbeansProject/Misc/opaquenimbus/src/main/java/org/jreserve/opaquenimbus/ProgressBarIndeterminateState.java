package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class ProgressBarIndeterminateState extends State {
    ProgressBarIndeterminateState() {
        super("Indeterminate");
    }

    @Override protected boolean isInState(JComponent c) {

                        return ((JProgressBar)c).isIndeterminate();
               
    }
}

