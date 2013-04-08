package org.jreserve.estimate.mcl2;

import javax.swing.event.ChangeListener;
import org.jreserve.AbstractChangeable;
import org.jreserve.AbstractMultiSourceCalculationData;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclEstimateInput extends AbstractMultiSourceCalculationData<ClaimTriangle>{
    
    @Override
    protected void recalculateLayer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private class ClaimTriangleAdapter extends AbstractChangeable implements ClaimTriangle {
        private ClaimTriangle delegate;

        public int getAccidentCount() {
            return delegate.getAccidentCount();
        }

        public int getDevelopmentCount() {
            return delegate.getDevelopmentCount();
        }

        public int getDevelopmentCount(int accident) {
            return delegate.getDevelopmentCount(accident);
        }

        public double getValue(Cell cell) {
            return delegate.getValue(cell);
        }

        public double getValue(int accident, int development) {
            return delegate.getValue(accident, development);
        }

        public double[][] toArray() {
            return delegate.toArray();
        }

        public void recalculate() {
        }

        public void detach() {
        }

        public ClaimTriangle copy() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
