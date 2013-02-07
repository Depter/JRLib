package org.jreserve.factor;

import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DevelopmentFactors extends AbstractTriangle {
    
    private double[][] factors;
    private int accidents;
    private int developments;
    private boolean cummulateInput;

    public DevelopmentFactors(Triangle source, boolean cummulateInput) {
        super(source);
        this.cummulateInput = cummulateInput;
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
            return factors[accident].length;
        return 0;
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return factors[accident][development];
        return Double.NaN;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        recalculateDevelopments();
        recalculateAccidents();
        if(accidents > 0 && developments > 0) {
            recalculateFactors();
        } else {
            factors = new double[0][];
        }
    }
    
    private void recalculateDevelopments() {
        developments = source==null? 0 : source.getDevelopmentCount() - 1;
        if(developments < 0)
            developments = 1;
    }
    
    private void recalculateAccidents() {
        if(developments == 0 || source==null) {
            accidents = 0;
        } else {
            accidents = source.getAccidentCount();
            while(accidents>0 && source.getDevelopmentCount(accidents-1) < 2)
                accidents--;
        }
    }
    
    private void recalculateFactors() {
        double[][] input = getSourceData();
        factors = new double[accidents][];
        for(int a=0; a<accidents; a++)
            factors[a] = calculateFactors(input[a]);
    }
    
    private double[][] getSourceData() {
        double[][] result = source==null? new double[0][] : source.toArray();
        if(cummulateInput)
            TriangleUtil.cummulate(result);
        return result;
    }
    
    private double[] calculateFactors(double[] input) {
        int size = Math.max(input.length-1, 0);
        double[] result = new double[size];
        for(int d=0; d<size; d++) {
            double denom = input[d];
            result[d] = (denom==0d)? Double.NaN : input[d+1]/input[d];
        }
        return result;
    }
}