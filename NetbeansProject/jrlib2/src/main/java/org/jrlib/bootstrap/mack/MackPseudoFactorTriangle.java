package org.jrlib.bootstrap.mack;

import java.util.Collections;
import java.util.List;
import org.jrlib.bootstrap.residualgenerator.DoubleResidualGenerator;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScale;
import org.jrlib.linkratio.scale.residuals.LRResidualTriangle;
import org.jrlib.triangle.AbstractTriangle;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.triangle.factor.FactorTriangle;
import org.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackPseudoFactorTriangle extends AbstractTriangle<ClaimTriangle> implements FactorTriangle {

    private DoubleResidualGenerator<LRResidualTriangle> residuals;
    private int accidents;
    private int developments;
    private double[][] pseudoValues;
    private double[][] wik;
    private double[] lrs;
    private double[] scales;

    public MackPseudoFactorTriangle(Random rnd, LRResidualTriangle residuals) {
        this(rnd, residuals, Collections.EMPTY_LIST);
    }
    
    public MackPseudoFactorTriangle(Random rnd, LRResidualTriangle residuals, List<int[][]> segments) {
        super(residuals.getSourceTriangle());
        this.residuals = new DoubleResidualGenerator<LRResidualTriangle>(rnd, residuals, segments);
        initState(residuals);
    }

    private void initState(LRResidualTriangle residuals) {
        initFactors(residuals.getSourceFactors());
        LinkRatio linkRatios = residuals.getSourceLinkRatios();
        initWeights(linkRatios);
        initLinkRatios(linkRatios);
        initScales(residuals.getSourceLinkRatioScales());
    }
    
    private void initFactors(FactorTriangle factors) {
        accidents = factors.getAccidentCount();
        developments = factors.getDevelopmentCount();
        pseudoValues = factors.toArray();
        
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
    
    private void initScales(LinkRatioScale scales) {
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = scales.getValue(d);
    }
    
    private void initLinkRatios(LinkRatio lrs) {
        this.lrs = new double[developments];
        for(int d=0; d<developments; d++)
            this.lrs[d] = lrs.getValue(d);
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
        doRecalculate();
    }
    
    private void doRecalculate() {
        for(int a=0; a<accidents; a++) {
            int devs = pseudoValues[a].length;
            for(int d=0; d<devs; d++)
                pseudoValues[a][d] = recalculatePseudoValue(a, d);
        }
    }
    
    private double recalculatePseudoValue(int accident, int development) {
        double l = lrs[development];
        double r = residuals.getValue(accident, development);
        double s = scales[development];
        double w = wik[accident][development];
        return l + r * s / w;
    }
}
