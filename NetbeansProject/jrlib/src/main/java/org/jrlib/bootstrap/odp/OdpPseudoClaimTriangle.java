package org.jrlib.bootstrap.odp;

import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jrlib.bootstrap.odp.residuals.OdpScaledResidualTriangle;
import org.jrlib.bootstrap.odp.scale.OdpResidualScale;
import org.jrlib.bootstrap.residualgenerator.DoubleResidualGenerator;
import org.jrlib.triangle.Cell;
import org.jrlib.triangle.TriangleUtil;
import org.jrlib.triangle.claim.ClaimTriangle;
import org.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OdpPseudoClaimTriangle implements ClaimTriangle {
    
    private DoubleResidualGenerator<OdpScaledResidualTriangle> residuals;
    private int accidents;
    private int developments;
    private double[][] fitted;
    private double[][] pseudoValues;
    private double[] scales;
    
    public OdpPseudoClaimTriangle(Random rnd, OdpScaledResidualTriangle residuals) {
        this(rnd, residuals, Collections.EMPTY_LIST);
    }
    
    public OdpPseudoClaimTriangle(Random rnd, OdpScaledResidualTriangle residuals, List<int[][]> segments) {
        this.residuals = new DoubleResidualGenerator<OdpScaledResidualTriangle>(rnd, residuals, segments);
        initData(residuals);
        initScales(residuals.getSourceOdpResidualScales());
    }

    private void initData(OdpScaledResidualTriangle rt) {
        this.fitted = rt.toArrayFittedValues();
        this.pseudoValues = TriangleUtil.copy(fitted);
        this.accidents = rt.getSourceTriangle().getAccidentCount();
        this.developments = rt.getSourceTriangle().getDevelopmentCount();
    }
    
    private void initScales(OdpResidualScale scales) {
        this.scales = new double[developments];
        for(int d=0; d<developments; d++)
            this.scales[d] = scales.getValue(d);
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
                pseudoValues[accident].length : 
                0;
    }
    
    private boolean withinBounds(int accident) {
        return 0<=accident && accident < accidents;
    }

    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                pseudoValues[accident][development] :
                Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               0 <= development && 
               development < pseudoValues[accident].length;
    }

    @Override
    public double[][] toArray() {
        return TriangleUtil.copy(pseudoValues);
    }

    @Override
    public void recalculate() {
        for(int a=0; a<accidents; a++) {
            int devs = fitted[a].length;
            for(int d=0; d<devs; d++)
                pseudoValues[a][d] = recalculatePseudoValue(a, d);
        }
        TriangleUtil.cummulate(pseudoValues);
    }
    
    private double recalculatePseudoValue(int accident, int development) {
        double f = fitted[accident][development];
        double s = scales[development];
        double r = residuals.getValue(accident, development);
        return f + r * Math.sqrt(f * s);
    }
    
    /**
     * Always return false.
     */
    @Override
    public boolean isCallsForwarded() {
        return false;
    }
    
    /**
     * Does nothing.
     */
    @Override
    public void setCallsForwarded(boolean forwardCalls) {}

    /**
     * Does nothing.
     */
    @Override
    public void detach() {}

    /**
     * Does nothing.
     */
    @Override
    public void addChangeListener(ChangeListener listener) {}

    /**
     * Does nothing.
     */
    @Override
    public void removeChangeListener(ChangeListener listener) {}

    /**
     * Does nothing.
     */
    @Override
    public void setEventsFired(boolean eventsFired) {}

    /**
     * Always return false.
     */
    @Override
    public boolean isEventsFired() {
        return false;
    }
}
