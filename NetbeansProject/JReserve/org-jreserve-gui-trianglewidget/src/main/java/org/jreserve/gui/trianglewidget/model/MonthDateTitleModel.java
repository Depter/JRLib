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
package org.jreserve.gui.trianglewidget.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jreserve.jrlib.gui.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MonthDateTitleModel implements TitleModel {
        
    private final MonthDate[] dates;
    
    public MonthDateTitleModel(Collection<MonthDate> dates) {
        this.dates = dates.toArray(new MonthDate[dates.size()]);
    }
    
    public MonthDateTitleModel(MonthDate[] dates) {
        this.dates = Arrays.copyOf(dates, dates.length);
    }
        
    @Override
    public String getName(int index) {
        if(index < 0 || index>=dates.length)
            return null;
        return dates[index].toString();
    }
}
