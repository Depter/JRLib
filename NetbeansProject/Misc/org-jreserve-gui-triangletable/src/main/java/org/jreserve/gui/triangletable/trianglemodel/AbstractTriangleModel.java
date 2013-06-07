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

package org.jreserve.gui.triangletable.trianglemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.jrlib.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleModel implements TriangleModel {
    
    protected Triangle triangle;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private TriangleListener triangleListener = new TriangleListener();
    
    protected AbstractTriangleModel() {
    }
    
    protected AbstractTriangleModel(Triangle triangle) {
        this.triangle = triangle;
        if(triangle != null)
            triangle.addChangeListener(triangleListener);
    }

    @Override
    public Triangle getTriangle() {
        return triangle;
    }

    @Override
    public void setTriangle(Triangle triangle) {
        releaseOldTriangle();
        initNewTriangle(triangle);
        fireChange();
    }
    
    private void releaseOldTriangle() {
        if(this.triangle != null)
            this.triangle.removeChangeListener(triangleListener);
    }
    
    private void initNewTriangle(Triangle triangle) {
        this.triangle = triangle;
        if(this.triangle != null)
            this.triangle.addChangeListener(triangleListener);
    }

    @Override
    public boolean hasValueAt(int row, int column) {
        if(triangle == null)
            return false;
        
        int accident = getAccidentIndex(row, column);
        if(accident<0 || accident>=triangle.getAccidentCount())
            return false;
        
        int development = getDevelopmentIndex(row, column);
        return development>=0 && development<triangle.getDevelopmentCount(accident);
    }
    
    @Override
    public double getValueAt(int row, int column) {
        if(triangle == null)
            return Double.NaN;
        int accident = getAccidentIndex(row, column);
        int development = getDevelopmentIndex(row, column);
        return triangle.getValue(accident, development);
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireChange() {
        ChangeEvent evt = null;
        for(ChangeListener listener : listenerArray()) {
            if(evt == null)
                evt = new ChangeEvent(this);
            listener.stateChanged(evt);
        }
    }
    
    private ChangeListener[] listenerArray() {
        ChangeListener[] result = new ChangeListener[listeners.size()];
        return listeners.toArray(result);
    }
    
    private class TriangleListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
}
