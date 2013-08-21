package org.officelaf.options;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class OfficeLAFAdvancedOption extends AdvancedOption {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(OfficeLAFAdvancedOption.class, "AdvancedOption_DisplayName_OfficeLAF");
    }

    @Override
    public String getTooltip() {
        return NbBundle.getMessage(OfficeLAFAdvancedOption.class, "AdvancedOption_Tooltip_OfficeLAF");
    }

    @Override
    public OptionsPanelController create() {
        return new OfficeLAFOptionsPanelController();
    }
}
