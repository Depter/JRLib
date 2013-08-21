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
package org.jreserve.gui.poi.read.xlsx;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class XlsxWorkbookReader<T> extends XlsxReader<T> {
    
    private InputStream wb;
    
    @Override
    protected void readReader(XSSFReader reader) throws Exception {
        try {
            wb = reader.getWorkbookData();
            InputSource is = new InputSource(wb);
            XMLReader parser = super.createParser();
            parser.parse(is);
        } finally {
            close();
        }
    }
    
    private void close() {
        if(wb != null)
            try{wb.close();} catch(IOException ex) {}
    }

}
