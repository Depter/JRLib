package org.jreserve;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationData<V extends CalculationData> extends AbstractChangeable implements CalculationData {
    
    protected SourceListener sourceListener = new SourceListener();
    protected V source;
    private boolean myChange = false;
    
    protected AbstractCalculationData() {
    }
    
    protected AbstractCalculationData(V source) {
        this.source = source;
        this.source.addChangeListener(sourceListener);
    }
    
    @Override
    public V getSource() {
        return source;
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
    public void detach() {
        if(source != null)
            source.detach();
        listeners = null;
        sourceListener = null;
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
