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
    "LBL.GRScriptFileChooser.Description=Groovy Reserve Script (*.grs)"
})
class GRScriptFileFilter extends FileFilter {
    
    private static GRScriptFileFilter INSTANCE = null;
    
    static GRScriptFileFilter getDefault() {
        if(INSTANCE == null)
            INSTANCE = new GRScriptFileFilter();
        return INSTANCE;
    }
    
    private GRScriptFileFilter() {
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() ||
               (f.isFile() && f.getAbsolutePath().toLowerCase().endsWith(".grs"));
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_GRScriptFileChooser_Description();
    }
}
