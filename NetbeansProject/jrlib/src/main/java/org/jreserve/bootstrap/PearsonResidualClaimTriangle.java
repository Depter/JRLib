package org.jreserve.bootstrap;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class PearsonResidualClaimTriangle extends AbstractTriangle<LinkRatio> implements ResidualTriangle {
    
    private Triangle triangle;
    
    private int accidents;
    private int developments;
    private double[][] residuals;

    public PearsonResidualClaimTriangle(LinkRatio source) {
        super(source);
        this.triangle = source.getSourceTriangle();
        doRecalculate();
    }
    
    public LinkRatio getSourceLinkRatios() {
        return source;
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
        return withinBounds(accident)? residuals[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                residuals[accident][development] :
                Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initState();
        calculateFittedValues();
        calculatePearsonResiduals();
    }
    
    private void initState() {
        this.accidents = triangle.getAccidentCount();
        this.developments = triangle.getDevelopmentCount();
        this.residuals = new double[accidents][];
    }
    
    private void calculateFittedValues() {
        for(int a=0; a<accidents; a++)
            residuals[a] = calculateFittedValues(a);
        TriangleUtil.deCummulate(residuals);
    }
    
    private double[] calculateFittedValues(int accident) {
        int d = triangle.getDevelopmentCount(accident);
        double[] result = new double[d];
        
        if(d == 0) 
            return result;
        
        result[--d] = triangle.getValue(accident, d);
        while(--d >= 0)
            result[d] = result[d+1] / source.getValue(d);
        
        return result;
    }
    
    private void calculatePearsonResiduals() {
        for(int a=0; a<accidents; a++) {
            int devs = residuals[a].length;
            for(int d=0; d<devs; d++)
                calculatePearsonResidual(a, d);
        }
    }
    
    private void calculatePearsonResidual(int accident, int development) {
        double original = triangle.getValue(accident, development);
        if(development > 0)
            original -= triangle.getValue(accident, development-1);
        double fitted = residuals[accident][development];
        residuals[accident][development] = (original - fitted) / Math.sqrt(fitted);
    }
}
