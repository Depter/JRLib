package org.jreserve.gui.misc.officelaf.options;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class OfficeLAFAdvancedOption extends AdvancedOption {

    public String getDisplayName() {
        return NbBundle.getMessage(OfficeLAFAdvancedOption.class, "AdvancedOption_DisplayName_OfficeLAF");
    }

    public String getTooltip() {
        return NbBundle.getMessage(OfficeLAFAdvancedOption.class, "AdvancedOption_Tooltip_OfficeLAF");
    }

    public OptionsPanelController create() {
        return new OfficeLAFOptionsPanelController();
    }
}
