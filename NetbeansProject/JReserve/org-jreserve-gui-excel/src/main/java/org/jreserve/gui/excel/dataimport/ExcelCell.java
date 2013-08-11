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

package org.jreserve.gui.excel.dataimport;

import org.apache.poi.ss.usermodel.Cell;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelCell<T> {
    
    static enum Type {
        BLANK,
        DATE,
        DOUBLE,
        STRING;
    }
    
    private static MonthDate.Factory MDF = new MonthDate.Factory();
    
    static ExcelCell createCell(Type type, Cell cell) {
        if(cell == null || Cell.CELL_TYPE_BLANK == cell.getCellType())
            return new ExcelCell(Type.BLANK, null);
        
        if(Type.DATE == type) {
            return getDateCell(cell);
        } else {
            return getNumberCell(cell);
        }
    }
    
    private static ExcelCell getNumberCell(Cell cell) {
        try {
            return new ExcelCell(Type.DOUBLE, cell.getNumericCellValue());
        } catch (Exception ex) {
            return new ExcelCell(Type.STRING, getValueAsString(cell));
        }
    }
    
    private static ExcelCell getDateCell(Cell cell) {
        try {
            MonthDate md = MDF.toMonthDate(cell.getDateCellValue());
            return new ExcelCell(Type.DATE, md);
        } catch (Exception ex) {
            return new ExcelCell(Type.STRING, getValueAsString(cell));
        }
    }
    
    private static String getValueAsString(Cell cell) {
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK: return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return (cell.getBooleanCellValue())? "True" : "False";
            case Cell.CELL_TYPE_ERROR:
                return "Error #"+cell.getErrorCellValue();
            case Cell.CELL_TYPE_FORMULA:
                try {
                    return ""+cell.getNumericCellValue();
                } catch (Exception ex) {
                    return cell.getStringCellValue();
                }
            case Cell.CELL_TYPE_NUMERIC:
                return ""+cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return "Unkonw cell type: "+cell.getCellType();
        }
    }
    
    private Type type;
    private T value;
    
    private ExcelCell(Type type, T value) {
        this.type = type;
        this.value = value;
    }
    
    public Type getType() {
        return type;
    }
    
    public T getValue() {
        return value;
    }
}
