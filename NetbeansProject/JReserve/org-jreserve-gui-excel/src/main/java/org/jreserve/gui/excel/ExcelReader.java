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

package org.jreserve.gui.excel;

import java.io.File;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcelReader implements Runnable {
    
    private final File file;
    
    private final Object lock = new Object();
    private Workbook wb;
    private Exception ex;
    
    public ExcelReader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        synchronized(lock) {
            try {
                wb = WorkbookFactory.create(file);
            } catch (Exception ex2) {
                this.ex = ex2;
            }
        }
    }
    
    public Workbook getWorkbook() throws Exception {
        synchronized(lock) {
            if(ex != null)
                throw ex;
            return wb;
        }
    }
}
