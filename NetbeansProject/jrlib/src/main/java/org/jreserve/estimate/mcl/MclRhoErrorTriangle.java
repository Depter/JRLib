package org.jreserve.estimate.mcl;

import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclRhoErrorTriangle extends AbstractTriangle<Triangle> {

    private MclRho rhos;
    private Triangle numerator;
    private Triangle denominator;
    
    private int accidents;
    private int developments;
    private double[][] values;
    
    public MclRhoErrorTriangle(MclRho rhos) {
        this.rhos = rhos;
        this.numerator = rhos.getNumerator();
        this.denominator = rhos.getDenominator();
        doRecalculate();
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
        if(withinBounds(accident))
            return values[accident].length;
        return 0;
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        accidents = Math.min(numerator.getAccidentCount(), denominator.getAccidentCount());
        developments = Math.min(numerator.getDevelopmentCount(), denominator.getDevelopmentCount());
        values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            values[a] = recalculateAccident(a);
    }
    
    private double[] recalculateAccident(int accident) {
        int devs = Math.min(numerator.getDevelopmentCount(accident), denominator.getDevelopmentCount(accident));
        double[] errors = new double[devs];
        for(int d=0; d<devs; d++)
            errors[d] = calculateError(accident, d);
        return errors;
    }
    
    private double calculateError(int accident, int development) {
        double n = numerator.getValue(accident, development);
        double d = denominator.getValue(accident, development);
        double ratio = rhos.getRatio(development);
        double rho = rhos.getRho(development);
        return (Math.sqrt(d) * (n / d - ratio)) / rho;
    }
}
