package org.jreserve;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMultiSourceCalculationData extends AbstractChangeable implements CalculationData {
    
    private SourceListener sourceListener = new SourceListener();
    private boolean myChange = false;
    
    protected AbstractMultiSourceCalculationData() {
    }
    
    protected final void attachSource(CalculationData data) {
        data.addChangeListener(sourceListener);
    }

    @Override
    public final void recalculate() {
        recalculateSource();
        recalculateLayer();
        fireChange();
    }
    
    protected abstract void recalculateSource();
    
    protected final void recalculateSource(CalculationData data) {
        if(data != null) {
            myChange = true;
            data.recalculate();
            myChange = false;
        }
    }

    protected abstract void recalculateLayer();
    
    @Override
    public final void detach() {
        detachSource();
        listeners = null;
        sourceListener = null;
    }
    
    protected abstract void detachSource();
    
    protected final void detachSource(CalculationData data) {
        if(data != null)
            data.detach();
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
