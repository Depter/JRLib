package org.jreserve.jrlib.vector;


/**
 * Base class for all classes that want to modify a vector. A modified 
 * vector has the same length as it's source vector, but
 * the extending classes can return a modified value for some
 * cells.
 * 
 * A modified vector should never return a non NaN value outside
 * the bounds of it's source vector.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractVectorModification extends AbstractVector<Vector> implements ModifiedVector {
    
    /**
     * Creates an instance with the given vector as source.
     * 
     * @throws NullPointerException if `source` is null.
     */
    public AbstractVectorModification(Vector source) {
        super(source);
    }
    
    @Override
    public int getLength() {
        return source.getLength();
    }

    public double getValue(int index) {
        return source.getValue(index);
    }
    
    @Override
    public Vector getSourceVector() {
        return source;
    }
}
