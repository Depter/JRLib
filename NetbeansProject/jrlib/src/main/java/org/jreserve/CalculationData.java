package org.jreserve;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface CalculationData extends Changeable {
    
    public CalculationData getSource();
    
    public void recalculate();
    
    public void detach();
}