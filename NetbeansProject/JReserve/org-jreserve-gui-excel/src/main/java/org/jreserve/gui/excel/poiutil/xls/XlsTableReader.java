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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.ss.util.CellReference;
import org.jreserve.jrlib.gui.data.DataEntry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XlsTableReader extends XlsReader<List<DataEntry>>{
    
    private final static short[] RIDS = {
        BOFRecord.sid,
        BoundSheetRecord.sid,
        RowRecord.sid,
        NumberRecord.sid, 
        FormulaRecord.sid
    };
    
    private List<DataEntry> result = new ArrayList<DataEntry>();
    
    private Map<Integer, String> sheets = new HashMap<Integer, String>();
    private int bofPosition = -1;
    private boolean onReferencedSheet = false;
    private boolean onReferencedRow;
    private int rowIndex;
    
    private String sheetName;
    private int firstRow;
    private short firstCol;
    
    public XlsTableReader(CellReference ref) {
        sheetName = ref.getSheetName();
        firstRow = ref.getRow();
        firstCol = ref.getCol();
    }
    
    @Override
    protected List<DataEntry> getResult() {
        return result;
    }
    
    @Override
    protected short[] getInterestingReqordIds() {
        return RIDS;
    }

    @Override
    public void processRecord(Record record) {
        switch(record.getSid()) {
            case BoundSheetRecord.sid:
                boundRecord((BoundSheetRecord) record);
                break;
            case BOFRecord.sid:
                bofRecord((BOFRecord)record);
                break;
            case RowRecord.sid:
                if(onReferencedSheet)
                    rowRecord((RowRecord)record);
                break;
            case NumberRecord.sid:
                break;
            default:
                break;
        }
    }
    
    private void boundRecord(BoundSheetRecord record) {
        sheets.put(record.getPositionOfBof(), record.getSheetname());
    }
    
    private void bofRecord(BOFRecord record) {
        bofPosition++;
        onReferencedSheet = false;
        if(record.getType() == BOFRecord.TYPE_WORKSHEET) {
            String name = sheets.get(bofPosition);
            if(name != null && sheetName!=null && name.equals(sheetName))
                onReferencedSheet = true;
        }
    }
    
    private void rowRecord(RowRecord record) {
        rowIndex = record.getRowNumber();
        if(rowIndex < firstRow)
            onReferencedRow = false;
    }
    
    private void numberRecord(NumberRecord record) {
        if(record.getColumn() < firstCol)
            return;
    }
    
    private void formulaREcord(FormulaRecord record) {
        if(record.getColumn() < firstCol)
            return;
    
    }
}
