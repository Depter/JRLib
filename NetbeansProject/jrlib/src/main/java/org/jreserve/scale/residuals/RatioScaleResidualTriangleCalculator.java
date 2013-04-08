package org.jreserve.scale.residuals;

import org.jreserve.scale.RatioScale;
import org.jreserve.scale.RatioScaleInput;
import org.jreserve.triangle.AbstractTriangle;
import static java.lang.Math.sqrt;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RatioScaleResidualTriangleCalculator<T extends RatioScaleInput> extends AbstractTriangle<RatioScale<T>> implements RatioScaleResidualTriangle<T> {
    
    private T input;
    private int accidents;
    private int developments;
    private double[][] values;
    
    public RatioScaleResidualTriangleCalculator(RatioScale<T> source) {
        super(source);
        this.input = source.getSourceInput();
        doRecalculate();
    }

    @Override
    public RatioScale<T> getSourceRatioScales() {
        return source;
    }
    
    @Override
    public T getSourceInput() {
        return input;
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
        return withinBounds(accident)?
                values[accident].length :
                0;
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
        initCalculation();
        for(int a=0; a<accidents; a++)
            values[a] = calculateAccident(a);
    }

    private void initCalculation() {
        accidents = input.getAccidentCount();
        developments = input.getDevelopmentCount();
        values = new double[accidents][];
    }

    private double[] calculateAccident(int accident) {
        int devs = input.getDevelopmentCount(accident);
        double[] errs = new double[devs];
        for(int d=0; d<devs; d++)
            errs[d] = calculateError(accident, d);
        return errs;
    }

    private double calculateError(int accident, int development) {
        double wik = input.getWeight(accident, development);
        double rik = input.getRatio(accident, development);
        double rk = input.getRatio(development);
        double scale = source.getValue(development);
        return sqrt(wik) * (rik - rk) / scale;
    }
}
