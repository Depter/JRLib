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
 * This abstract class represents a link in a calculation chain, whith one 
 * source. The class handles the source calculation (recalculation, detaching,
 * changing, etc.), extending classes should only provide an implementation
 * for {@link #recalculateLayer() recalculateLayer}, where they update their
 * state, based on the source calculation.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationData<T extends CalculationData> extends AbstractChangeable implements CalculationData {
    
    private SourceListener sourceListener = new SourceListener();
    protected T source;
    
    /**
     * Creates an instance, with the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    protected AbstractCalculationData(T source) {
        this.source = source;
        this.source.addCalculationListener(sourceListener);
    }

    /**
     * Creates a calculation without a source. If this constructor
     * is used, then extending classes should be aware that `source`
     * can be null when {@link #recalculateLayer() recalculateLayer}
     * is called.
     */
    protected AbstractCalculationData() {
    }
    
    /**
     * Sets the source for this calculation. After calling this method
     * the following happens:
     * 1.   If the source was alreadt set, then we stop listening for cahnges
     *      on the old source.
     * 2.   If the new source is not `null`, then we start listening for
     *      changes on the new source.
     * 3.   {@link #recalculateLayer() recalculateLayer} is called.
     * 4.   A ChangeEvent is fired.
     * 
     * Basically this mehtod is an implementation for
     * {@link MutableSource#setSource(java.lang.Object) MutableSource}, but
     * extending classes can decide wether they want to implement it.
     */
    public void setSource(T source) {
        setState(CalculationState.INVALID);
        releaseSource();
        initSource(source);
        
        if(CalculationState.VALID == getSourceState())
            recalculate();
    }
    
    private void releaseSource() {
        if(this.source != null)
            this.source.removeCalculationListener(sourceListener);
        this.source = null;
    }
    
    private void initSource(T source) {
        this.source = source;
        if(this.source != null)
            this.source.addCalculationListener(sourceListener);
    }
    
    @Override
    protected CalculationState getSourceState() {
        return source==null? CalculationState.VALID : source.getState();
    }
    
    @Override
    public void detach() {
        detach(source);
    }
    
    @Override
    public void detach(CalculationData source) {
        if(this.source!=null && this.source == source) {
            setState(CalculationState.INVALID);
            releaseSource();
            recalculateLayer();
            setState(CalculationState.VALID);
        }
    }
    
    private class SourceListener implements CalculationListener {

        @Override
        public void stateChanged(CalculationData data) {
            if(CalculationState.INVALID == getSourceState()) {
                setState(CalculationState.INVALID);
            } else {
                recalculateLayer();
                setState(CalculationState.VALID);
            }
        }
    }
}