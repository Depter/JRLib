package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.scale.LinkRatioScaleCaclulator;
import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.LinkRatioScaleExtrapolation;
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
        ClaimTriangle triangle = TestData.getCummulatedTriangle(TestData.INCURRED);
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(triangle));
        source = new LinkRatioScaleCaclulator(lr);
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