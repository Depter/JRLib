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
package org.jreserve.jrlib.bootstrap.residualgenerator;

import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractResidualSegment<V> {

    protected int[][] cells;
    protected int cellCount;
    protected int valueCount;
    
    protected AbstractResidualSegment() {
    }
        
    protected AbstractResidualSegment(int[][] cells) {
        this.cells = cells;
        this.cellCount = cells.length;
    }

        
    public boolean containsCell(int accident, int development) {
        //TODO Binary search
        for(int c = 0; c<cellCount; c++) {
            int[] cell = cells[c];
            if(cell[0] == accident && cell[1] == development)
                return true;
        }
        return false;    
    }
    
    protected void setValues(List<V> values) {
        this.valueCount = values.size();
    }
}
