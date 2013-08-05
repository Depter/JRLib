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
package org.jreserve.gui.data.csv;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CsvFileFilter.Name=CSV Files"
})
public class CsvFileFilter extends FileFilter {

    private static FileFilter INSTANCE;
    
    public synchronized static FileFilter getDefault() {
        if(INSTANCE == null)
            INSTANCE = new CsvFileFilter();
        return INSTANCE;
    }
    
    private CsvFileFilter() {
    }
    
    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        String name = f.getName();
        return name.endsWith(".csv") || name.endsWith(".CSV");
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_CsvFileFilter_Name();
    }
}
