package org.jrlib.bootstrap.mack;

import org.jrlib.bootstrap.ProcessSimulator;

/**
 * MackProcessSimulators calculate the process variance for development
 * period `d` from the claim and scale parameter from development period
 * `d-1`. The mean and variance are calculated as follows:
 * <p><pre>
 *     mean(a,d) = C(a,d)
 *     var(a,d) = sigma(d-1)^2 * C(a,d-1)
 * </pre>
 * where:
 * <ul>
 *   <li>`C(a,d)` is the observed/estimated cummulated claim for accident 
 *       period 'a' and development period `d`.
 *   </li>
 *   <li>`sigma(d)` is Mack's scale parameter {@link org.jrlib.linkratio.scale.LinkRatioScale LinkRatioScale}
 *       for development period `d`.
 *   </li>
 * </ul>
 * </p>
 * @author Peter Decsi
 * @version 1.0
 */
public interface MackProcessSimulator extends ProcessSimulator {

    /**
     * Sets the used estimates.
     * 
     * @throws NullPointerException if `estimate` is null.
     */
    public void setEstimate(MackBootstrapEstimate estimate);
    
}
