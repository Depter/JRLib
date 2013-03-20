package org.jreserve;

/**
 *
 * @author Peter Decsi
 */
public class JRLibTestUtl {
    
    public final static double EPSILON = 0.0000001;
    public final static int PRECISION = 7;
    
    public final static boolean EXECUTE_SPEED_TESTS = true;
    
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