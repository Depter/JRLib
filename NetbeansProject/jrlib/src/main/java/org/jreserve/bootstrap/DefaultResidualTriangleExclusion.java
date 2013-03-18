package org.jreserve.bootstrap;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultResidualTriangleExclusion extends AbstractTriangleModification<ResidualTriangle> implements ResidualTriangleExclusion  {

    private boolean[][] exclusions = new boolean[0][];
    
    public DefaultResidualTriangleExclusion(ResidualTriangle source) {
        super(source);
    }
    
    @Override
    public ResidualTriangle getSourceResidualTriangle() {
        return source;
    }
    
    @Override
    public void excludeResidual(int accident, int development) {
        checkCoordiantes(accident, development);
        ensureCellExists(accident, development);
        exclusions[accident][development] = true;
    } 
    
    private void checkCoordiantes(int accident, int development) {
        if(accident < 0)
            throw new IllegalArgumentException("Accident index less then 0! "+accident);
        if(development < 0)
            throw new IllegalArgumentException("Development index less then 0! "+development);
    }
    
    private void ensureCellExists(int accident, int development) {
        ensureAccidentExists(accident);
        if(exclusions[accident] == null) {
            exclusions[accident] = new boolean[development+1];
        } else {
            ensureDevelopmentExists(accident, development);
        }
    }
    
    private void ensureAccidentExists(int accident) {
        if(accident >= exclusions.length) {
            boolean[][] redim = new boolean[accident+1][];
            System.arraycopy(exclusions, 0, redim, 0, exclusions.length);
            exclusions = redim;
        }
    }
    
    private void ensureDevelopmentExists(int accident, int development) {
        int length = exclusions[accident].length;
        if(development >= length) {
            boolean[] redim = new boolean[development+1];
            System.arraycopy(exclusions[accident], 0, redim, 0, length);
            exclusions[accident] = redim;
        }
    }
    
    @Override
    public void includeResidual(int accident, int development) {
        checkCoordiantes(accident, development);
        if(accident < exclusions.length) {
            boolean[] e = exclusions[accident];
            if(e != null && development < e.length)
                e[development] = false;
        }
    }
    
    @Override
    public boolean isExcluded(int accident, int development) {
        if(accident < 0 || accident >= exclusions.length)
            return false;
        boolean[] e = exclusions[accident];
        return (e!=null && 0 <= development && development < e.length && e[development]);
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(isExcluded(accident, development))
            return Double.NaN;
        return super.getValue(accident, development);
    }
    
    @Override
    protected void recalculateLayer() {
    }
}
