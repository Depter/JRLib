package org.jreserve.factor.curve;

import java.util.Map;
import org.jreserve.factor.LinkRatio;
import org.jreserve.factor.LinkRatioSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothing extends LinkRatio {
    
    @Override
    public LinkRatio getSource();
    
    /**
     * Sets the number of development periods. Calling this method fires a
     * change event.
     */
    public void setDevelopmentCount(int developments);
    
    /**
     * Returns the function used, when no method is specified for
     * a given development period.
     */
    public LinkRatioFunction getDefaultFunction();
    
    /**
     * Sets the default function used, when no function is specified for
     * a given development period.
     * 
     * <p>A null value resets the default method to the implementations
     * original default method.</p>
     */
    public void setDefaultFunction(LinkRatioFunction function);
    
    /**
     * Sets the function used for the given development period. Implementations
     * shoudl allow setting functions to larger development periods
     * then the actual development periods found in the source.
     * 
     * <p>If function is null, then the default function should be
     * used for the given develoment period.</p>
     * 
     * <p>Calling this method should fire a change event.</p>
     */
    public void setFunction(LinkRatioFunction function, int development);
    
    /**
     * Sets multiple functions for the given development periods. This
     * method enables to set multiple methods, but fire only on change
     * event.
     */
    public void setFunctions(Map<Integer, LinkRatioFunction> functions);
    
    /**
     * Retunrs the function used for the given development period. If
     * <i>development</i> is less then 0, implementations may throw
     * an exception, in other cases this method should never return 
     * <i>null</i>.
     */
    public LinkRatioFunction getFunction(int development);
}
