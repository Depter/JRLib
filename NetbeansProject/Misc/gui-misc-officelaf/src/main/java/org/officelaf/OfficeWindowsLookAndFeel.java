package org.officelaf;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;

/**
 *
 * @author mikael
 */
public class OfficeWindowsLookAndFeel extends WindowsLookAndFeel {
    private OfficeLookAndFeelHelper helper;

    public OfficeWindowsLookAndFeel() {
        helper = new OfficeLookAndFeelHelper();
    }

    @Override
    public String getName() {
        return "Office";
    }

    @Override
    public String getID() {
        return "Office";
    }

    @Override
    public String getDescription() {
        return "The Office look and feel for Exie 10";
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }
    
    @Override
    public UIDefaults getDefaults() {
        UIDefaults table = helper.createDefaults();

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
        table.putDefaults(new Object[] {"TreeUI", "org.officelaf.OfficeWindowsTreeUI"});
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
