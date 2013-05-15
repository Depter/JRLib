package org.jreserve.jrlib.scale;

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
public class MinMaxScaleEstimatorTest {
    
    private final static double[] EXPECTED = {
        111.67022649, 55.88282502, 27.96528879, 19.10349518, 
         16.57903797, 17.16892279,  0.05132606
    };
    
    private DefaultScaleCalculator<LinkRatioScaleInput> source;
    private MinMaxScaleEstimator<LinkRatioScaleInput> estimate;

    @Before
    public void setUp() {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(TestData.INCURRED));
        LinkRatioScaleInput input = new LinkRatioScaleInput(lr);
        source = new DefaultScaleCalculator<LinkRatioScaleInput>(input);
        estimate = new MinMaxScaleEstimator();
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