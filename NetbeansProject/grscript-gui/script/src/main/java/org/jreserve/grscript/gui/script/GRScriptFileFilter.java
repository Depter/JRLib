/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript.gui.script;

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
public class GRScriptFileFilter extends FileFilter {
    
    private static GRScriptFileFilter INSTANCE = null;
    
    public static GRScriptFileFilter getDefault() {
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
