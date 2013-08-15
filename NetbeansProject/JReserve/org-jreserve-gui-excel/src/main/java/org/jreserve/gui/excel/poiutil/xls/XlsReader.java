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
package org.jreserve.gui.excel.poiutil.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jreserve.gui.excel.poiutil.ExcelReader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class XlsReader<T> implements HSSFListener, ExcelReader<T> {
    private final static String DOCUMENT_NAME = "Workbook"; //NOI18
    
    private FileInputStream fin;
    private InputStream din;
    
    @Override
    public T read(File file) throws IOException {
        try {
            openFile(file);
            processFile();
            return getResult();
        } catch (Exception ex) {
            String path = file==null? null : file.getAbsolutePath();
            String msg = String.format("Unabel to read file '%s'!", path);
            throw new IOException(msg, ex);
        } finally {
            closeFile();
        }
    }
    
    private void openFile(File file) throws IOException {
        fin = new FileInputStream(file);
        POIFSFileSystem pfs = new POIFSFileSystem(fin);
        din = pfs.createDocumentInputStream(DOCUMENT_NAME);
    }
    
    private void closeFile() {
        close(din);
        close(fin);
    }
    
    private void close(InputStream is) {
        if(is != null)
            try {is.close();} catch(IOException ex) {}
    }
    
    private void processFile() {
        HSSFRequest req = new HSSFRequest();
        for(short rId : getInterestingReqordIds())
            req.addListener(this, rId);
        HSSFEventFactory factory = new HSSFEventFactory();
        factory.processEvents(req, din);
    }
    
    protected abstract short[] getInterestingReqordIds();
    
    protected abstract T getResult();
}
