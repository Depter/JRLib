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

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcelUtil {
    
    public static boolean isEmpty(Cell cell) {
        return cell==null || Cell.CELL_TYPE_BLANK == cell.getCellType();
    }
    
    public static String toString(Cell cell) {
        return new CellReference(cell).formatAsString();
    }
    
    public static CellReference getValidCellReference(Workbook wb, String ref) {
        CellReference cr = getCellReference(wb, ref);
        String shName = cr.getSheetName();
        if(shName == null || wb.getSheetIndex(shName) < 0 || 
                cr.getRow() < 0 || cr.getCol() < 0)
            return null;
        return cr;
    }
    
    public static CellReference getCellReference(Workbook wb, String ref) {
        Name name = wb.getName(ref);
        if(name != null)
            ref = name.getRefersToFormula();
        
        if(ref.indexOf(':') > 0) {
            AreaReference aRef = new AreaReference(ref);
            return aRef.getFirstCell();
        } else {
            return new CellReference(ref);
        }
    }
    
    public static List<Name> getReferenceNames(Workbook wb) {
        int size = wb.getNumberOfNames();
        List<Name> result = new ArrayList<Name>(size);
        
        for(int i = 0; i < size; i++) {
            Name name = wb.getNameAt(i);
            if(isReferenceName(wb, name))
                result.add(name);
        }
        
        return result;
    }

    public static boolean isReferenceName(Workbook wb, Name name) {
        return !name.isFunctionName()
                && !name.isDeleted()
                && wb.getSheetIndex(name.getNameName()) < 0;
    }
    
    private ExcelUtil() {}
}
