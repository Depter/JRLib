package org.jreserve.jrlib.claimratio;

import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 * SimpleClaimRatio is a basic implementation for the 
 * {@link ClaimRatio ClaimRatio} interface. It is uses only one method
 * to calculate all claim ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleClaimRatio extends AbstractSimpleMethodSelection<ClaimRatio, ClaimRatioMethod> implements ClaimRatio {
    
    private int developments;
    
    /**
     * Creates an instance for the given input, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
     */
    public SimpleClaimRatio(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(numerator, denominator, new DefaultCRMethod());
    }
    
    /**
     * Creates an instance for the given input, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(ClaimTriangle numerator, ClaimTriangle denominator, ClaimRatioMethod method) {
        this(new DefaultRatioTriangle(numerator, denominator), method);
    }
    
    /**
     * Creates an instance for the given source, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatio(RatioTriangleInput source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates an instance for the given source, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(RatioTriangleInput source, ClaimRatioMethod method) {
        this(new DefaultRatioTriangle(source), method);
    }
    
    /**
     * Creates an instance for the given source, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatio(RatioTriangle source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates an instance for the given source, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(RatioTriangle source, ClaimRatioMethod method) {
        this(new ClaimRatioCalculator(source), method);
    }
    
    /**
     * Creates an instance for the given source, using the given method to
     * fill NaN values.
     * 
     * @throws NullPointerException if one of the parameters is null.
     */
    public SimpleClaimRatio(ClaimRatio source, ClaimRatioMethod method) {
        super(source, 
              (method instanceof DefaultCRMethod)? method : new DefaultCRMethod(),
              method);
        super.recalculateLayer();
    }
    
    @Override
    public void setSource(RatioTriangle ratioTriangle) {
        source.setSource(ratioTriangle);
    }
    
    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }
    
    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return source.getSourceRatioTriangleInput();
    }


    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    /**
     * Does nothing.
     */
    @Override
    protected void initCalculation() {
        developments = source.getLength();
    }

    @Override
    public int getLength() {
        return developments;
    }
}