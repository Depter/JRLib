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
package org.jreserve.grscript.gui.classpath.explorer.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.jreserve.grscript.gui.classpath.ClassPathUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.JavaSourceFilter.Description=Java binary (*.jar, *.class)"
})
class JavaSourceFilter extends FileFilter {

    private static JavaSourceFilter INSTANCE;
    
    static JavaSourceFilter getInstance() {
        if(INSTANCE == null)
            INSTANCE = new JavaSourceFilter();
        return INSTANCE;
    }
    
    private JavaSourceFilter() {
    }
    
    @Override
    public boolean accept(File f) {
        return f!=null &&
               (f.isDirectory() || ClassPathUtil.isJavaBinary(f));
    }

    @Override
    public String getDescription() {
        return Bundle.CTL_JavaSourceFilter_Description();
    }
}
