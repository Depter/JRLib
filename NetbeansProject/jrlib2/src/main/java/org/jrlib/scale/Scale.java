package org.jrlib.scale;

import org.jrlib.vector.Vector;

/**
 * The scale interface represents the ability to estimate a scale factor
 * for variances for eache development period within a triangle. Such
 * scales are the sigma parameters for Mack's method and the rho parameters
 * for the Munich Chain-Ladder methods.
 * 
 * In general there are X inputs needed to calculate the scale parameters:
 * 1.   accidents: number of accident periods in the input.
 * 2.   developments: number of development periods in the input.
 * 3.   r(d): an estimated ratio for development period d.
 * 4.   r(a,d): a ratio for accident period a, and development period d.
 * 3.   w(a,d): the weight for r(a,d).
 * 
 * Given these inputs, the scale for development period d is:
 *              sum(w(a,d) * (r(a,d) - r(d))^2
 *     s(d)^2 = ------------------------------, if n>1, Double.NaN otherwise.
 *                          n - 1
 * where <i>n</i> is the number of cells where <i>w(a,d)</i> and 
 * <i>r(a,d)</i> is not <i>Double.NaN</i>, within development period 
 * <i>d</i>.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Scale<T extends ScaleInput> extends Vector {
    
    /**
     * Returns the input used for the calculation.
     */
    public T getSourceInput();
    
    @Override
    public Scale<T> copy();
}
