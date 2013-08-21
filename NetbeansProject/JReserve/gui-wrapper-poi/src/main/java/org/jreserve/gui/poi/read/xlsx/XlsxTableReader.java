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
import org.jreserve.gui.poi.read.ExcelReadException;
import org.jreserve.gui.poi.read.TableFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 *
 * @author Peter Decsi
 */
public class XlsxTableReader<T> extends XlsxReader<T> {
    
    private final static String ROW_TAG = "row";
    private final static String ROW_NUMBER_ATTR = "r";
    private final static String CELL_TAG = "c";
    private final static String CELL_REF_ATTR = "r";
    private final static String CELL_TYPE_ATTR = "t";
    private final static String CELL_TYPE_STRING = "s";
    private final static String CELL_TYPE_STRING_FUNCTION = "str";
    private final static String CELL_TYPE_BOOLEAN = "b";
    private final static String CELL_TYPE_ERROR = "e";
    private final static String VALUE_TAG = "v";
    
    private final static int VALUE_TYPE_NUMERIC = 0;
    private final static int VALUE_TYPE_STRING = 1;
    private final static int VALUE_TYPE_STRING_FUNCTION = 2;
    private final static int VALUE_TYPE_BOOLEAN = 3;
    private final static int VALUE_TYPE_ERROR = 4;

    private final TableFactory<T> factory;
    private final String sheetName;
    private final int firstRow;
    private final short firstColumn;
    
    private SharedStringsTable ssTable;
    private InputStream sh;
    
    private int rowIndex;
    private short columnIndex;
    private int cellType = -1;
    private boolean inCell = false;
    private final StringBuilder sb = new StringBuilder();
    private short lastColumn = -1;
    private int prevRow;
    private short prevColumn;

    public XlsxTableReader(CellReference ref, TableFactory<T> factory) {
        this.factory = factory;
        sheetName = ref.getSheetName();
        firstRow = ref.getRow();
        prevRow = firstRow;
        firstColumn = ref.getCol();
        prevColumn = firstColumn;
        lastColumn = firstColumn;
    }

    @Override
    protected T getResult() throws Exception {
        return factory.getTable();
    }

    @Override
    protected void readReader(XSSFReader reader) throws Exception {
        try {
            ssTable = reader.getSharedStringsTable();
            String sheetId = getSheetId(reader);
            readSheet(reader, sheetId);
        } finally {
            close();
        }
    }
    
    private String getSheetId(XSSFReader reader) throws Exception {
        SheetIdReader idReader = new SheetIdReader();
        String rId = idReader.getSheetId(sheetName, reader);
        if(rId == null)
            throw new IllegalArgumentException(String.format("Sheet '%s' not found!", sheetName));
        return rId;
    }
    
    private void readSheet(XSSFReader reader, String sheetId) throws Exception {
        sh = reader.getSheet(sheetId);
        InputSource is = new InputSource(sh);
        XMLReader parser = super.createParser();
        parser.parse(is);            
    }
    
    private void close() {
        if(sh != null)
            try{sh.close();} catch(IOException ex) {}
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(ROW_TAG.equals(qName)) {
            setRowIndex(attributes);
        } else if(CELL_TAG.equals(qName)) {
            setColumnIndex(attributes);
            cellType = getCellType(attributes);
        } else if(VALUE_TAG.equals(qName)) {
            if(rowIndex >= firstRow && columnIndex >= firstColumn) {
                inCell = true;
                sb.setLength(0);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(VALUE_TAG.equals(qName)) {
            inCell = false;
            try {
                cellFound();
            } catch (Exception ex) {
                CellReference ref = new CellReference(sheetName, rowIndex, columnIndex, false, false);
                throw new SAXException(new ExcelReadException(ref, ex));
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(inCell)
            sb.append(ch, start, length);
    }
    
    private void setRowIndex(Attributes attributes) {
        String sIndex = attributes.getValue(ROW_NUMBER_ATTR);
        if(sIndex==null || sIndex.length()==0)
            throw new IllegalArgumentException("Row number attribute is not set!");
        rowIndex = Integer.parseInt(sIndex) - 1;
    }
    
    private void setColumnIndex(Attributes attributes) {
        String ref = attributes.getValue(CELL_REF_ATTR);
        if(ref==null || ref.length()==0)
            throw new IllegalArgumentException("Cell reference attribute is not set!");
        columnIndex = new CellReference(ref).getCol();
    }
    
    private int getCellType(Attributes attr) {
        String vType = attr.getValue(CELL_TYPE_ATTR);
        if(vType==null || vType.length()==0)
            return VALUE_TYPE_NUMERIC;
        if(CELL_TYPE_STRING.equals(vType))
            return VALUE_TYPE_STRING;
        if(CELL_TYPE_STRING_FUNCTION.equals(vType))
            return VALUE_TYPE_STRING_FUNCTION;
        if(CELL_TYPE_BOOLEAN.equals(vType))
            return VALUE_TYPE_BOOLEAN;
        if(CELL_TYPE_ERROR.equals(vType))
            return VALUE_TYPE_ERROR;
        throw new IllegalArgumentException("Unkown cell value type: "+vType);
    }
    
    private void cellFound() throws Exception {
        //Not on the good sheet, before first row/column
        if(firstRow > rowIndex || firstColumn > columnIndex)
            return;
        
        //The cell is empty, do not read it
        boolean isEmptyCell = sb.length() == 0;
        if(isEmptyCell) {
            //If first cell is empty, whole table is empty
            if(firstRow==rowIndex && firstColumn == columnIndex)
                lastColumn = (short)(firstColumn - 1);
            return;
        }
        
        //Calculate the last column, based on the first row
        if(firstRow == rowIndex) {
            //If we missed a cell, do not read other columns
            if((columnIndex - lastColumn) < 2)
                lastColumn = columnIndex;
        }
        
        //after the last column
        if(columnIndex > lastColumn)
            return;
        
        if(columnIndex == firstColumn)
            prevColumn = firstColumn;
        
        //missed a row, do not read further
        if((rowIndex - prevRow)> 1 || (columnIndex-prevColumn)>1)
            return;
        
        String str = sb.toString();
        switch(cellType) {
            case VALUE_TYPE_NUMERIC:
                factory.numberFound(rowIndex, columnIndex, Double.parseDouble(str));
                break;
            case VALUE_TYPE_STRING:
                int index = Integer.parseInt(str);
                factory.stringFound(rowIndex, columnIndex, ssTable.getEntryAt(index).getT());
                break;
            case VALUE_TYPE_STRING_FUNCTION:
                factory.stringFound(rowIndex, columnIndex, str);
                break;
            case VALUE_TYPE_BOOLEAN:
                factory.booleanFound(rowIndex, columnIndex, "1".equals(str));
                break;
            case VALUE_TYPE_ERROR:
                factory.errorFound(rowIndex, columnIndex, str);
            default:
                break;
        }
        
        prevRow = rowIndex;
        prevColumn = columnIndex;
    }
    
    protected final String getCellReference(int row, short column) {
        return new CellReference(sheetName, row, column, 
                false, false).formatAsString();
    }
}