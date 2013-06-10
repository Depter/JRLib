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
package org.jreserve.jrlib;

import java.util.ArrayList;
import java.util.List;

/**
 * Most basic implementation for the {@link CalculationData CalculationData} 
 * interface. This class does nothing extra, just reduces 
 * boiler-plate codeing.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractChangeable implements CalculationData {

    private int listenerCount = 0;
    private List<CalculationListener> listeners = new ArrayList<CalculationListener>();
    private CalculationState state = CalculationState.VALID;
    
    @Override
    public CalculationState getState() {
        return state;
    }
    
    /**
     * Handles the recalculation process. By calling this method at 
     * first the sources are recalculated (extending class does not 
     * recieve notifications), then 
     * {@link #recalculateLayer() recalculateLayer} is called, 
     * then all registerd listeners (if not detached) are notified.
     */
    @Override
    public void recalculate() {
        setState(CalculationState.INVALID);
        recalculateLayer();
        setState(CalculationState.VALID);
    }
    
    protected void setState(CalculationState state) {
        if(this.state != state) {
            this.state = state;
            fireStateChange();
        }
    }

    /**
     * Override this method to recalculate the state based on
     * the source calculation. If the no-arg constructor is
     * used the source can be `null`.
     */
    protected abstract void recalculateLayer();
    
    protected abstract CalculationState getSourceState();
    
    @Override
    public void addCalculationListener(CalculationListener listener) {
        if(listeners!=null && listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            listenerCount++;
        }
    }

    @Override
    public void removeCalculationListener(CalculationListener listener) {
        if(listeners != null && listeners.remove(listener))
            listenerCount--;
    }
    
    /**
     * Fires a state change event. The source
     * of the event will be this object.
     */
    protected void fireStateChange() {
        for(int i=0; i<listenerCount; i++)
            listeners.get(i).stateChanged(this);
    }
}
