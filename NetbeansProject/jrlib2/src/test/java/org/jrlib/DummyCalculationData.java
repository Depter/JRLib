package org.jrlib;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DummyCalculationData<T extends CalculationData> extends AbstractCalculationData<T> {

    public DummyCalculationData() {
    }

    public DummyCalculationData(T source) {
        super(source);
    }

    @Override
    protected void recalculateLayer() {
    }
}
