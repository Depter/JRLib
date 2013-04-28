package org.jreserve.grscript.gui.script.explorer.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.GRScriptFilter.Description=GRScripts (*.grs)"
})
public class GRScriptFilter extends FileFilter {

    private static GRScriptFilter INSTANCE;
    
    static GRScriptFilter getInstance() {
        if(INSTANCE == null)
            INSTANCE = new GRScriptFilter();
        return INSTANCE;
    }
    
    private GRScriptFilter() {
    }
    
    @Override
    public boolean accept(File f) {
        return f.isDirectory() ||
               f.getName().toLowerCase().endsWith(".grs");
    }

    @Override
    public String getDescription() {
        return Bundle.CTL_GRScriptFilter_Description();
    }
}
