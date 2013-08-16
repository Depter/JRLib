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
package org.jreserve.gui.poi.read.xls;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.CellRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.LittleEndianOutput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XlsTableReader extends XlsReader<Void>{
    
    private final static short[] RIDS = {
        BOFRecord.sid,
        BoundSheetRecord.sid,
        RowRecord.sid,
        BoolErrRecord.sid,
        FormulaRecord.sid,
        SSTRecord.sid,
        LabelSSTRecord.sid,
        LabelRecord.sid,
        StringRecord.sid,
        NumberRecord.sid 
    };
    
    private final List<String> sheets = new ArrayList<String>();
    private final String sheetName;
    private final int firstRow;
    private final short firstColumn;
    
    private int prevRow;
    private short prevColumn;
    private boolean outputNextString = false;
    
    private int sheetIndex = -1;
    private boolean onReferencedSheet = false;
    private boolean tableEnded = false;
    private SSTRecord sstRecord;
    
    private short lastColumn = 0;
    
    public XlsTableReader(CellReference ref) {
        sheetName = ref.getSheetName();
        firstRow = ref.getRow();
        firstColumn = ref.getCol();
        lastColumn = firstColumn;
        prevRow = firstRow;
    }
    
    @Override
    protected Void getResult() {
        return null;
    }
    
    @Override
    protected short[] getInterestingReqordIds() {
        return RIDS;
    }

    @Override
    public void processRecord(Record record) {
        if(tableEnded)
            return;
        
        switch(record.getSid()) {
            case BoundSheetRecord.sid:
                boundRecord((BoundSheetRecord) record);
                break;
            case BOFRecord.sid:
                bofRecord((BOFRecord)record);
                break;
            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;
            case BoolErrRecord.sid:
                cellRecord((CellRecord)record);
                break;
            case FormulaRecord.sid:
                cellRecord((CellRecord)record);
                break;
            case LabelSSTRecord.sid:
                cellRecord((CellRecord)record);
                break;
            case LabelRecord.sid:
                LabelRecord lr = (LabelRecord) record;
                cellRecord(lr.getRow(), lr.getColumn(), lr);
                break;
            case StringRecord.sid:
                if(outputNextString) {
                    cellRecord(prevRow, prevColumn, record);
                    outputNextString = false;
                }
                break;
            case NumberRecord.sid:
                cellRecord((CellRecord)record);
                break;
            default:
                break;
        }
    }
    
    private void boundRecord(BoundSheetRecord record) {
        sheets.add(record.getSheetname());
    }
    
    private void bofRecord(BOFRecord record) {
        if(BOFRecord.TYPE_WORKSHEET == record.getType()) {
            sheetIndex++;
            onReferencedSheet = false;
            if(sheetIndex < sheets.size() && sheetName!=null)
                onReferencedSheet = sheetName.equals(sheets.get(sheetIndex));
        }
    }
    
    private void cellRecord(CellRecord cr) {
        cellRecord(cr.getRow(), cr.getColumn(), cr);
    }
    
    private void cellRecord(int row, short column, Record record) {
        //Not on the good sheet, before first row/column
        if(!onReferencedSheet || firstRow > row || firstColumn > column)
            return;
        
        //The cell is empty, do not read it
        boolean isEmptyCell = isEmpty(record);
        if(isEmptyCell) {
            //If first cell is empty, whole table is empty
            if(firstRow==row && firstColumn == column)
                lastColumn = (short)(firstColumn - 1);
            return;
        }
        
        //Calculate the last column, based on the first row
        if(firstRow == row) {
            //If we missed a cell, do not read other columns
            if((column - lastColumn) < 2)
                lastColumn = column;
        }
        
        //after the last column
        if(column > lastColumn)
            return;
        
        //missed a row, do not read further
        if(row - prevRow > 1)
            return;
        
        short sid = record.getSid();
        switch(sid) {
            case NumberRecord.sid:
                numberFound(row, column, ((NumberRecord)record).getValue());
                break;
            case LabelSSTRecord.sid:
                if(sstRecord == null)
                    throw new IllegalArgumentException("SSTRecord is null");
                int stringId = ((LabelSSTRecord)record).getSSTIndex();
                String str = sstRecord.getString(stringId).getString();
                stringFound(row, column, str);
                break;
            case LabelRecord.sid:
                stringFound(row, column, ((LabelRecord)record).getValue());
                break;
            case StringRecord.sid:
                stringFound(row, column, ((StringRecord)record).getString());
                break;
            case FormulaRecord.sid:
                FormulaRecord fr = (FormulaRecord) record;
                if(fr.hasCachedResultString()) {
                    outputNextString = true;
                } else {
                    numberFound(row, column, fr.getValue());
                }
                break;
            case BoolErrRecord.sid:
                BoolErrRecord ber = (BoolErrRecord) record;
                if(ber.isBoolean()) {
                    booleanFound(row, column, ber.getBooleanValue());
                } else {
                    errorFound(row, column, ber.getErrorValue());
                }
                break;
            default:
                break;
        }
        
        prevRow = row;
        prevColumn = column;
    }
    
    private boolean isEmpty(Record record) {
        short sid = record.getSid();
        if(LabelSSTRecord.sid == sid) {
            String str = sstRecord.getString(((LabelSSTRecord)record).getSSTIndex()).getString();
            return str==null || str.length()==0;
        }
        return false;
    }
    
    protected final String getCellReference(CellRecord record) {
        return getCellReference(record.getRow(), record.getColumn());
    }
    
    protected final String getCellReference(int row, short column) {
        return new CellReference(sheetName, row, column, 
                false, false).formatAsString();
    }
    
    protected void numberFound(int row, short column, double value) {
        String ref = getCellReference(row, column);
        System.out.println("[NUMBER ] "+ref +" = "+value);
    }
    
    protected void stringFound(int row, short column, String value) {
        String ref = getCellReference(row, column);
        System.out.println("[STRING ] "+ref +" = "+value);
    }
    
    protected void booleanFound(int row, short column, boolean value) {
        String ref = getCellReference(row, column);
        System.out.println("[BOOLEAN] "+ref +" = "+value);
    }
    
    protected void errorFound(int row, short column, byte value) {
        String ref = getCellReference(row, column);
        System.out.println("[ERROR  ] "+ref +" = "+value);
    }
    
    
//    protected final Date getDate(CellRecord record) {
//        try {
//            double value = getDoubleValue(record);
//            return DateUtil.getJavaDate(value);
//        } catch (Exception ex) {
//            String ref = getCellReference(record);
//            throw new IllegalArgumentException(String.format("Unable to read date value from cell '%s'!", ref));
//        }
//    }
//    
//    protected final double getDoubleValue(CellRecord record) {
//        short sid = record.getSid();
//        if(NumberRecord.sid == sid)
//            return ((NumberRecord) record).getValue();
//        if(FormulaRecord.sid == sid)
//            return getDoubleValue((FormulaRecord)record);
//        
//        String ref = getCellReference(record);
//        throw new IllegalArgumentException(String.format("Unable to read numeric value from cell '%s'!", ref));
//    }
//    
//    protected final double getDoubleValue(FormulaRecord record) {
//        if(HSSFCell.CELL_TYPE_NUMERIC == record.getCachedResultType())
//                return record.getValue();
//        String ref = getCellReference(record);
//        throw new IllegalArgumentException(String.format("Unable to read numeric value from cell '%s'!", ref));
//    }
//    
//    protected final String getStringValue(CellRecord record) {
//        short sid = record.getSid();
//        if(LabelSSTRecord.sid == record.getSid()) {
//            if(sstRecord == null)
//                throw new IllegalStateException("SSTRecord was never set!");
//            return sstRecord.getString(((LabelSSTRecord)record).getSSTIndex()).getString();
//        }
//        
//        if(FormulaRecord.sid == sid)
//            return getString((FormulaRecord) record);
//    }
//    
//    protected final String getStringValue(FormulaRecord record) {
//        if(HSSFCell.CELL_TYPE_STRING == record.getCachedResultType())
//            return record.get;
//    }
    
    private static class LabelCellRecord extends CellRecord {
        private final LabelRecord lr;

        public LabelCellRecord(LabelRecord lr) {
            this.lr = lr;
        }

        
        
        @Override
        protected void appendValueText(StringBuilder sb) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected String getRecordName() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected void serializeValue(LittleEndianOutput out) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected int getValueDataSize() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public short getSid() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
