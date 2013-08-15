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
package org.jreserve.gui.excel.poiutil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jreserve.gui.excel.poiutil.xls.XlsNameReader;
import org.jreserve.gui.excel.poiutil.xls.XlsReferenceUtilReader;
import org.jreserve.gui.excel.poiutil.xlsx.XlsxNameReader;
import org.jreserve.gui.excel.poiutil.xlsx.XlsxReferenceUtilReader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PoiUtil {
    
    public static List<String> getDefinedNames(File file) throws IOException {
        ExcelReader<List<String>> reader;
        reader = isXlsx(file)? new XlsxNameReader() : new XlsNameReader();
        return reader.read(file);
    }
    
    private static boolean isXlsx(File file) {
        String name = file.getName().toLowerCase();
        if(name.endsWith(".xlsx"))
            return true;
        else if(name.endsWith(".xls"))
            return false;
        else
            throw new IllegalArgumentException("Unkown file type: "+file.getAbsolutePath());
    }
    
    public static Task<List<String>> getDefinedNamesTask(final File file) {
        return new Task<List<String>>() {
            @Override
            List<String> calculate() throws Exception {
                return getDefinedNames(file);
            }
        };
    }
    
    public static ReferenceUtil getReferenceUtil(File file) throws IOException {
        ExcelReader<ReferenceUtil> reader;
        reader = isXlsx(file)? new XlsxReferenceUtilReader() : new XlsReferenceUtilReader();
        return reader.read(file);
    }
    
    public static Task<ReferenceUtil> getReferenceUtilTask(final File file) {
        return new Task<ReferenceUtil>() {
            @Override
            ReferenceUtil calculate() throws Exception {
                return getReferenceUtil(file);
            }
        };
    }
    
    public static abstract class Task<T> implements Runnable {
        
        private Exception ex;
        private T result;
        
        private Task() {}
        
        @Override
        public void run() {
            try {
                result = calculate();
            } catch (Exception rex) {
                this.ex = rex;
            }
        }
        
        abstract T calculate() throws Exception;
        
        public T get() throws Exception {
            if(ex != null)
                throw ex;
            return result;
        }
    }
}
