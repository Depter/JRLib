package org.jreserve.estimate;

import org.jreserve.AbstractCalculationData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChainLadderEstimate extends AbstractCalculationData<LinkRatio> implements Estimate {

    private int accidents;
    private int developments;
    private double[][] values;
    
    private Triangle ciks;

    public ChainLadderEstimate(LinkRatio source) {
        super(source);
        ciks = source.getInputFactors().getInputTriangle();
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
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return accident >= 0 && accident < accidents &&
               development >= 0 && development < developments;
    }

    @Override
    public double[][] toArray() {
        double[][] result = new double[accidents][developments];
        for(int a=0; a<accidents; a++)
            System.arraycopy(values[a], 0, result[a], 0, developments);
        return result;
    }

    @Override
    public double getReserve(int accident) {
        int dLast = ciks.getDevelopmentCount(accident)-1;
        double lastObserved = getValue(accident, dLast);
        double lastEstimated = getValue(accident, developments-1);
        return lastEstimated - lastObserved;
    }

    @Override
    public double getReserve() {
        double sum = 0d;
        for(int a=0; a<accidents; a++)
            sum += getReserve(a);
        return sum;
    }

    @Override
    public double[] toArrayReserve() {
        double[] result = new double[accidents];
        for(int a=0; a<accidents; a++)
            result[a] = getReserve(a);
        return result;
    }


    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        accidents = ciks.getAccidentCount();
        developments = source.getDevelopmentCount()+1;
        values = EstimateUtil.completeTriangle(ciks, source);
    }
}
