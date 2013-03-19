package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ResidualTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class VariableScaleODPResidualTriangle extends AbstractODPResidualTriangle {

    private int developments;
    private double[] scales;
    
    public VariableScaleODPResidualTriangle(ResidualTriangle source) {
        super(source);
        doRecalculate();
    }

    public double getScale(int development) {
        return (0 <= development && development < developments)?
                scales[development] :
                Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        int accidents = source.getAccidentCount();
        developments = source.getDevelopmentCount();
        scales = new double[developments];
        for(int d=0; d<developments; d++)
            scales[d] = calculateScale(d, accidents);
    }

    private double calculateScale(int development, int accidents) {
        double scale = 0d;
        int count = 0;
        for(int a=0; a<accidents; a++) {
            double r = source.getValue(a, development);
            if(!Double.isNaN(r)) {
                count++;
                scale += Math.pow(r, 2d);
            }
        }
        
        return count==0? Double.NaN : scale / ((double)count);
    }
}
