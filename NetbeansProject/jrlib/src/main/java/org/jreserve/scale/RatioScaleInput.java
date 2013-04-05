package org.jreserve.scale;

import org.jreserve.CalculationData;
import org.jreserve.Copyable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface RatioScaleInput extends CalculationData, Copyable<RatioScaleInput> {

    public int getDevelopmentCount();

    public int getAccidentCount(int development);
    
    public double getRatio(int development);

    public double getRatio(int accident, int development);
    
    public double getWeight(int accident, int development);
}
