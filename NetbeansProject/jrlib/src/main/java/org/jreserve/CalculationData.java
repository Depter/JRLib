package org.jreserve;

/**
 * The calculation data is the basic building block for evry calculation
 * within JRLib. Within JRLib all calculations are represented as a chain of
 * separate calculations. A calculation-link nows only it's input link's but
 * knows noting over the following links up in the chain.
 * 
 * As CalculationData extends the {@link Changeable Changeable} interface,
 * other calculations, interested on the result of this calculation can
 * register a change listener on this calculation. This means that in 
 * order to automatically update their state when needed, all calculation
 * data should listen for changes on their input calculations.
 * 
 * This interface exposes two methods, which can be used by 
 * all calculations.
 * 
 * -   recalculate: By calling this method, the calculation should update 
 *     it's state, after calling recalculate on it's input calculations.
 * -   detach: By calling detach this calculation will drop all listeners, 
 *     after detaching all links downward the calculation chain.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface CalculationData extends Changeable {
    
    /**
     * Recalculates the state of this calculation data. After calling this
     * method the following actions are made in strict order:
     * 
     * 1.  recalculate is called on all input calculations (if any).
     * 2.  The state of this calculation data is recalculated.
     * 3.  If {@link #detach detach} has never been called yet, then 
     *     the registered change listeners are notified about the cahnge.
     */
    public void recalculate();
    
    /**
     * By calling this method, the instance becomes detached. A detached
     * item releases all registered listeners, and will not accept new
     * listeners. After detaching itself the calculation data detaches
     * all of it's input data.
     */
    public void detach();
}
