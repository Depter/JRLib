package org.officelaf;

import org.netbeans.swing.tabcontrol.TabbedContainer;
import org.netbeans.swing.tabcontrol.plaf.DefaultTabbedContainerUI;

public class OfficeTabbedContainerUI extends DefaultTabbedContainerUI {

    public OfficeTabbedContainerUI(TabbedContainer c) {
        super(c);
    }

    protected void install() {
        container.setOpaque(false);
        tabDisplayer.setOpaque(false);
        contentDisplayer.setOpaque(false);
    }
}
