package org.jreserve.estimate;

import org.jreserve.AbstractCalculationData;
import org.jreserve.CalculationData;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractEstimate<T extends CalculationData> extends AbstractCalculationData<T> implements Estimate {
    
    protected int accidents;
    protected int developments;
    protected double[][] values;
    
    protected AbstractEstimate() {
    }
    
    protected AbstractEstimate(T source) {
        super(source);
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
    
    protected boolean withinBounds(int accident, int development) {
        return accident >= 0 && accident < accidents &&
               development >= 0 && development < developments;
    }

    @Override
    public double[][] toArray() {
        return TriangleUtil.copy(values);
    }

    @Override
    public double getReserve(int accident) {
        int dLast = getObservedDevelopmentCount(accident)-1;
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
}
