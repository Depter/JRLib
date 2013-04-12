package org.jrlib.claimratio;

import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangleInput;
import org.jrlib.util.AbstractSimpleMethodSelection;

/**
 * SimpleClaimRatio is a basic implementation for the 
 * {@link ClaimRatio ClaimRatio} interface. It is uses only one method
 * to calculate all claim ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleClaimRatio extends AbstractSimpleMethodSelection<RatioTriangle, ClaimRatioMethod> implements ClaimRatio {
    
    /**
     * Creates an instance for the given input, using the 
     * {@link DefaultCRMethod DefaultCRMethod} method.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
     */
    public SimpleClaimRatio(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(numerator, denominator, null);
    }
    
    /**
     * Creates an instance for the given input, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
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
        this(source, null);
    }
    
    /**
     * Creates an instance for the given source, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if `source` is null.
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
        this(source, null);
    }
    
    /**
     * Creates an instance for the given source, using the given method. If
     * `method` is null, the {@link DefaultCRMethod DefaultCRMethod} is used.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public SimpleClaimRatio(RatioTriangle source, ClaimRatioMethod method) {
        super(source, method==null? new DefaultCRMethod() : method);
    }

    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source;
    }
    
    @Override
    public RatioTriangleInput getSourceRatioTriangleInput() {
        return source.getSourceInput();
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
    }

    @Override
    public int getLength() {
        return source.getDevelopmentCount();
    }

    @Override
    public SimpleClaimRatio copy() {
        return new SimpleClaimRatio(source.copy(), estimatorMethod.copy());
    }
}