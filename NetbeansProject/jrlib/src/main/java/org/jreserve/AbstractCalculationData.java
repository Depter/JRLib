package org.jreserve;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationData<T extends CalculationData> extends AbstractChangeable implements CalculationData {
    
    private SourceListener sourceListener = new SourceListener();
    private boolean myChange = false;
    protected T source;
    
    protected AbstractCalculationData(T source) {
        this.source = source;
        this.source.addChangeListener(sourceListener);
    }

    protected AbstractCalculationData() {
    }
    
    @Override
    public void recalculate() {
        recalculateSource();
        recalculateLayer();
        fireChange();
    }
    
    private void recalculateSource() {
        if(source != null) {
            myChange = true;
            source.recalculate();
            myChange = false;
        }
    }

    protected abstract void recalculateLayer();
    
    @Override
    public final void detach() {
        if(source != null)
            source.detach();
        listeners = null;
        sourceListener = null;
    }
    
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
