package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ResidualTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ConstantScaleODPResidualTriangle extends AbstractODPResidualTriangle {

    private double scale;
    
    public ConstantScaleODPResidualTriangle(ResidualTriangle source) {
        super(source);
        doRecalculate();
    }

    @Override
    public double getScale(int development) {
        return scale;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        super.calculateCorrection();
        int accidents = source.getAccidentCount();
        
        scale = 0d;
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double r = source.getValue(a, d);
                if(!Double.isNaN(r))
                    scale += Math.pow(correction * r, 2d);
            }
        }
        
        scale = scale / (double) n;
        
    }
}
