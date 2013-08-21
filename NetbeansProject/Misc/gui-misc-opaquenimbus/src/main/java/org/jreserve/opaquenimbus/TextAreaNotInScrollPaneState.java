package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class TextAreaNotInScrollPaneState extends State {
    TextAreaNotInScrollPaneState() {
        super("NotInScrollPane");
    }

    @Override protected boolean isInState(JComponent c) {

                          return !(c.getParent() instanceof javax.swing.JViewport);
    }
}

