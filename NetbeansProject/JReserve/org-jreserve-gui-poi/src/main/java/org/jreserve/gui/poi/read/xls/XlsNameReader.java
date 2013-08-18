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
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.Record;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XlsNameReader extends XlsReader<List<String>>{
    
    private final static short[] RIDS = {
        NameRecord.sid
    };
    
    private final List<String> names = new ArrayList<String>();

    @Override
    protected List<String> getResult() {
        return names;
    }
    
    @Override
    protected short[] getInterestingReqordIds() {
        return RIDS;
    }

    @Override
    protected void recordFound(Record record) {
        NameRecord nr = (NameRecord) record;
        names.add(nr.getNameText());
    }
}
