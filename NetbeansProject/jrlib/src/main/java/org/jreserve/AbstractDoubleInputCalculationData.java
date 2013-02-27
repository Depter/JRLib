package org.jreserve;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractDoubleInputCalculationData<P extends CalculationData, S extends CalculationData> extends AbstractChangeable implements CalculationData {

    private SourceListener sourceListener = new SourceListener();
    protected P primary;
    protected S secondary;
    private boolean myChange = false;
    
    protected AbstractDoubleInputCalculationData(P primary, S secondary) {
        this.primary = primary;
        this.primary.addChangeListener(sourceListener);
        this.secondary = secondary;
        this.secondary.addChangeListener(sourceListener);
    }
    
    @Override
    public P getSource() {
        return primary;
    }
    
    public S getSecondarySource() {
        return secondary;
    }

    @Override
    public void recalculate() {
        recalculateSource(primary);
        recalculateSource(secondary);
        recalculateLayer();
        fireChange();
    }
    
    private void recalculateSource(CalculationData source) {
        if(source != null) {
            myChange = true;
            source.recalculate();
            myChange = false;
        }
    }

    protected abstract void recalculateLayer();

    public void detach() {
        detachSources();
        listeners = null;
        sourceListener = null;
    }
    
    private void detachSources() {
        if(primary != null)
            primary.detach();
        if(secondary != null)
            secondary.detach();
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
