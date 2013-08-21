/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jreserve.gui.poi.read.xlsx;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Peter Decsi
 */
class SheetIdReader extends DefaultHandler {
    private final static String TAG_NAME = "sheet";
    private final static String NAME_ATTR = "name";
    private final static String ID_ATTR = "r:id";
    
    private String sheetName;
    private InputStream wb;
    private String sheetId = null;
    
    String getSheetId(String sheetName, XSSFReader reader) throws Exception {
        this.sheetName = sheetName;
        try {
            wb = reader.getWorkbookData();
            InputSource is = new InputSource(wb);
            XMLReader parser = XlsxReader.createParser(this);
            parser.parse(is);
            return  sheetId;
        } finally {
            close();
        }
    }
    
    private void close() {
        if(wb != null)
            try{wb.close();} catch(IOException ex) {}
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(sheetId != null || !TAG_NAME.equals(qName))
            return;
        String name = attributes.getValue(NAME_ATTR);
        if(name==null || !sheetName.equals(name))
            return;
        sheetId = attributes.getValue(ID_ATTR);
    }
    
    
}
