package org.jrlib.triangle;

/**
 * Utility methods for {@link Triangle Triangles} and
 * {@link org.jrlib.vector.Vector Vectors}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleUtil {

    /**
     * Retunrs wether the two trinagles have the same geometry. Same 
     * geometry means, that they have the same accident and development
     * counts.
     * 
     * @see Triangle#getAccidentCount() 
     * @see Triangle#getDevelopmentCount() 
     * @see Triangle#getDevelopmentCount(int) 
     * @throws NullPointerException if one of the triangles is null.
     */
    public static boolean isSameGeometry(Triangle t1, Triangle t2) {
        int accidents = t1.getAccidentCount();
        if(accidents != t2.getAccidentCount())
            return false;
        
        for(int a=0; a<accidents; a++)
            if(t1.getDevelopmentCount(a) != t2.getDevelopmentCount(a))
                return false;
        
        return t1.getDevelopmentCount() == t2.getDevelopmentCount();
    }
    
    /**
     * Copies the input array. The result is a new array
     * with the same dimensions as the input, which is 
     * completly independent from the original array.
     */
    public static double[][] copy(double[][] values) {
        if(values == null)
            return null;
        
        int size = values.length;
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = copy(values[i]);
        return result;
    }
    
    /**
     * Copies the given array. The result is a new array
     * with the same dimensions as the input, which is 
     * completly independent from the original array.
     */
    public static double[] copy(double[] values) {
        if(values == null)
            return null;
        int size = values.length;
        double[] result = new double[size];
        System.arraycopy(values, 0, result, 0, size);
        return result;
    }
    
    /**
     * Cummulates the given values. The expected format is
     * arrays of accident periods.
     * 
     * @throws NullPointerException if `values` or one of
     * it's element is null.
     */
    public static void cummulate(double[][] values) {
        for(double[] row : values)
            cummulate(row);
    }
    
    /**
     * Cummulates th given input array (accident period).
     * 
     * @throws NullPointerException if `values` is null.
     */
    private static void cummulate(double[] values) {
        int size = values.length;
        for(int i=1; i<size; i++)
            values[i] += values[i-1];
    }
    
    /**
     * Decummulates the given values. The expected format is
     * arrays of accident periods.
     * 
     * @throws NullPointerException if `values` or one of
     * it's element is null.
     */
    public static void deCummulate(double[][] values) {
        for(double[] row : values)
            deCummulate(row);
    }
    
    /**
     * Decummulates the given values.
     * 
     * @throws NullPointerException if `values` or one of
     * it's element is null.
     */
    private static void deCummulate(double[] values) {
        int size = values.length;
        for(int i=(size-1); i>0; i--)
            values[i] -= values[i-1];
    }
    
    /**
     * Returns the number of accident periods for the given
     * development period. If `a` is returned from this method
     * for development period `d` then for values `n >= a`
     * `triangle.getValue(n, d)` will always return `NaN`.
     * 
     * @throws NullPointerException if `triangle` is null.
     */
    public static int getAccidentCount(Triangle triangle, int development) {
        int max = triangle.getAccidentCount();
        int count = 0;
        while(count<max && development<triangle.getDevelopmentCount(count))
            count++;
        return count;
    }
    
    /**
     * Retunrs an array containing the values for the given development period.
     * The returned array will have the same length as returned from
     * {@link #getAccidentCount(org.jrlib.triangle.Triangle, int) getAccidentCount}.
     * 
     * @throws NullPointerException if `trinagle` is null.
     */
    public static double[] getAccidentValues(Triangle triangle, int development) {
        int accidents = getAccidentCount(triangle, development);
        double[] result = new double[accidents];
        for(int a=0; a<accidents; a++)
            result[a] = triangle.getValue(a, development);
        return result;
    }
    
    /**
     * Adds the two arrays. The length of the returned array will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if either input or on of it's elements are null;
     */
    public static double[][] add(double[][] a, double[][] b) {
        int size = Math.min(a.length, b.length);
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = add(a[i], b[i]);
        return result;
    }
    
    /**
     * Adds the two vectors. The length of the returned vector will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if `t1` or `t2` is null;
     */
    public static double[] add(double[] a, double[] b) {
        int size = Math.min(a.length, b.length);
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = a[i] + b[i];
        return result;
    }
    
    /**
     * Subtracts the two arrays. The length of the returned array will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if either input or on of it's elements are null;
     */
    public static double[][] subtract(double[][] a, double[][] b) {
        int size = Math.min(a.length, b.length);
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = subtract(a[i], b[i]);
        return result;
    }
    
    /**
     * Subtracts the two vectors. The length of the returned vector will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if `a` or `b` is null;
     */
    public static double[] subtract(double[] a, double[] b) {
        int size = Math.min(a.length, b.length);
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = a[i] - b[i];
        return result;
    }
    
    /**
     * Divides the two arrays. The length of the returned array will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if either input or on of it's elements are null;
     */
    public static double[][] divide(double[][] numerator, double[][] denominator) {
        int size = Math.min(numerator.length, denominator.length);
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = divide(numerator[i], denominator[i]);
        return result;
    }
    
    /**
     * Divides the two vectors. The length of the returned vector will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if `numerator` or `denominator` is null;
     */
    public static double[] divide(double[] numerator, double[] denominator) {
        int size = Math.min(numerator.length, denominator.length);
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = numerator[i] / denominator[i];
        return result;
    }
    
    /**
     * Multiplies the two arrays. The length of the returned array will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if either input or on of it's elements are null;
     */
    public static double[][] multiply(double[][] a, double[][] b) {
        int size = Math.min(a.length, b.length);
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = multiply(a[i], b[i]);
        return result;
    }
    
    /**
     * Multiplies the two vectors. The length of the returned vector will be
     * equal to the length of the shorter input.
     * 
     * @throws NullPointerException if `a` or `b` is null;
     */
    public static double[] multiply(double[] a, double[] b) {
        int size = Math.min(a.length, b.length);
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = a[i] * b[i];
        return result;
    }
    
    private TriangleUtil() {
    }

}
