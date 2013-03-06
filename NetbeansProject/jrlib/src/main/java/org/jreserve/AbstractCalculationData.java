package org.jreserve;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationData<V extends CalculationData> extends AbstractMultiSourceCalculationData {
    
    protected V source;
    
    protected AbstractCalculationData(V source) {
        this.source = source;
        attachSource(source);
    }
    
    protected AbstractCalculationData() {
    }
    
    @Override
    public V getSource() {
        return source;
    }
    
    @Override
    protected void recalculateSource() {
        recalculateSource(source);
    }
    
    @Override
    protected void detachSource() {
        detachSource(source);
    }
}
