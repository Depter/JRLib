package org.jrlib.triangle;

import org.jrlib.AbstractCalculationData;
import org.jrlib.CalculationData;

/**
 * Abstract implementation of the {@link Triangle Triangle} interface. This
 * implementation handles the utility methods from the Triangle interface,
 * extending classes should provide implementations for the bounds and
 * values of the triangle.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangle<T extends CalculationData> extends AbstractCalculationData<T> implements Triangle {
    
    /**
     * Creates an instance, with the given `source` as input.
     * 
     * @throws NullPointerException if `source` is null.
     */
    protected AbstractTriangle(T source) {
        super(source);
    }
    
    /**
     * Creates an instance with no source.
     */
    protected AbstractTriangle() {
    }
    
    /**
     * Forwards the call to the {@link Triangle#getValue(int, int) getValue(accident, development)}
     * method.
     * 
     * @throws NullPointerException when 'cell' is null.
     */
    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }
    
    /**
     * Checks wether the given `accident` and `development` periods
     * are within the bound of the triangle. This method uses
     * the {@link #withinBounds(int) withinBounds(accident)} and
     * the {@link Triangle#getDevelopmentCount(int) getDevelopmentCount(accident)}
     * methods to check the bounds.
     * 
     * @return `true` if the given cell is contained in the triangle.
     */
    protected boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               development >= 0 &&
               development < getDevelopmentCount(accident);
    }
    
    /**
     * Checks wether the given `accident` period is within the bound of 
     * the triangle. This method uses the 
     * {@link Triangle#getAccidentCount() getAccidentCount()} and methods 
     * to check the bound.
     * 
     * @return `true` if the given accident period is contained in the 
     *          triangle.
     */
    protected boolean withinBounds(int accident) {
        return accident >= 0 &&
               accident < getAccidentCount();
    }

    /**
     * Fills an array with the same dimensions as the triangle with
     * the values from the triangle.
     * 
     * @see Triangle#toArray() 
     */
    @Override
    public double[][] toArray() {
        int accidents = getAccidentCount();
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }
    
    private double[] toArray(int accident) {
        int devs = getDevelopmentCount(accident);
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = getValue(accident, d);
        return result;
    }        
}
