/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.gui.poi.read.xlsx;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Peter Decsi
 */
public class XlsxTableReader<T> extends XlsxReader<T> {
    
    private final String sheetName;
    private final int firstRow;
    private final short firstColumn;
    
    
    private SharedStringsTable ssTable;
    private InputStream sh;
    
    public XlsxTableReader(CellReference ref) {
        sheetName = ref.getSheetName();
        firstRow = ref.getRow();
        firstColumn = ref.getCol();
    }

    @Override
    protected T getResult() {
        return null;
    }

    @Override
    protected void readReader(XSSFReader reader) throws Exception {
        try {
            String rId = getSheetId(reader);
            if(rId == null)
                throw new IllegalArgumentException(String.format("Sheet '%s' not found!", sheetName));
            
            sh = reader.getSheet(rId);
            InputSource is = new InputSource(sh);
            XMLReader parser = XlsxReader.createParser(this);
            parser.parse(is);            
        } finally {
            close();
        }
    }
    
    private String getSheetId(XSSFReader reader) throws Exception {
        SheetIdReader idReader = new SheetIdReader();
        return idReader.getSheetId(sheetName, reader);
    }
    
    private void close() {
        if(sh != null)
            try{sh.close();} catch(IOException ex) {}
    }
}
