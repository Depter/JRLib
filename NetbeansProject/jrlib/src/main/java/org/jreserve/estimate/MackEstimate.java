package org.jreserve.estimate;

import org.jreserve.AbstractCalculationData;
import org.jreserve.CalculationData;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.standarderror.LinkRatioScale;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackEstimate extends AbstractCalculationData<LinkRatioScale> implements Estimate {

    private int accidents;
    private int developments;
    private double[][] values;

    public MackEstimate(LinkRatioScale source) {
        super(source);
        doRecalculate();
    }
    
    public int getAccidentCount() {
        return accidents;
    }

    public int getDevelopmentCount() {
        return developments;
    }

    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return accident >= 0 && accident < accidents &&
               development >= 0 && development < developments;
    }

    public double[][] toArray() {
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }
    
    private double[] toArray(int accident) {
        double[] result = new double[developments];
        System.arraycopy(values[accident], 0, result, 0, developments);
        return result;
    }


    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        LinkRatio lrs = source.getLinkRatios();
        FactorTriangle dfs = lrs.getInputFactors();
        Triangle cik = dfs.getInputTriangle();
        
        accidents = cik.getAccidentCount();
        developments = lrs.getDevelopmentCount() + 1;
        values = EstimateUtil.completeTriangle(cik, lrs);
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    private double[] calculateSeFik(Triangle cik) {
        
        return null;
    }
    
    private double sumCikAlpha(Triangle cik, int development) {
        return Double.NaN;
    }
}
