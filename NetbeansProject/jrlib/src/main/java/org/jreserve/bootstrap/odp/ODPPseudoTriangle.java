package org.jreserve.bootstrap.odp;

import javax.swing.event.ChangeListener;
import org.jreserve.bootstrap.ResidualGenerator;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ODPPseudoTriangle implements ClaimTriangle {
    
    private final double[][] mij;
    private final int accidents;        //mij.length - accidents
    private final int[] developments;   //mij[a].length = developments[a]
    
    private final ResidualGenerator residuals;
    private final double[] scales;
    private final double[] adjustments;
    
    private final double[][] values;
    
    ODPPseudoTriangle(ResidualGenerator residuals, ODPScaledResidualTriangle odpResiduals) {
        this.residuals = residuals;
        
        accidents = odpResiduals.getAccidentCount();
        developments = new int[accidents];
        
        mij = new double[accidents][];
        values = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = odpResiduals.getDevelopmentCount(a);
            developments[a] = devs;
            
            mij[a] = new double[devs];
            for(int d=0; d<devs; d++) 
                mij[a][d] = odpResiduals.getWeight(a, d);
            
            values[a] = new double[devs];
        }
        
        int devCount = odpResiduals.getDevelopmentCount();
        scales = new double[devCount];
        adjustments = new double[devCount];
        for(int d=0; d<devCount; d++) {
            scales[d] = odpResiduals.getScale(d);
            adjustments[d] = odpResiduals.getAdjustment(d);
        }
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
            for(int d=0; d<devs; d++) {
                double r = residuals.getValue(a, d);
                double m = mij[a][d];
                values[a][d] = r * scales[d] / adjustments[d] * Math.sqrt(m) + m;
            }
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

    public ClaimTriangle copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
