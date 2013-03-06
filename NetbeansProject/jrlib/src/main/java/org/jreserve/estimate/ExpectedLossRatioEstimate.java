package org.jreserve.estimate;

import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExpectedLossRatioEstimate extends AbstractEstimate {

    private LinkRatio lrs;
    private Triangle ciks;
    private Vector exposure;
    private Vector lossRatio;
    
    public ExpectedLossRatioEstimate(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        this.lrs = lrs;
        this.ciks = lrs.getInputFactors().getInputTriangle();
        this.exposure = exposure;
        this.lossRatio = lossRatio;
        checkInput();
        attachSources();
        doRecalculate();
    }
    
    private void checkInput() {
        if(ciks.getAccidentCount() > exposure.getLength())
            throw cikExposureMismatchException();
        if(ciks.getAccidentCount() > lossRatio.getLength())
            throw cikLossRatioMismatchException();
    }
    
    private IllegalArgumentException cikExposureMismatchException() {
        String msg = "Accident count in claims [%d] is more then the accident count in the exposure [%d]!";
        msg = String.format(msg, ciks.getAccidentCount(), exposure.getLength());
        return new IllegalArgumentException(msg);
    }
    
    private IllegalArgumentException cikLossRatioMismatchException() {
        String msg = "Accident count in claims [%d] is more then the accident count in the loss ratios [%d]!";
        msg = String.format(msg, ciks.getAccidentCount(), exposure.getLength());
        return new IllegalArgumentException(msg);
    }
    
    private void attachSources() {
        attachSource(lrs);
        attachSource(exposure);
        attachSource(lossRatio);
    }
    
    @Override
    protected void recalculateSource() {
        recalculateSource(lrs);
        recalculateSource(exposure);
        recalculateSource(lossRatio);
    }

    @Override
    protected void detachSource() {
        detachSource(lrs);
        detachSource(exposure);
        detachSource(lossRatio);
    }

    @Override
    public LinkRatio getSource() {
        return lrs;
    }

    @Override
    protected int getObservedDevelopmentCount(int accident) {
        return ciks.getDevelopmentCount(accident);
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initDimensions();
        fillValues();
    }
    
    private void initDimensions() {
        accidents = ciks.getAccidentCount();
        developments = lrs.getDevelopmentCount()+1;
        values = new double[accidents][developments];
    }
    
    private void fillValues() {
        double[] quotas = calculateQuotas();
        double[] ultimates = calculateUltimates();
        
        for(int a=0; a<accidents; a++) {
            int devs = ciks.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                values[a][d] = ciks.getValue(a, d);
            for(int d=devs; d<developments; d++)
                values[a][d] = ultimates[a] * quotas[d];
        }
    }
    
    private double[] calculateQuotas() {
        double[] quotas = new double[developments];
        
        quotas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = lrs.getValue(d);
            quotas[d] = (lr == 0d)? Double.NaN : quotas[d+1] / lr;
        }
        
        return quotas;
    }
    
    private double[] calculateUltimates() {
        double[] ultimates = new double[accidents];
        for(int a=0; a<accidents; a++)
            ultimates[a] = exposure.getValue(a) * lossRatio.getValue(a);
        return ultimates;
    }
}