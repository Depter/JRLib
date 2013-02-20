package org.jreserve.factor.scale;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.LinkRatio;
import org.jreserve.factor.SimpleLinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class LinkRatioScaleExtrapolationTest {
    
    private final static double[] EXPECTED = {
        146.57349699, 67.89088652, 31.44615205, 14.56543771, 
          6.74651625,  3.12489623,  1.44741021, 0.67042108
    };
    
    private LinkRatioScale source;
    private LinkRatioScaleExtrapolation estimate;

    public LinkRatioScaleExtrapolationTest() {
    }

    @Before
    public void setUp() {
        createSource();
        estimate = new LinkRatioScaleExtrapolation();
    }
    
    private void createSource() {
        Triangle triangle = TriangleFactory.create(TestData.INCURRED).cummulate().build();
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(triangle));
        source = new LinkRatioScaleCaclulator(lr, triangle);
    }

    @Test
    public void testFit() {
        estimate.fit(source);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, estimate.getValue(-1), JRLibTestSuite.EPSILON);
        for(int i=0; i<length; i++)
            assertEquals(EXPECTED[i], estimate.getValue(i), JRLibTestSuite.EPSILON);
    }
}