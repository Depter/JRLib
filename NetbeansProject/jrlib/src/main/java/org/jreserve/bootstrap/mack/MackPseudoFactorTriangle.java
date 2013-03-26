package org.jreserve.bootstrap.mack;

import javax.swing.event.ChangeListener;
import org.jreserve.bootstrap.ResidualGenerator;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.claim.ClaimTriangle;
import org.jreserve.triangle.claim.InputTriangle;
import org.jreserve.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackPseudoFactorTriangle implements FactorTriangle {
    
    private final ResidualGenerator residuals;
    private final int accidents;
    private final int[] developments;
    private final double[][] values;
    private final double[] sigmas;
    private final double[] lrs;
    private final ClaimTriangle source;
    
    public MackPseudoFactorTriangle(LinkRatioScale scales, ResidualGenerator residuals) {
        //Residuals
        this.residuals = residuals;
        
        //Accident, developments, values
        FactorTriangle factors = scales.getSourceFactors();
        this.accidents = factors.getAccidentCount();
        this.developments = new int[accidents];
        this.values = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = factors.getDevelopmentCount(a);
            developments[a] = devs;
            values[a] = new double[devs];
        }
        
        //sigmas, link-ratios
        LinkRatio linkRatios = scales.getSourceLinkRatios();
        int devs = factors.getDevelopmentCount();
        sigmas = new double[devs];
        lrs = new double[devs];
        for(int d=0; d<devs; d++) {
            sigmas[d] = scales.getValue(d);
            lrs[d] = linkRatios.getValue(d);
        }
        
        //Source triangle
        source = new InputTriangle(scales.getSourceTriangle().toArray());
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
        return developments[0];
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return developments[accident];
    }

    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        return values[accident][development];
    }

    @Override
    public double[][] toArray() {
        return values;
    }

    @Override
    public void recalculate() {
        for(int a=0; a<accidents; a++) {
            int devs = developments[a];
            for(int d=0; d<devs; d++)
                values[a][d] = lrs[d] + residuals.getValue(a, d) * sigmas[d] / Math.sqrt(source.getValue(a, d));
        }
    }

    @Override
    public void detach() {
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
    }

    @Override
    public void setSource(ClaimTriangle source) {
        throw new UnsupportedOperationException("Can not set source for pseudo triangle!");
    }

    @Override
    public FactorTriangle copy() {
        throw new UnsupportedOperationException("Can not copy pseudo triangle!");
    }

}
