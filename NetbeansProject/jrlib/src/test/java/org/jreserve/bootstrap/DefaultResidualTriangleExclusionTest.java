package org.jreserve.bootstrap;

import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.bootstrap.odp.PearsonResidualClaimTriangle;
import org.jreserve.linkratio.SimpleLinkRatio;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultResidualTriangleExclusionTest {

    
    private ResidualTriangle source;
    private ResidualTriangleExclusion exclusion;
    
    public DefaultResidualTriangleExclusionTest() {
    }

    @Before
    public void setUp() {
        source = new PearsonResidualClaimTriangle(new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.PAID)));
        exclusion = new ResidualTriangleExclusion(source);
    }

    @Test
    public void testGetSourceResidualTriangle() {
        assertTrue(source == exclusion.getSourceResidualTriangle());
    }

    @Test
    public void testExclusion() {
        assertFalse(exclusion.isExcluded(0, 0));
        double expected = source.getValue(0, 0);
        assertEquals(expected, exclusion.getValue(0, 0), JRLibTestUtl.EPSILON);
        
        exclusion.excludeResidual(0, 0);
        assertTrue(exclusion.isExcluded(0, 0));
        assertEquals(Double.NaN, exclusion.getValue(0, 0), JRLibTestUtl.EPSILON);
        
        exclusion.includeResidual(0, 0);
        assertFalse(exclusion.isExcluded(0, 0));
        assertEquals(expected, exclusion.getValue(0, 0), JRLibTestUtl.EPSILON);
    }
}