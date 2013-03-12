package org.jreserve;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractMultiSourceCalculationData<T extends CalculationData> extends AbstractChangeable implements CalculationData {
    
    private SourceListener sourceListener = new SourceListener();
    private boolean myChange = false;
    
    protected T[] sources;
    private int sourceCount;
    
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
    public final void recalculate() {
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

    protected abstract void recalculateLayer();
    
    @Override
    public final void detach() {
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
