package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.scale.LinkRatioScaleCalculator;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
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
    private LinkRatioScaleMinMaxEstimator estimate;
    
    public LinkRatioScaleMinMaxEstimateTest() {
    }

    @Before
    public void setUp() {
        createSource();
        estimate = new LinkRatioScaleMinMaxEstimator();
    }
    
    private void createSource() {
        ClaimTriangle triangle = TestData.getCummulatedTriangle(TestData.INCURRED);
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(triangle));
        source = new LinkRatioScaleCalculator(lr);
    }

    @Test
    public void testFit() {
        estimate.fit(source);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, estimate.getValue(-1), JRLibTestUtl.EPSILON);
        for(int i=0; i<length; i++)
            assertEquals(EXPECTED[i], estimate.getValue(i), JRLibTestUtl.EPSILON);
    }
}