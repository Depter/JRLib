package org.jrlib;

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
    public final void recalculate() {
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
    public final void detach() {
        if(forwardCalls)
           detachSources();
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
