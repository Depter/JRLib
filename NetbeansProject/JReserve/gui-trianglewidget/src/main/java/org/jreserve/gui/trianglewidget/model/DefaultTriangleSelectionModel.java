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
import java.util.TreeSet;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultTriangleSelectionModel implements TriangleSelectionModel {

    private boolean isAdjusting = false;
    private Set<Cell> selection = new TreeSet<Cell>();
    private List<TriangleSelectionListener> listeners = new ArrayList<TriangleSelectionListener>();
    
    @Override
    public boolean isValueAdjusting() {
        return this.isAdjusting;
    }

    @Override
    public void setValueAdjusting(boolean adjusting) {
        this.isAdjusting = adjusting;
        if(!adjusting)
            fireEvent(-1, -1);
    }
    
    private void fireEvent(int accident, int development) {
        TriangleSelectionEvent event = null;
        for(TriangleSelectionListener listener : getListenerArray()) {
            if(event == null)
                event = new TriangleSelectionEvent(this, accident, development, isAdjusting);
            listener.selectionChanged(event);
        }
    }
    
    private TriangleSelectionListener[] getListenerArray() {
        int size = listeners.size();
        return listeners.toArray(new TriangleSelectionListener[size]);
    }

    @Override
    public boolean isSelected(int accident, int development) {
        return selection.contains(new Cell(accident, development));
    }

    @Override
    public void setSelected(int accident, int development) {
        if(selection.add(new Cell(accident, development)))
            fireEvent(accident, development);
    }
    
    @Override
    public void removeSelection(int accident, int development) {
        if(selection.remove(new Cell(accident, development)))
            fireEvent(accident, development);
    }
    
    @Override
    public void clearSelection() {
        this.selection.clear();
        fireEvent(-1, -1);
    }

    @Override
    public TriangleSelection createSelection(Triangle triangle) {
        return new TriangleSelectionImpl(this.selection, triangle);
    }
    
    @Override
    public void addTriangleSelectionListener(TriangleSelectionListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTriangleSelectionListener(TriangleSelectionListener listener) {
        listeners.remove(listener);
    }

//    static class Cell implements Comparable<Cell> {
//        private final int accident;
//        private final int development;
//
//        private Cell(int accident, int development) {
//            this.accident = accident;
//            this.development = development;
//        }
//        
//        int getAccident() {
//            return accident;
//        }
//
//        int getDevelopment() {
//            return development;
//        }
//        
//        @Override
//        public int compareTo(Cell o) {
//            int dif = accident - o.accident;
//            if(dif == 0)
//                return development - o.development;
//            return dif;
//        }
//        
//        @Override
//        public boolean equals(Object o) {
//            return (o instanceof Cell) &&
//                   compareTo((Cell)o) == 0;
//        }
//        
//        @Override
//        public int hashCode() {
//            int hash = 31 + accident;
//            return 17 * hash + development;
//        }
//        
//        @Override
//        public String toString() {
//            return String.format("Cell [%d; %d]", accident, development);
//        }
//    }
}
