package org.jreserve.jrlib.bootstrap.util;

import org.jreserve.jrlib.bootstrap.util.HistogramData;
import org.jreserve.jrlib.bootstrap.util.HistogramDataFactory;
import org.jreserve.jrlib.TestConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HistogramDataFactoryTest {
    
    private final static double[] DATA = {
         0.72357985,  0.10094794, -1.12564889,  0.21082691, -0.12782709, -1.00468624, -2.01513583, -1.55732554,  1.47743869, -1.13904243, 
        -1.11587228,  0.83299096, -1.36872375, -0.18172149,  0.27847089, -0.55920859,  1.19840351, -0.96365140, -1.04940305, -0.42256035, 
        -1.32480054, -0.20072318, -1.02054928, -0.28456858,  0.71299122, -0.39576593, -0.36693587, -1.07229462, -0.86663096,  1.20579886, 
        -0.45140298, -1.25386090, -0.27728748,  2.10496368, -0.55614304, -0.53997980, -1.36298419,  2.86299670, -0.66080415,  1.91198772, 
         1.27364749, -0.60846915,  1.43107131, -0.62525078, -0.02008894,  1.06088225,  1.02233089,  0.75797482,  0.28432777,  1.54290740, 
        -0.00940073,  0.69041377,  1.56410993,  0.06655731, -1.92506154, -0.16970427,  0.60206416,  0.89736471, -0.69795590, -0.59851813, 
         2.21860103, -0.09596256, -0.11625549,  0.72500603, -1.21526011, -0.38839992,  0.55407580,  1.31829916, -1.88994863, -0.35130027, 
         2.48947307, -0.81123061, -0.11856241, -2.18873699, -2.44957704,  0.02854348,  0.26468524, -0.32195421,  0.12692397,  1.21773016, 
        -0.58561398, -0.19648000, -1.60907255, -0.25836783,  0.1328569,  -0.68193059, -0.05601889, -0.37988900, -0.46512262,  0.45675103, 
         0.24251842, -1.22069735, -0.47339959, -1.79239063,  1.54646582,  1.22639956, -0.39653442, -1.22582793, -0.52039959, -0.57593259, 
        -0.99334043,  0.47434468, -1.89818153,  1.39409356,  1.18944086,  1.26565750,  2.01640039, -0.59438188, -0.02418918,  1.80128148
    };
    private final static double FIRST_UPPER = -2.15443405;
    private final static double SIZE = 0.59028597;
    private final static int COUNT = 10;
    
    private HistogramDataFactory factory;
    
    @Before
    public void setUp() {
        factory = new HistogramDataFactory(DATA);
    }
    
    @Test
    public void testConstructorSettings() {
        assertEquals(COUNT, factory.getIntervalCount());
        assertEquals(FIRST_UPPER, factory.getFirstUpperBound(), TestConfig.EPSILON);
        assertEquals(SIZE, factory.getIntervalSize(), TestConfig.EPSILON);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervalCount_LessTheOne() {
        factory.setIntervalCount(0);
    }
    
    @Test
    public void testSetIntervalCount() {
        factory.setIntervalCount(5);
        assertEquals(5, factory.getIntervalCount());
        assertEquals(-1.78550532, factory.getFirstUpperBound(), TestConfig.EPSILON);
        assertEquals(1.32814343, factory.getIntervalSize(), TestConfig.EPSILON);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_NaNUpper() {
        factory.setIntervals(Double.NaN, SIZE);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_NegativeInfiniteUpper() {
        factory.setIntervals(Double.NEGATIVE_INFINITY, SIZE);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_PositiveInfiniteUpper() {
        factory.setIntervals(Double.POSITIVE_INFINITY, SIZE);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_NaNSize() {
        factory.setIntervals(FIRST_UPPER, Double.NaN);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_NegativeInfiniteSize() {
        factory.setIntervals(FIRST_UPPER, Double.NEGATIVE_INFINITY);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_PositiveInfiniteSize() {
        factory.setIntervals(FIRST_UPPER, Double.POSITIVE_INFINITY);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetIntervals_InvalidSize() {
        factory.setIntervals(FIRST_UPPER, 0d);
    }
    
    @Test
    public void testSetIntervals() {
        factory.setIntervals(-2d, 0.5);
        assertEquals(11, factory.getIntervalCount());
        assertEquals(-2d, factory.getFirstUpperBound(), TestConfig.EPSILON);
        assertEquals(0.5, factory.getIntervalSize(), TestConfig.EPSILON);
    }
    
    @Test
    public void testBuildData() {
        HistogramData hist = factory.buildData();
        assertEquals(COUNT, hist.getIntervalCount());
        assertEquals(FIRST_UPPER, factory.getFirstUpperBound(), TestConfig.EPSILON);
        assertEquals(SIZE, factory.getIntervalSize(), TestConfig.EPSILON);
        
        factory.setIntervals(-2d, 0.5);
        hist = factory.buildData();
        assertEquals(11, hist.getIntervalCount());
        assertEquals(-2d, factory.getFirstUpperBound(), TestConfig.EPSILON);
        assertEquals(0.5, factory.getIntervalSize(), TestConfig.EPSILON);
    }
}
