package org.jreserve;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationData<T extends CalculationData> extends AbstractMultiSourceCalculationData {
    
    protected T source;
    
    protected AbstractCalculationData(T source) {
        this.source = source;
        attachSource(source);
    }
    
    protected AbstractCalculationData() {
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
