package org.jreserve.factor;

import java.util.Map;
import org.jreserve.triangle.Triangle;

/**
 * This interface represents the calculation of link ratios, from
 * a triangle of development factors.
 * 
 * <p>The class enables the users to use different calculation 
 * methods ({@link LinkRatioMethod LinkRatioMethod}) for different 
 * development periods.</p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSelection extends LinkRatio {
    
    @Override
    public Triangle getSource();
    
    /**
     * Returns the weights for each development factor factor.
     */
    public Triangle getWeights();
    
    /**
     * Sets the used weights for each development factor.
     * Setting this value fires a change event.
     */
    public void setWeights(Triangle weights);
    
    /**
     * Returns the method used, when no method is specified for
     * a given development period.
     */
    public LinkRatioMethod getDefaultMethod();
    
    /**
     * Sets the default method used, when no method is specified for
     * a given development period.
     * 
     * <p>A null value resets the default method to the implementations
     * original default method.</p>
     */
    public void setDefaultMethod(LinkRatioMethod method);
    
    /**
     * Sets the method used for the given development period. Implementations
     * shoudl allow setting methods the more development periods
     * then the actual development periods found in the source.
     * 
     * <p>If method is null, then the default method should be
     * used for the given develoment period.</p>
     * 
     * <p>Calling this method should fire a change event.</p>
     */
    public void setMethod(LinkRatioMethod method, int development);
    
    /**
     * Sets multiple methods for the given development periods. This
     * method enables to set multiple methods, but fire only on change
     * event.
     */
    public void setMethods(Map<Integer, LinkRatioMethod> methods);
    
    /**
     * Retunrs the method used for the given development period. If
     * <i>development</i> is less then 0, implementations may throw
     * an exception, in other cases this method should never return 
     * <i>null</i>.
     */
    public LinkRatioMethod getMethod(int development);
}
