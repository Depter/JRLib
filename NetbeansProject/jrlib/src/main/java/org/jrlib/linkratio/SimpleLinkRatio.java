package org.jrlib.linkratio;

import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.DevelopmentFactors;
import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * This class will calculate link-ratios for all development
 * periods with the same {@link LinkRatioMethod method}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatio extends AbstractSimpleMethodSelection<FactorTriangle, LinkRatioMethod> implements LinkRatio {
    
    /**
     * Creates an instance for the given source, using the
     * {@link WeightedAverageLRMethod WeightedAverageLRMethod}.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleLinkRatio(ClaimTriangle source) {
        this(new DevelopmentFactors(source), null);
    }
    
    /**
     * Creates an instance for the given source, using the
     * suplied method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `method` is null.
     */
    public SimpleLinkRatio(ClaimTriangle source, LinkRatioMethod method) {
        this(new DevelopmentFactors(source), method);
    }
    
    /**
     * Creates an instance for the given source, using the
     * {@link WeightedAverageLRMethod WeightedAverageLRMethod}.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleLinkRatio(FactorTriangle source) {
        this(source, null);
    }
    
    /**
     * Creates an instance for the given source, using the
     * suplied method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `method` is null.
     */
    public SimpleLinkRatio(FactorTriangle source, LinkRatioMethod method) {
        super(source, method==null? new WeightedAverageLRMethod() : method);
    }
    
    /**
     * Returns the number of development periods from the source.
     * 
     * @see FactorTriangle#getAccidentCount().
     */
    @Override
    public int getLength() {
        return source.getDevelopmentCount();
    }
    
    /**
     * Does nothing.
     */
    @Override
    protected void initCalculation() {
    }
    
    /**
     * Retunrs the source factor triangle, used for this
     * calculation.
     */
    @Override
    public FactorTriangle getSourceFactors() {
        return source;
    }

    /**
     * Retunrs the source claim triangle, used for this
     * calculation.
     */
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    /**
     * Returns Mack's alpha parameter for the given 
     * development period.
     * 
     * @see LinkRatioMethod#getMackAlpha().
     */
    @Override
    public double getMackAlpha(int development) {
        return defaultMethod.getMackAlpha();
    }
    
    /**
     * Returns the weight for the given accident and 
     * development period used to calculate the link-ratios.
     * 
     * @see LinkRatioMethod#getWeight(int, int).
     */
    @Override
    public double getWeight(int accident, int development) {
        return defaultMethod.getWeight(accident, development);
    }
}
