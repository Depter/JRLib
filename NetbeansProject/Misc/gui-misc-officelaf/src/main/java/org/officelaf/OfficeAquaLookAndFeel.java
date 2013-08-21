package org.officelaf;

import apple.laf.AquaLookAndFeel;

import javax.swing.*;

public class OfficeAquaLookAndFeel extends AquaLookAndFeel {
    private OfficeLookAndFeelHelper helper;

    public OfficeAquaLookAndFeel() {
        helper = new OfficeLookAndFeelHelper();
    }
    
    @Override
    public String getName() {
        return "OfficeAqua";
    }

    @Override
    public String getID() {
        return "OfficeAqua";
    }

    @Override
    public String getDescription() {
        return "The Office look and feel for Exie 10";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        String osName = System.getProperty("os.name");
        return (osName != null) && (osName.toLowerCase().indexOf("mac os x") != -1);
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return isNativeLookAndFeel();
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }

    @Override
    public UIDefaults getDefaults() {
        UIDefaults table = super.getDefaults();

        initClassDefaults(table);
        initSystemColorDefaults(table);
        initComponentDefaults(table);

        OfficeLookAndFeelHelper.installLFCustoms(this, table);

        return table;
    }
    
    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        table.putDefaults(helper.getClassDefaults());
        // TODO create UI class for TreeUI that extends AquaTreeUI (see OfficeMetalTreeUI)
        table.putDefaults(new Object[] {"TreeUI", "org.officelaf.OfficeMetalTreeUI"});
    }
    
    @Override
    protected void initSystemColorDefaults(UIDefaults table) {
        super.initSystemColorDefaults(table);
//        loadSystemColors(table, helper.getSystemColorDefaults(), false);
    }
    
    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        table.putDefaults(helper.getComponentDefaults());
    }
}
