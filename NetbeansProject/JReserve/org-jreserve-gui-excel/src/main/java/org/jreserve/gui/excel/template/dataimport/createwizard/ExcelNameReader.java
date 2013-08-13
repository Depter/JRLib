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
package org.jreserve.gui.excel.template.dataimport.createwizard;

import java.io.File;
import java.util.List;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jreserve.gui.excel.ExcelUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelNameReader implements Runnable {
    
    private final File file;
    
    private final Object lock = new Object();
    private List<String> names;
    
    private Exception ex;
    
    ExcelNameReader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        synchronized(lock) {
            try {
                Workbook wb = WorkbookFactory.create(file);
                for(Name name : ExcelUtil.getReferenceNames(wb))
                    names.add(name.getNameName());
            } catch (Exception ex2) {
                this.ex = ex2;
            }
        }
    }
    
    public List<String> getNames() throws Exception {
        synchronized(lock) {
            if(ex != null)
                throw ex;
            return names;
        }
    }
}
