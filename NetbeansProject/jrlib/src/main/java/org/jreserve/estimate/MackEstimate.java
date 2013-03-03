package org.jreserve.estimate;

import org.jreserve.AbstractCalculationData;
import org.jreserve.factor.FactorTriangle;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.standarderror.LinkRatioSE;
import org.jreserve.factor.standarderror.LinkRatioScale;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleUtil;
        
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackEstimate extends AbstractCalculationData<LinkRatioSE> implements Estimate {

    private int accidents;
    private int developments;
    private double[][] values;
    private double seRi[];
    private double seR;
    
    private Triangle ciks;
    private FactorTriangle fiks;
    private LinkRatio lrs;
    private LinkRatioScale scales;
    
    public MackEstimate(LinkRatioSE source) {
        super(source);
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
    public double getReserve(int accident) {
        int dLast = ciks.getDevelopmentCount(accident)-1;
        double lastObserved = getValue(accident, dLast);
        double lastEstimated = getValue(accident, developments-1);
        return lastEstimated - lastObserved;
    }

    @Override
    public double[] toArrayReserve() {
        double[] result = new double[accidents];
        for(int a=0; a<accidents; a++)
            result[a] = getReserve(a);
        return result;
    }

    @Override
    public double getReserve() {
        double sum = 0d;
        for(int a=0; a<accidents; a++) {
            double r = getReserve(a);
            if(!Double.isNaN(r))
                sum += r;
        }
        return sum;
    }
    
    public double getStandardError(int accident) {
        if(accident < 0 || accident >= accidents)
            return Double.NaN;
        return seRi[accident];
    }
    
    public double[] toArrayStandardError() {
        return TriangleUtil.copy(seRi);
    }
    
    public double getStandardError() {
        return seR;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initState();
        values = EstimateUtil.completeTriangle(ciks, lrs);
        calculateSeRi();
        calculateSeR();
    }
    
    private void initState() {
        scales = source.getLinkRatioScales();
        lrs = scales.getLinkRatios();
        fiks = lrs.getInputFactors();
        ciks = fiks.getInputTriangle();
        accidents = ciks.getAccidentCount();
        developments = lrs.getDevelopmentCount() + 1;
    }
    
    private void calculateSeRi() {
        seRi = new double[accidents];
        for(int a=0; a<accidents; a++)
            seRi[a] = calculateSeRi(a);
    }
    
    private double calculateSeRi(int accident) {
        double se = 0d;
        
        for(int d=ciks.getDevelopmentCount(accident); d<developments; d++) {
            //seR(i, k+1) = (C(i,k)^2 * (sigma(k)^2/C(i,k) + seF(k)^2) + (seR(i,k) * lr(k))^2) ^ 0.5
            double v = square(values[accident][d-1]);
            double seFik = square(scales.getValue(d-1)) / values[accident][d-1];
            v *= (seFik + square(source.getValue(d-1)));
            v += square(se * lrs.getValue(d-1));
            se = Math.sqrt(v);
        }
        
        return se;
    }
    
    private double square(double v) {
        return v * v;
    }
    
    private void calculateSeR() {
        seR = 0d;
        
        for(int d=1; d<developments; d++) { 
            double sCik = sumCikLast(d-1);
            //seR(i,k+1) = ((seR(i,k)*lr(k))^2 + sCik * sigma(k)^2 + (sCik * seF(k))^2)^0.5
            double sigma = square(scales.getValue(d-1));
            double seF = source.getValue(d-1);
            seR = square(seR * lrs.getValue(d-1)) + sCik*sigma + square(sCik * seF);
            seR = Math.sqrt(seR);
        }
    }
    
    private double sumCikLast(int development) {
        double sum = 0d;
        for(int a=0; a<accidents; a++) {
            int last = ciks.getDevelopmentCount(a)-1;
            if(development >= last)
                sum += values[a][development];
        }
        return sum;
    }
}
