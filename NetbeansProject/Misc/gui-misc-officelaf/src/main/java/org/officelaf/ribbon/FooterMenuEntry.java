package org.officelaf.ribbon;

import javax.swing.*;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter;

public class FooterMenuEntry extends RibbonApplicationMenuEntryFooter {
    public FooterMenuEntry(Action action) {
        super(ActionUtil.lookupIcon(action, false), ActionUtil.lookupText(action), action);
    }
}
