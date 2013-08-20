package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class ProgressBarFinishedState extends State {
    ProgressBarFinishedState() {
        super("Finished");
    }

    @Override protected boolean isInState(JComponent c) {

                        return ((JProgressBar)c).getPercentComplete() == 1.0;
                
    }
}

