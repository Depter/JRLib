package org.officelaf;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.UIDefaults;
import org.netbeans.swing.plaf.nimbus.NimbusLFCustoms;

/**
 *
 * @author mikael
 */
public class OfficeNimbusLookAndFeel extends NimbusLookAndFeel {
    private OfficeLookAndFeelHelper helper;

    public OfficeNimbusLookAndFeel() {
        helper = new OfficeLookAndFeelHelper();
    }

    public String getName() {
        return "OfficeNimbus";
    }

    public String getID() {
        return "OfficeNimbus";
    }

    public String getDescription() {
        return "The Office Nimbus look and feel";
    }


    public boolean getSupportsWindowDecorations() {
        return true;
    }

    public UIDefaults getDefaults() {
        UIDefaults table = super.getDefaults();

        initClassDefaults(table);
        initSystemColorDefaults(table);
        initComponentDefaults(table);

        table.put("Nb." + getName() + "LFCustoms", new OfficeNimbusLFCustoms());
        return table;
    }

    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        table.putDefaults(helper.getClassDefaults());
        //table.putDefaults(new Object[] {"TreeUI", "org.officelaf.OfficeMetalTreeUI"});
    }

    protected void initSystemColorDefaults(UIDefaults table) {
        super.initSystemColorDefaults(table);
        loadSystemColors(table, helper.getSystemColorDefaults(), false);
    }

    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        table.putDefaults(helper.getComponentDefaults());
    }
}
