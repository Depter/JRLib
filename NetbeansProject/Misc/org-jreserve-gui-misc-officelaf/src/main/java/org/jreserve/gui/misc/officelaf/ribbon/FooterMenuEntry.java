package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;

import javax.swing.*;

public class FooterMenuEntry extends RibbonApplicationMenuEntryFooter {
    public FooterMenuEntry(Action action) {
        super(ActionUtil.lookupIcon(action, false), ActionUtil.lookupText(action), action);
    }
}
