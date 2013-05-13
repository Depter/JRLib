package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclPseudoCRResidualTriangle 
    extends AbstractTriangle<ClaimRatioScale> 
    implements CRResidualTriangle {
    
    static MclPseudoCRResidualTriangle createPaid(MclResidualBundle bundle) {
        return new MclPseudoCRResidualTriangle(bundle, true);
    }
    
    static MclPseudoCRResidualTriangle createIncurred(MclResidualBundle bundle) {
        return new MclPseudoCRResidualTriangle(bundle, false);
    }
    
    private static ClaimRatioScale getLinkRatioScales(MclResidualBundle bundle, boolean isPaid) {
        CRResidualTriangle s = isPaid? 
                bundle.getSourcePaidCRResidualTriangle() : 
                bundle.getSourceIncurredCRResidualTriangle();
        return s.getSourceClaimRatioScales();
    }

    private boolean isPaid;
    private int accidents;
    private int developments;
    private double[][] residuals;
    
    private MclPseudoCRResidualTriangle(MclResidualBundle bundle, boolean isPaid) {
        super(getLinkRatioScales(bundle, isPaid));
        detach();
        super.setCallsForwarded(false);
        
        this.isPaid = isPaid;
        initState(bundle);
    }
    
    private void initState(MclResidualBundle bundle) {
        initDimensions(bundle);
        CRResidualTriangle t = isPaid? bundle.getSourcePaidCRResidualTriangle() : bundle.getSourceIncurredCRResidualTriangle();
        residuals = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = bundle.getDevelopmentCount(a);
            residuals[a] = initResiduals(accidents, devs, t);
        }
    }
    
    private void initDimensions(MclResidualBundle bundle) {
        accidents = bundle.getAccidentCount();
        developments = bundle.getDevelopmentCount();
    }
    
    private double[] initResiduals(int accident, int devs, CRResidualTriangle t) {
        double[] res = new double[devs];
        for(int d=0; d<devs; d++)
            res[d] = t.getValue(accident, d);
        return res;
    }
    
    void setValueAt(int accident, int development, MclResidualCell cell) {
        double r = getResidualFromCell(cell);
        residuals[accident][development] = r;
    }
    
    private double getResidualFromCell(MclResidualCell cell) {
        if(cell == null)
            return Double.NaN;
        else if(isPaid)
            return cell.getPaidLRResidual();
        else
            return cell.getIncurredLRResidual();
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
        return withinBounds(accident)? residuals[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                residuals[accident][development] :
                Double.NaN;
    }

    @Override
    public ClaimRatioScale getSourceClaimRatioScales() {
        return source;
    }

    @Override
    public ClaimRatio getSourceClaimRatios() {
        return source.getSourceClaimRatios();
    }

    @Override
    public RatioTriangle getSourceRatioTriangle() {
        return source.getSourceRatioTriangle();
    }

    @Override
    public ClaimTriangle getSourceNumeratorTriangle() {
        return source.getSourceNumeratorTriangle();
    }

    @Override
    public ClaimTriangle getSourceDenominatorTriangle() {
        return source.getSourceDenominatorTriangle();
    }
    
    @Override
    protected void recalculateLayer() {
    }
}
