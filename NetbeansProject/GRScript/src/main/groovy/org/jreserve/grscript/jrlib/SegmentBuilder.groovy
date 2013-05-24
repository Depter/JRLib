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
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.triangle.Cell

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SegmentBuilder {
	
    private MapUtil mapUtil = MapUtil.getInstance()
    private List<Cell> cells = new ArrayList<Cell>()
    
    private Cell from = null
    private Cell to = null
    
    void from(int accident, int development) {
        from = new Cell(accident, development)
        this.addRange()
    }
    
    private void addRange() {
        if(from && to) {
            this.createRange()
            from = null
            to = null
        }
    }
    
    private void createRange() {
        for(a in from.getAccident()..to.getAccident())
            for(d in from.getDevelopment()..to.getDevelopment())
                this.cell(a, d)
    }
    
    void from(Map map) {
        int accident = mapUtil.getAccident(map)
        int development = mapUtil.getDevelopment(map)
        this.from(accident, development)
    }
    
    void to(int accident, int development) {
        to = new Cell(accident, development)
        this.addRange()
    }
    
    void to(Map map) {
        int accident = mapUtil.getAccident(map)
        int development = mapUtil.getDevelopment(map)
        this.to(accident, development)
    }
    
    void cell(int accident, int development) {
        Cell cell = new Cell(accident, development)
        if(!cells.contains(cell))
            cells.add(cell)
    }
    
    void cell(Map map) {
        int accident = mapUtil.getAccident(map)
        int development = mapUtil.getDevelopment(map)
        this.cell(accident, development)
    }
    
    int[][] getCells() {
        cells.sort()
        int size = cells.size()
        int[][] result = new int[size][]
        for(int i=0; i<size; i++)
            result[i] = cells.get(i).toArray()
        
        cells.clear()
        return result
    }
}

