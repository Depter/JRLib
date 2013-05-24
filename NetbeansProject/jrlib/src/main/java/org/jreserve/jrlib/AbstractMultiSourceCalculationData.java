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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Basic implementation for the {@link CalculationData CalculationData} 
 * interface. This class is the most generic implementation of the 
 * CalculationData interface. It handles most of the work in regard with
 * sources by {@link CalculationData#recalculate() recalculation} and
 * {@link CalculationData#detach() detaching}. Extending classes can
 * focus on their inner state.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMultiSourceCalculationData<T extends CalculationData> extends AbstractChangeable implements CalculationData {
    
    private SourceListener sourceListener = new SourceListener();
    private boolean myChange = false;
    
    protected T[] sources;
    private int sourceCount;
    private boolean forwardCalls = true;
    
    protected AbstractMultiSourceCalculationData(T... sources) {
        sourceCount = sources.length;
        this.sources = sources;
        attachSources();
    }
    
    protected AbstractMultiSourceCalculationData(boolean isAttached, T... sources) {
        sourceCount = sources.length;
        this.sources = sources;
        
        if(isAttached) {
            attachSources();
        } else {
            super.setEventsFired(false);
            listeners = null;
            sourceListener = null;
        }
    }
    
    private void attachSources() {
        for(int i=0; i<sourceCount; i++)
            sources[i].addChangeListener(sourceListener);
    }
    
    @Override
    public boolean isCallsForwarded() {
        return forwardCalls;
    }
    
    @Override
    public void setCallsForwarded(boolean forwardCalls) {
        this.forwardCalls = forwardCalls;
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
        if(forwardCalls)
            recalculateSources();
        recalculateLayer();
        fireChange();
    }
    
    private void recalculateSources() {
        myChange = true;
        for(int i=0; i<sourceCount; i++)
            sources[i].recalculate();
        myChange = false;
    }

    /**
     * Extending classes should recalculate their inner state. When
     * this method is called, then the source calculations are already 
     * recalculated.
     */
    protected abstract void recalculateLayer();
    
    /**
     * Detaches all source calculations and removes all
     * registered listeners.
     */
    @Override
    public void detach() {
        if(forwardCalls)
           detachSources();
        
        super.setEventsFired(false);
        listeners = null;
        sourceListener = null;
    }
    
    private void detachSources() {
        for(int i=0; i<sourceCount; i++)
            sources[i].detach();
    }
    
    private class SourceListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if(!myChange) {
                recalculateLayer();
                fireChange();
            }
        }
    }
}
