package org.jreserve.estimate.mcl2;

import org.jreserve.AbstractCalculationData;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLambdaCalculator extends AbstractCalculationData<LambdaCalculatorInput> implements LambdaCalculator {

    private double lambda;
    
    public DefaultLambdaCalculator(LambdaCalculatorInput source) {
        super(source);
        doRecalculate();
    }
    
    @Override
    public LambdaCalculatorInput getSourceInput() {
        return source;
    }

    @Override
    public double getLambda() {
        return lambda;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        Triangle lrResiduals = source.getSourceLRResiduals();
        Triangle ratioResiduals = source.getSourceRatioResiduals();
        
        double sumLrR = 0d;
        double sumRR = 0d;
        
        int accidents = getAccidents(lrResiduals, ratioResiduals);
        for(int a=0; a<accidents; a++) {
            int devs = getDevelopments(lrResiduals, ratioResiduals, a);
            for(int d=0; d<devs; d++) {
                double lr = lrResiduals.getValue(a, d);
                double r = ratioResiduals.getValue(a, d);
                
                if(!Double.isNaN(lr) && !Double.isNaN(r)) {
                    sumLrR += lr * r;
                    sumRR += r * r;
                }
            }
        }
        
        lambda = sumLrR / sumRR;
    }
    
    private int getAccidents(Triangle t1, Triangle t2) {
        int a1 = t1.getAccidentCount();
        int a2 = t2.getAccidentCount();
        return (a1 < a2)? a1 : a2;
    }
    
    private int getDevelopments(Triangle t1, Triangle t2, int accident) {
        int d1 = t1.getDevelopmentCount(accident);
        int d2 = t2.getDevelopmentCount(accident);
        return (d1 < d2)? d1 : d2;
    }
}
