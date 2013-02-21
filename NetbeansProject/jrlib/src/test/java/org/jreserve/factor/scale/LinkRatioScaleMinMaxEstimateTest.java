package org.jreserve.factor.scale;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.DevelopmentFactors;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class LinkRatioScaleMinMaxEstimateTest {
    
    private final static double[] EXPECTED = {
        111.6702265, 55.8828250, 32.6734950, 23.2743446, 
         19.9898831,  1.0129175, 0.0513261
    };
    
    private LinkRatioScale source;
    private LinkRatioScaleMinMaxEstimate estimate;
    
    public LinkRatioScaleMinMaxEstimateTest() {
    }

    @Before
    public void setUp() {
        createSource();
        estimate = new LinkRatioScaleMinMaxEstimate();
    }
    
    private void createSource() {
        Triangle triangle = TriangleFactory.create(TestData.INCURRED).cummulate().build();
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(triangle));
        source = new LinkRatioScaleCaclulator(lr);
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