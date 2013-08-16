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

package org.jreserve.gui.poi;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ExcelFileFilter.Name=Excel Files (*.xls, *.xlsx)"
})
public class ExcelFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        String name = f.getName().toLowerCase();
        return name.endsWith(".xls") || name.endsWith(".xlsx");
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_ExcelFileFilter_Name();
    }

}
