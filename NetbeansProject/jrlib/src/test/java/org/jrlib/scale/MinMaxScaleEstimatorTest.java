package org.jrlib.scale;

import org.jrlib.TestConfig;
import org.jrlib.TestData;
import org.jrlib.linkratio.LinkRatio;
import org.jrlib.linkratio.SimpleLinkRatio;
import org.jrlib.linkratio.scale.LinkRatioScaleInput;
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
        111.6702265, 55.8828250, 32.6734950, 23.2743446, 
         19.9898831,  1.0129175, 0.0513261
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