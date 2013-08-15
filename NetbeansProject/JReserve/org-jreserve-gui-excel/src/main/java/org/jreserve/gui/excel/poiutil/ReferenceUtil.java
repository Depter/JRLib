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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.ss.util.CellReference;

/**
 *
 * @author Peter Decsi
 */
public class ReferenceUtil {
    
    private final static char CELL_SEPARATOR = ':';
    
    private final static short MAX_COL_XLSX = 16384;
    private final static int MAX_ROW_XLSX = 1048576;
    
    private final static short MAX_COL_XLS = 256;
    private final static int MAX_ROW_XLS = 65536;
    
    private final List<String> sheets;
    private final Map<String, String> names; 
    private boolean isXls;
    
    public ReferenceUtil(List<String> sheets, Map<String, String> names, boolean isXls) {
        this.sheets = initUmodifiable(sheets);
        this.names = new TreeMap<String, String>(names);
        this.isXls = isXls;
    }
    
    private List<String> initUmodifiable(List<String> list) {
        Collections.sort(list);
        return Collections.unmodifiableList(list);
    }
    
    public List<String> getNames() {
        return new ArrayList<String>(names.keySet());
    }
    
    public List<String> getSheets() {
        return sheets;
    }
    
    public boolean isReferenceValid(String ref) {
        if(ref == null || ref.length()==0)
            return false;
        if(names.containsKey(ref))
            return true;
        try {
            return isReferenceValid(new CellReference(ref));
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean isReferenceValid(CellReference ref) {
        if(ref == null)
            return false;
        
        String sheet = ref.getSheetName();
        if(sheet == null || sheet.length()==0 || !sheets.contains(sheet))
            return false;
        
        if(ref.getCol() >= getMaxCol())
            return false;
        
        if(ref.getRow() >= getMaxRow())
            return false;
        
        return true;
    }
    
    private short getMaxCol() {
        return isXls? MAX_COL_XLS : MAX_COL_XLSX;
    }
    
    private int getMaxRow() {
        return isXls? MAX_ROW_XLS : MAX_ROW_XLSX;
    }
    
    public String getReference(String name) {
        return toCellReference(name).formatAsString();
    }
    
    public CellReference toCellReference(String ref) {
        String formula = names.get(ref);
        if(formula != null)
            return toCellReference(formula);
        
        int index = ref.indexOf(CELL_SEPARATOR);
        if(index >= 0)
            ref = ref.substring(0, index);
        
        
        return new CellReference(ref);
    }
}
