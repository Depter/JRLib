package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleUtil {

    public static double[][] copy(double[][] values) {
        if(values == null)
            return null;
        
        int size = values.length;
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = copy(values[i]);
        return result;
    }
    
    public static double[] copy(double[] values) {
        if(values == null)
            return null;
        int size = values.length;
        double[] result = new double[size];
        System.arraycopy(values, 0, result, 0, size);
        return result;
    }
    
    public static void cummulate(double[][] values) {
        for(double[] row : values)
            cummulate(row);
    }
    
    private static void cummulate(double[] values) {
        int size = values.length;
        for(int i=1; i<size; i++)
            values[i] += values[i-1];
    }
    
    public static void deCummulate(double[][] values) {
        for(double[] row : values)
            deCummulate(row);
    }
    
    private static void deCummulate(double[] values) {
        int size = values.length;
        for(int i=(size-1); i>0; i--)
            values[i] -= values[i-1];
    }
    
    private TriangleUtil() {
    }
}
