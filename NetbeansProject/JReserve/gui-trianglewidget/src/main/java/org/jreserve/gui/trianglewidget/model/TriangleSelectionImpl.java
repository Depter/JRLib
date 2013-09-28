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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jreserve.gui.trianglewidget.model.DefaultTriangleSelectionModel.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleSelectionImpl implements TriangleSelection {
    
    private List<Cell> cells = new ArrayList<Cell>();
    private int accident = -1;
    private int development = -1;
    
    TriangleSelectionImpl(Set<Cell> cells) {
        for(Cell cell : cells) {
            if(this.cells.isEmpty()) {
                initBounds(cell);
            } else {
                checkBounds(cell);
            }
            this.cells.add(cell);
        }
    }
    
    private void initBounds(Cell cell) {
        accident = cell.getAccident();
        development = cell.getDevelopment();
    }
    
    private void checkBounds(Cell cell) {
        if(accident != -1 && accident != cell.getAccident())
            accident = -1;
        if(development != -1 && development != cell.getDevelopment())
            development = -1;
    }
    
    @Override
    public int getCellCount() {
        return cells.size();
    }

    @Override
    public int getAccident(int index) {
        return cells.get(index).getAccident();
    }

    @Override
    public int getDevelopment(int index) {
        return cells.get(index).getDevelopment();
    }

    @Override
    public int getAccident() {
        return accident;
    }

    @Override
    public int getDevelopment() {
        return development;
    }
    
    @Override
    public String toString() {
        return String.format("TriangleSelection [%d]", cells.size());
    }
}
