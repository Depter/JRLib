package org.jreserve.jrlib.linkratio.standarderror;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.vector.Vector;

/**
 * Calculates the standard error of a link-ratio for a given development period.
 * 
 * @see "Mack [1993]: Measuring the Variability of Chain Ladder Reserve Estimates"
 * @see org.jreserve.jrlib.scale.Scale
 * @see LinkRatioSECalculator
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSE extends Vector {
    
    /**
     * Returns Mack's sigma parameters, used for the calculation.
     */
    public LinkRatioScale getSourceLRScales();
    
    /**
     * Returns the input for the {@link LinkRatioScale LinkRatioScale}.
     */
    public LinkRatioScaleInput getSourceLrScaleInput();
    
    /**
     * Returns the LinkRatios used for this calculation.
     */
    public LinkRatio getSourceLinkRatios();
    
    /**
     * Returns the development factors used for this calculation.
     */
    public FactorTriangle getSourceFactors();
    
    /**
     * Returns the claims used for this calculation.
     */
    public ClaimTriangle getSourceTriangle();
}
