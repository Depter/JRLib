package org.jreserve.estimate.mcl;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;
import org.jreserve.util.AbstractMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultMclRhoSelection extends AbstractMethodSelection<MclRho, MclRhoEstimator> implements MclRhoSelection {

    private LinkRatio numerator;
    private LinkRatio denominator;
    private int developments;
    private double[] ratios;
    private double[] rhos;
    
    public DefaultMclRhoSelection(LinkRatio numerator, LinkRatio denominator) {
        this(numerator, denominator, null);
    }
    
    public DefaultMclRhoSelection(LinkRatio numerator, LinkRatio denominator, MclRhoEstimator defaultMethod) {
        super(new MclRhoCalculator(numerator.getSourceTriangle(), denominator.getSourceTriangle()), 
              defaultMethod==null? new EmptyMclRhoEstimator() : defaultMethod);
        this.numerator = numerator;
        this.denominator = denominator;
        doRecalculate();
    }

    @Override
    public MclRho getSourceRhos() {
        return source;
    }
    
    @Override
    public Triangle getNumerator() {
        return source.getNumerator();
    }

    @Override
    public Triangle getDenominator() {
        return source.getDenominator();
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public double getRatio(int development) {
        if(withinBound(development))
            return ratios[development];
        return Double.NaN;
    }
    
    private boolean withinBound(int development) {
        return 0 <= development &&
               development < developments;
    }

    @Override
    public double getRho(int development) {
        if(withinBound(development))
            return rhos[development];
        return Double.NaN;
    }

    @Override
    public double[] ratiosToArray() {
        return TriangleUtil.copy(ratios);
    }

    @Override
    public double[] rhosToArray() {
        return TriangleUtil.copy(rhos);
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        initState();
        calculateRatios();
        calculateRhos();
    }
    
    private void initState() {
        developments = numerator.getDevelopmentCount();
        ratios = new double[developments];
        rhos = new double[developments];
    }
    
    private void calculateRatios() {
        for(int d=0; d<developments; d++) {
            double ratio = source.getRatio(d);
            ratios[d] = (Double.isNaN(ratio))? calculateRatio(d) : ratio;
        }
    }
    
    private double calculateRatio(int development) {
        if(development == 0)
            return Double.NaN;
        development--;
        
        double prev = ratios[development];
        if(Double.isNaN(prev))
            return Double.NaN;
        
        double lrN = numerator.getValue(development);
        if(Double.isNaN(lrN))
            return Double.NaN;
        
        double lrD = denominator.getValue(development);
        if(Double.isNaN(lrD) || lrD == 0d)
            return Double.NaN;
        
        return prev * lrN / lrD;
    }
    
    private void calculateRhos() {
        super.fitMethods();
        rhos = super.getFittedValues(developments);
    }
}
