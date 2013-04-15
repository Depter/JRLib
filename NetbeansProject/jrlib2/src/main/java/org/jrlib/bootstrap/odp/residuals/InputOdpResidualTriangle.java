package org.jrlib.bootstrap.odp.residuals;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.triangle.AbstractTriangle;
import org.jrlib.triangle.TriangleUtil;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * InputOdpResidualTriangle calculates the adjusted pearson residuals for
 * a given set of link-ratios. The formula to calculate the residual
 * for accident period `a` and development period `d` is:
 *               o(a,d) - f(a,d)
 *     r(a,d) =  ---------------
 *                 f(a,d)^0.5
 * where:
 * -   `o(a,d)` is the original incremental value.
 * -   `f(a,d)` is teh fitted incremental value
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class InputOdpResidualTriangle extends AbstractTriangle<LinkRatio> implements OdpResidualTriangle {
    
    private ClaimTriangle cik;
    private int accidents;
    private int developments;
    private double[][] values;
    
    /**
     * Creates an instance for the given source.
     */
    public InputOdpResidualTriangle(LinkRatio source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return source;
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return withinBounds(accident)? values[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)? 
                values[accident][development] :
                Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        recalculateBounds();
        double[][] fitted = calculateFittedValues();
        TriangleUtil.deCummulate(values);
        calculateResiduals(fitted);
    }
    
    private void recalculateBounds() {
        cik = source.getSourceTriangle();
        accidents = cik.getAccidentCount();
        developments = cik.getDevelopmentCount();
        values = cik.toArray();
    }
    
    private double[][] calculateFittedValues() {
        double[][] fitted = TriangleUtil.copy(values);
        for(int a=0; a<accidents; a++) {
            int devs = values[a].length - 2;
            for(int d=devs; d>=0; d--)
                fitted[a][d] = fitted[a][d+1]/source.getValue(d);
        }
        TriangleUtil.deCummulate(fitted);
        return fitted;
    }
    
    private void calculateResiduals(double[][] fitted) {
        for(int a=0; a<accidents; a++) {
            int devs = values[a].length;
            for(int d=0; d<devs; d++) 
                values[a][d] = calculateResidual(values[a][d], fitted[a][d]);
        }
    }
    
    private double calculateResidual(double original, double fitted) {
        if(Double.isNaN(original) || Double.isNaN(fitted) || fitted <= 0d)
            return Double.NaN;
        return (original - fitted) / Math.sqrt(fitted);
    }
}
