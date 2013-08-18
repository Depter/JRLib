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

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class DataEntryTableReader extends NumericTableReader<List<DataEntry>> {
    
    private final MonthDate.Factory mdf = new MonthDate.Factory();
    private final List<DataEntry> entries = new ArrayList<DataEntry>();

    protected int firstRow = -1;
    protected short firstColumn = -1;
    
    protected void initCoordinates(int row, short column) {
        if(firstRow == -1) {
            firstRow = row;
            firstColumn = column;
        }
    }
    
    protected MonthDate createDate(double value) {
        return mdf.toMonthDate(DateUtil.getJavaDate(value));
    }
    
    protected void addEntry(int row, DataEntry entry) {
        int index = entries.indexOf(entry);
        if(index >= 0) {
            int fRow = row - entries.size() + index;
            String msg = "DataEntry in row %d is a duplicate of the entry in row %d!";
            throw new IllegalArgumentException(String.format(msg, row+1, fRow+1));
        }
        entries.add(entry);
    }

    @Override
    public List<DataEntry> getTable() {
        return entries;
    }    
}
