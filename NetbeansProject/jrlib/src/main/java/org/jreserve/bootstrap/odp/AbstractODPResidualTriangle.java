package org.jreserve.bootstrap.odp;

import org.jreserve.bootstrap.ResidualTriangle;
import org.jreserve.triangle.AbstractTriangleModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractODPResidualTriangle extends AbstractTriangleModification<ResidualTriangle> implements ODPScaledResidualTriangle {

    protected int n;
    protected double adjustment;
    
    public AbstractODPResidualTriangle(ResidualTriangle source) {
        super(source);
    }

    @Override
    public ResidualTriangle getSourceResidualTriangle() {
        return source;
    }
    
    @Override
    public double getAdjustment(int development) {
        return adjustment;
    }
    
    @Override
    public double getValue(int accident, int development) {
        double rp = source.getValue(accident, development);
        return  adjustment * rp / getScale(development);
    }

    protected void calculateAdjustment() {
        countResiduals();
        int p = source.getAccidentCount() + source.getDevelopmentCount() - 1;
        adjustment = Math.sqrt(((double) n) / ((double)(n-p)));
    }
    
    private int countResiduals() {
        n = 0;
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++)
                if(!Double.isNaN(source.getValue(a, d)))
                    n++;
        }
        return n;
    }
    
    @Override
    public double getWeight(int accident, int development) {
        return source.getWeight(accident, development);
    }
}
