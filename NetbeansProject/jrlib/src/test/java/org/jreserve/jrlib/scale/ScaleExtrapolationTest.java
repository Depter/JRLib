package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.scale.DefaultScaleCalculator;
import org.jreserve.jrlib.scale.ScaleExtrapolation;
import org.jreserve.jrlib.scale.Scale;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.SimpleLinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScaleExtrapolationTest {
    
    private final static double[] EXPECTED = {
        146.57349699, 67.89088652, 31.44615205, 14.56543771, 
          6.74651625,  3.12489623,  1.44741021, 0.67042108
    };
    
    private Scale<LinkRatioScaleInput> source;
    private ScaleExtrapolation<LinkRatioScaleInput> estimate;

    @Before
    public void setUp() {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.INCURRED));
        LinkRatioScaleInput input = new LinkRatioScaleInput(lr);
        source = new DefaultScaleCalculator(input);
        estimate = new ScaleExtrapolation<LinkRatioScaleInput>();
    }

    @Test
    public void testFit() {
        estimate.fit(source);
        
        int length = EXPECTED.length;
        assertEquals(Double.NaN, estimate.getValue(-1), TestConfig.EPSILON);
        for(int i=0; i<length; i++)
            assertEquals(EXPECTED[i], estimate.getValue(i), TestConfig.EPSILON);
    }
}