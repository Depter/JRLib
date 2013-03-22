package org.jreserve.bootstrap.odp;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.ResidualTriangle;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractODPResidualTriangleTest {

    private ResidualTriangle paidSource;
    private AbstractODPResidualTriangle paid;
    private ResidualTriangle incurredSource;
    private AbstractODPResidualTriangle incurred;

    public AbstractODPResidualTriangleTest() {
    }

    @Before
    public void setUp() {
        LinkRatio lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID));
        paidSource = new PearsonResidualClaimTriangle(lrs);
        paid = new AbstractODPResidualTriangleImpl(paidSource);
        
        lrs = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.INCURRED));
        incurredSource = new PearsonResidualClaimTriangle(lrs);
        incurred = new AbstractODPResidualTriangleImpl(incurredSource);
    }

    @Test
    public void testGetSourceResidualTriangle() {
        assertTrue(paidSource == paid.getSourceResidualTriangle());
        assertTrue(incurredSource == incurred.getSourceResidualTriangle());
    }

    @Test
    public void testGetValue_Paid() {
        paid.calculateAdjustment();
        double correction = paid.adjustment;
        
        int accidents = paidSource.getAccidentCount();
        assertEquals(Double.NaN, paid.getValue(-1, 0), JRLibTestUtl.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = paidSource.getDevelopmentCount(a);
            assertEquals(Double.NaN, paid.getValue(a, -1), JRLibTestUtl.EPSILON);
            for(int d=0; d<devs; d++) {
                double expected = correction * paidSource.getValue(a, d);
                double found = paid.getValue(a, d);
                assertEquals(expected, found, JRLibTestUtl.EPSILON);
            }
        }
        assertEquals(Double.NaN, paid.getValue(accidents, 0), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testGetValue_Incurred() {
        incurred.calculateAdjustment();
        double correction = incurred.adjustment;
        
        int accidents = incurredSource.getAccidentCount();
        assertEquals(Double.NaN, incurred.getValue(-1, 0), JRLibTestUtl.EPSILON);
        for(int a=0; a<accidents; a++) {
            int devs = incurredSource.getDevelopmentCount(a);
            assertEquals(Double.NaN, incurred.getValue(a, -1), JRLibTestUtl.EPSILON);
            for(int d=0; d<devs; d++) {
                double expected = correction * incurredSource.getValue(a, d);
                double found = incurred.getValue(a, d);
                assertEquals(expected, found, JRLibTestUtl.EPSILON);
            }
        }
        assertEquals(Double.NaN, incurred.getValue(accidents, 0), JRLibTestUtl.EPSILON);
    }
    
    public void testCalculateCorrection() {
        paid.calculateAdjustment();
        assertEquals(1.30930734, paid.adjustment, JRLibTestUtl.EPSILON);
        assertEquals(36, paid.n);
    
        incurred.calculateAdjustment();
        assertEquals(1.63299316, incurred.adjustment, JRLibTestUtl.EPSILON);
        assertEquals(24, incurred.n);
    }

    private class AbstractODPResidualTriangleImpl extends AbstractODPResidualTriangle {

        public AbstractODPResidualTriangleImpl(ResidualTriangle source) {
            super(source);
        }

        @Override
        protected void recalculateLayer() {
        }

        @Override
        public double getScale(int development) {
            return 1d;
        }
    }

}