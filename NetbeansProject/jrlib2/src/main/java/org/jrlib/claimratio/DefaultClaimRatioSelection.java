package org.jrlib.claimratio;

import org.jrlib.triangle.TriangleUtil;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.ratio.DefaultRatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangle;
import org.jrlib.triangle.ratio.RatioTriangleInput;
import org.jrlib.util.AbstractMethodSelection;

/**
 * DefaultClaimRatioSelection allows to use different 
 * {@link ClaimRatioMethod ClaimRatioMethods} for different development
 * periods.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultClaimRatioSelection extends AbstractMethodSelection<RatioTriangle, ClaimRatioMethod> implements ClaimRatioSelection {
    
    private int developments;
    private double[] values;
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null.
     */
    public DefaultClaimRatioSelection(ClaimTriangle numerator, ClaimTriangle denominator) {
        this(numerator, denominator, new DefaultCRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioMethod LinkRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if any of the parameters is null.
     */
    public DefaultClaimRatioSelection(ClaimTriangle numerator, ClaimTriangle denominator, ClaimRatioMethod defaultMethod) {
        this(new DefaultRatioTriangle(numerator, denominator), defaultMethod);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangleInput source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioMethod LinkRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangleInput source, ClaimRatioMethod defaultMethod) {
        this(new DefaultRatioTriangle(source), defaultMethod);
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * {@link DefaultCRMethod DefaultCRMethod} as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangle source) {
        this(source, new DefaultCRMethod());
    }
    
    /**
     * Creates a instance for the given input, which uses the 
     * supplied {@link LinkRatioMethod LinkRatioMethod}
     * as it's default method.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws NullPointerException if `defaultMethod` is null.
     */
    public DefaultClaimRatioSelection(RatioTriangle source, ClaimRatioMethod defaultMethod) {
        super(source, defaultMethod);
        developments = (source==null)? 0 : source.getDevelopmentCount();
        doRecalculate();
    }
    
    private DefaultClaimRatioSelection(DefaultClaimRatioSelection original) {
        super(original.source.copy(), original);
        this.developments = original.developments;
        if(developments > 0)
            this.values = TriangleUtil.copy(original.values);
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
     * Length of the link-ratios is equals to the number of
     * development periods in the triangle of development factors.
     * 
     * @see FactorTriangle#getDevelopmentCount().
     */
    @Override
    public int getLength() {
        return developments;
    }

    @Override
    public double getValue(int development) {
        return (0 <= development && development < developments)? 
                values[development] : 
                Double.NaN;
    }

    @Override
    public double[] toArray() {
        return TriangleUtil.copy(values);
    }

    @Override
    public DefaultClaimRatioSelection copy() {
        return new DefaultClaimRatioSelection(this);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        developments = source.getDevelopmentCount();
        super.fitMethods();
        values = super.getFittedValues(developments);
    }
}
