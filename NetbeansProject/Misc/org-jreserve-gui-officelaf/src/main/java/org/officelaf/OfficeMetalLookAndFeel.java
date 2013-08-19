package org.officelaf;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
/**
 * @author Mikael Tollefsen
 * @author Gunnar A. Reinseth
 */
public class OfficeMetalLookAndFeel extends MetalLookAndFeel {
    private OfficeLookAndFeelHelper helper;

    public OfficeMetalLookAndFeel() {
        helper = new OfficeLookAndFeelHelper();
    }

    @Override
    public String getName() {
        return "OfficeMetal";
    }

    @Override
    public String getID() {
        return "OfficeMetal";
    }

    @Override
    public String getDescription() {
        return "The Office Metal look and feel";
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