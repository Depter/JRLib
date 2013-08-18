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

import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleDataEntryTableReader extends DataEntryTableReader {
    private MonthDate accident;
    private MonthDate development;

    @Override
    public void numberFound(int row, short column, double value) throws Exception {
        initCoordinates(row, column);
        if(column == firstColumn) {
            if(accident != null)
                throw new IllegalArgumentException("Illegal entry in row: "+row);
            accident = createDate(value);
        } else if(column == (firstColumn+1)) {
            if(development != null)
                throw new IllegalArgumentException("Illegal entry in row: "+row);
            development = createDate(value);
        } else if(column == (firstColumn+2)) {
            createEntry(row, value);
            accident = null;
            development = null;
        }
    }

    private void createEntry(int row, double value) {
        if(accident == null)
            throw new IllegalStateException("Accident date not set!");
        if(development == null)
            throw new IllegalStateException("Development date not set!");
        addEntry(row, new DataEntry(accident, development, value));
    }
}
