package org.jreserve.estimate;

import org.jreserve.CalculationData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BornhuetterFergusonEstimate extends AbstractEstimate<CalculationData> {

    private final static int LRS = 0;
    private final static int EXPOSURES = 1;
    private final static int LOSS_RATIOS = 2;
    
    private LinkRatio lrs;
    private Triangle ciks;
    private Vector exposure;
    private Vector lossRatio;
    
    public BornhuetterFergusonEstimate(LinkRatio lrs, Vector exposure, Vector lossRatio) {
        super(lrs, exposure, lossRatio);
        initState();
        checkInput();
        doRecalculate();
    }
    
    private void initState() {
        this.lrs = (LinkRatio) sources[LRS];
        this.ciks = lrs.getSourceTriangle();
        this.exposure = (Vector) sources[EXPOSURES];
        this.lossRatio = (Vector) sources[LOSS_RATIOS];
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

    public LinkRatio getSourceLinkRatios() {
        return lrs;
    }
    
    public Vector getSourceExposure() {
        return exposure;
    }
    
    public Vector getSourceLossRatios() {
        return lossRatio;
    }

    @Override
    public int getObservedDevelopmentCount(int accident) {
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
            
            double prev = (devs > 0)? values[a][devs-1] : Double.NaN;
            for(int d=devs; d<developments; d++) {
                prev += ultimates[a] * quotas[d];
                values[a][d] = prev;
            }
        }
    }
    
    private double[] calculateQuotas() {
        double[] quotas = new double[developments];
        
        quotas[developments-1] = 1d;
        for(int d=(developments-2); d>=0; d--) {
            double lr = lrs.getValue(d);
            quotas[d] = (lr == 0d)? Double.NaN : quotas[d+1] / lr;
            quotas[d+1] = quotas[d+1] - quotas[d];
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
