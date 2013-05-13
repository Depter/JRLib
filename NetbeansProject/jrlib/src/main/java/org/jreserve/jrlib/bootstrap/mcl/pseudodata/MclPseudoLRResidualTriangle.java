package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 */
public class MclPseudoLRResidualTriangle extends AbstractTriangle<LinkRatioScale> implements LRResidualTriangle {
    
    static MclPseudoLRResidualTriangle createPaid(MclResidualBundle bundle) {
        return new MclPseudoLRResidualTriangle(bundle, true);
    }
    
    static MclPseudoLRResidualTriangle createIncurred(MclResidualBundle bundle) {
        return new MclPseudoLRResidualTriangle(bundle, false);
    }
    
    private static LinkRatioScale getLinkRatioScales(MclResidualBundle bundle, boolean isPaid) {
        LRResidualTriangle s = isPaid? 
                bundle.getSourcePaidLRResidualTriangle() : 
                bundle.getSourceIncurredLRResidualTriangle();
        return s.getSourceLinkRatioScales();
    }
    
    private boolean isPaid;
    private int accidents;
    private int developments;
    private double[][] residuals;
    
    private MclPseudoLRResidualTriangle(MclResidualBundle bundle, boolean isPaid) {
        super(getLinkRatioScales(bundle, isPaid));
        this.isPaid = isPaid;
        initState(bundle);
        detach();
        super.setCallsForwarded(false);
    }
    
    private void initState(MclResidualBundle bundle) {
        accidents = bundle.getAccidentCount();
        developments = bundle.getDevelopmentCount();
        residuals = new double[accidents][];
        for(int a=0; a<accidents; a++)
            residuals[a] = new double[bundle.getDevelopmentCount(a)];
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
    public LinkRatioScale getSourceLinkRatioScales() {
        return source;
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
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
    protected void recalculateLayer() {
    }

}
