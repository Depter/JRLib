package org.jreserve;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AbstractChangeableTest.class,
    AbstractCalculationDataTest.class,
    org.jreserve.triangle.TriangleTestSuite.class,
    org.jreserve.smoothing.SmoothingTestSuite.class
})
public class JRLibTestSuite {

    public final static double[][] INCURRED = {
        {4873559, 853069 , -83150 ,  59732 ,  83881 ,  71846, 3624, -70663},
        {5130849, 967272 ,  79653 , -12315 ,  126541,  12102, 235 },
        {5945611, 1580045,  32159 , -180480,  64785 , -23254},
        {6632221, 1228881, -328165, -4469  , -2600},	
        {7020974, 1667612,  24278 , -37897 },		
        {8275453, 1591873,  64978 },			
        {9000368, 1239520},				
        {9511539}
    };
    
    public final static double[][] PAID = {
        {4426765, 992330 , 88952 , 13240 , 38622 , 26720, 36818, 10750},
        {4388958, 984169 , 60162 , 35004 , 75768 , 23890, 572},
        {5280130, 1239396, 76122 , 110189, 112895, 11751},
        {5445384, 1164234, 171583, 16427 , 6451  },
        {5612138, 1837950, 155863, 127146},
        {6593299, 1592418, 74189 },
        {6603091, 1659748},
        {7194587}
    };
    
    public final static double[][] NoC = {
        {26373, 1173, 14, 4 , 2, 11, 0, 0},
        {27623, 1078, 19, 11, 8, 0 , 0},
        {30908, 1299, 27, 17, 2, 1},
        {31182, 1392, 46, 2 , 1},
        {32855, 1754, 46, 15},
        {37661, 1634, 54},
        {38160, 1696},
        {40194}
    };
    
    public final static double[] EXPOSURE = {
        345872,
        395282,
        444693,
        494103,
        544932,
        620275,
        668107,
        718563
    };
    
    public final static double EPSILON = 0.0000001;
    public final static int PRECISION = 7;
    
    public static double roundToPrecision(double value) {
        double multiplier = Math.pow(10, PRECISION);
        value = value * multiplier;
        value = Math.round(value);
        return value/multiplier;
    }
    
    public static void roundToPrecision(double[] values) {
        for(int i=0; i<values.length; i++)
            values[i] = roundToPrecision(values[i]);
    }
}