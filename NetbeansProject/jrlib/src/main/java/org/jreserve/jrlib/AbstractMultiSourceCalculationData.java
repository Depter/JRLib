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
    
    protected T[] sources;
    private int sourceCount;
    
    protected AbstractMultiSourceCalculationData(T... sources) {
        sourceCount = sources.length;
        this.sources = sources;
        attachSources();
    }
    
    private void attachSources() {
        for(int i=0; i<sourceCount; i++)
            sources[i].addCalculationListener(sourceListener);
    }
    
    @Override
    protected CalculationState getSourceState() {
        for(CalculationData source : sources)
            if(CalculationState.INVALID == source.getState())
                return CalculationState.INVALID;
        return CalculationState.VALID;
    }
    
    @Override
    public void detach() {
        if(sourceCount > 0) {
            setState(CalculationState.INVALID);
            releaseSources();
            recalculateLayer();
            setState(CalculationState.VALID);
        }    
    }
    
    private void releaseSources() {
        for(int i=0; i<sourceCount; i++)
            releaseSource(i);
        sources = null;
        sourceCount = 0;
    }
    
    private void releaseSource(int index) {
        if(sources[index] != null) {
            sources[index].removeCalculationListener(sourceListener);
            sources[index] = null;
        }
    }
    
    @Override
    public void detach(CalculationData source) {
        int index = getSourceIndex(source);
        if(index != -1) {
            setState(CalculationState.INVALID);
            
            releaseSource(index);
            cleanUpSources();
            recalculateLayer();
            
            setState(CalculationState.VALID);
        }
    }
    
    private void cleanUpSources() {
        for(CalculationData source : sources)
            if(source != null)
                return;
        sources = null;
        sourceCount = 0;
    }
    
    private int getSourceIndex(CalculationData source) {
        for(int i=0; i<sourceCount; i++)
            if(sources[i] == source)
                return i;
        return -1;
    }
    
    private class SourceListener implements CalculationListener {

        @Override
        public void stateChanged(CalculationData data) {
            if(CalculationState.INVALID == getSourceState()) {
                if(CalculationState.INVALID != getState())
                    setState(CalculationState.INVALID);
            } else {
                recalculateLayer();
                setState(CalculationState.VALID);
            }
        }
    }
}
