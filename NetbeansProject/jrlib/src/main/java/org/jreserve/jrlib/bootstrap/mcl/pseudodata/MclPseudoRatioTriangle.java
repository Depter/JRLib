package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import org.jreserve.jrlib.claimratio.ClaimRatio;
import org.jreserve.jrlib.claimratio.scale.ClaimRatioScale;
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle;
import org.jreserve.jrlib.triangle.AbstractTriangle;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangle;
import org.jreserve.jrlib.triangle.ratio.RatioTriangleInput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclPseudoRatioTriangle extends AbstractTriangle<RatioTriangleInput> implements RatioTriangle {
    
    static MclPseudoRatioTriangle createPaid(MclResidualBundle bundle) {
        return new MclPseudoRatioTriangle(bundle, true);
    }
    
    static MclPseudoRatioTriangle createIncurred(MclResidualBundle bundle) {
        return new MclPseudoRatioTriangle(bundle, false);
    }
    
    private boolean isPaid;
    private int accidents;
    private int developments;
    private double[][] pseudoValues;
    private double[][] wik;
    private double[] crs;
    private double[] scales;

    private MclPseudoRatioTriangle(MclResidualBundle bundle, boolean isPaid) {
        this.isPaid = isPaid;
        initState(bundle);
        detach();
        setCallsForwarded(false);
    }
    
    private void initState(MclResidualBundle bundle) {
        initSource(bundle);
        initRatios(getSourceRatioTriangle(bundle));
        initWeights(getSourceWeightTriangle(bundle));
        initClaimRatios(bundle);
        initScales(bundle);
    }
    
    private void initSource(MclResidualBundle bundle) {
        CRResidualTriangle t = getSourceResidualss(bundle);
        ClaimTriangle n = t.getSourceNumeratorTriangle();
        ClaimTriangle d = t.getSourceDenominatorTriangle();
        this.source = new RatioTriangleInput(n, d);
        this.source.detach();
    }
    
    private RatioTriangle getSourceRatioTriangle(MclResidualBundle bundle) {
        return getSourceResidualss(bundle).getSourceRatioTriangle();
    }
    
    private CRResidualTriangle getSourceResidualss(MclResidualBundle bundle) {
        return isPaid? 
                bundle.getSourcePaidCRResidualTriangle() :
                bundle.getSourceIncurredCRResidualTriangle();
    }
    
    private void initRatios(RatioTriangle ratios) {
        accidents = ratios.getAccidentCount();
        developments = ratios.getDevelopmentCount();
        pseudoValues = ratios.toArray();
    }
    
    private ClaimTriangle getSourceWeightTriangle(MclResidualBundle bundle) {
        return getSourceResidualss(bundle).getSourceDenominatorTriangle();
    }
    
    private void initWeights(ClaimTriangle cik) {
        wik = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = pseudoValues[a].length;
            wik[a] = new double[devs];
            for(int d=0; d<devs; d++)
                wik[a][d] = Math.sqrt(cik.getValue(a, d));
        }
    }
    
    private void initClaimRatios(MclResidualBundle bundle) {
        ClaimRatio ratios = getSourceResidualss(bundle).getSourceClaimRatios();
        this.crs = new double[developments];
        for(int d=0; d<developments; d++)
            this.crs[d] = ratios.getValue(d);
    }
    
    private void initScales(MclResidualBundle bundle) {
        ClaimRatioScale sigma = getSourceResidualss(bundle).getSourceClaimRatioScales();
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = sigma.getValue(d);
    }
    
    @Override
    public RatioTriangleInput getSourceInput() {
        return source;
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
    public void setSource(RatioTriangleInput source) {
        throw new UnsupportedOperationException("Can not set source on a pseudo triangle!");
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
        return withinBounds(accident)? pseudoValues[accident].length : 0;
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                pseudoValues[accident][development] : 
                Double.NaN;
    }
    
    void setValueAt(int accident, int development, MclResidualCell cell) {
        double r = getResidualFromCell(cell);
        double cr = crs[development];
        double s = scales[development];
        double w = wik[accident][development];
        pseudoValues[accident][development] = cr + r * s / w;
    }
    
    private double getResidualFromCell(MclResidualCell cell) {
        if(cell == null)
            return Double.NaN;
        else if(isPaid)
            return cell.getPaidCRResidual();
        else
            return cell.getIncurredCRResidual();
    }

    @Override
    protected void recalculateLayer() {
        throw new UnsupportedOperationException("Do not call recalculate on an MclPseudoTriangle!");
    }
}
