package org.jrlib;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    private boolean myChange = false;
    protected T source;
    private boolean forwardCalls = true;
    
    /**
     * Creates an instance, with the given source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    protected AbstractCalculationData(T source) {
        this.source = source;
        this.source.addChangeListener(sourceListener);
    }

    /**
     * Creates a calculation without a source. If this constructor
     * is used, then extending classes should be aware that `source`
     * can be null when {@link #recalculateLayer() recalculateLayer}
     * is called.
     */
    protected AbstractCalculationData() {
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
     * Recalculates the source calculation (if any), then calls
     * {@link #recalculateLayer() recalculateLayer}, then fires 
     * a change event (if not detached).
     */
    @Override
    public void recalculate() {
        recalculateSource();
        recalculateLayer();
        fireChange();
    }
    
    private void recalculateSource() {
        if(source != null && forwardCalls) {
            myChange = true;
            source.recalculate();
            myChange = false;
        }
    }

    /**
     * Override this method to recalculate the state based on
     * the source calculation. If the no-arg constructor is
     * used the source can be `null`.
     */
    protected abstract void recalculateLayer();
    
    /**
     * Detaches the source calculation (if set), then drops
     * all registred listeners.
     */
    @Override
    public void detach() {
        if(source != null && forwardCalls)
            source.detach();
        
        super.setEventsFired(false);
        listeners = null;
        sourceListener = null;
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
        if(this.source != null)
            this.source.removeChangeListener(sourceListener);
        this.source = source;
        
        if(this.source != null)
            this.source.addChangeListener(sourceListener);
        recalculateLayer();
        fireChange();
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