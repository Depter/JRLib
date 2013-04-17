package org.jrlib.bootstrap.mcl.pseudodata;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jrlib.triangle.AbstractTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclPseudoFactorTriangle extends AbstractTriangle<ClaimTriangle> implements FactorTriangle {
    
    static MclPseudoFactorTriangle createPaid(MclResidualBundle bundle) {
        return new MclPseudoFactorTriangle(bundle, true);
    }
    
    static MclPseudoFactorTriangle createIncurred(MclResidualBundle bundle) {
        return new MclPseudoFactorTriangle(bundle, false);
    }
    
    private boolean isPaid;
    
    private int accidents;
    private int developments;
    private double[][] pseudoValues;
    private double[][] wik;
    private double[] lrs;
    private double[] scales;
    
    private MclPseudoFactorTriangle(MclResidualBundle bundle, boolean isPaid) {
        this.isPaid = isPaid;
        source = getSourceResidualss(bundle).getSourceTriangle();
        initState(bundle);
        detach();
        super.setCallsForwarded(false);
    }

    private void initState(MclResidualBundle bundle) {
        initFactors(bundle);
        LinkRatio linkRatios = getLinkRatio(bundle);
        initWeights(linkRatios);
        initLinkRatios(linkRatios);
        initScales(bundle);
    }
    
    private LinkRatio getLinkRatio(MclResidualBundle bundle) {
        return getSourceResidualss(bundle).getSourceLinkRatios();
    }
    
    private LRResidualTriangle getSourceResidualss(MclResidualBundle bundle) {
        return isPaid? 
                bundle.getSourcePaidLRResidualTriangle() :
                bundle.getSourceIncurredLRResidualTriangle();
    }
    
    private void initFactors(MclResidualBundle bundle) {
        accidents = bundle.getAccidentCount();
        developments = bundle.getDevelopmentCount();
        pseudoValues = new double[accidents][];
        for(int a=0; a<accidents; a++)
            pseudoValues[a] = new double[bundle.getDevelopmentCount(a)];
    }
    
    private void initWeights(LinkRatio lrs) {
        wik = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = pseudoValues[a].length;
            wik[a] = new double[devs];
            for(int d=0; d<devs; d++)
                wik[a][d] = Math.sqrt(lrs.getWeight(a, d));
        }
    }
    
    private void initLinkRatios(LinkRatio lrs) {
        this.lrs = new double[developments];
        for(int d=0; d<developments; d++)
            this.lrs[d] = lrs.getValue(d);
    }
    
    private void initScales(MclResidualBundle bundle) {
        LinkRatioScale sigma = getLinkRatioScales(bundle);
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = sigma.getValue(d);
    }
    
    private LinkRatioScale getLinkRatioScales(MclResidualBundle bundle) {
        return getSourceResidualss(bundle).getSourceLinkRatioScales();
    }
    
    @Override
    public ClaimTriangle getSourceTriangle() {
        return source;
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

    @Override
    protected void recalculateLayer() {
    }
    
    void setValueAt(int accident, int development, MclResidualCell cell) {
        double r = getResidualFromCell(cell);
        double l = lrs[development];
        double s = scales[development];
        double w = wik[accident][development];
        pseudoValues[accident][development] = l + r * s / w;
    }
    
    private double getResidualFromCell(MclResidualCell cell) {
        if(cell == null)
            return Double.NaN;
        else if(isPaid)
            return cell.getPaidLRResidual();
        else
            return cell.getIncurredLRResidual();
    }
}
