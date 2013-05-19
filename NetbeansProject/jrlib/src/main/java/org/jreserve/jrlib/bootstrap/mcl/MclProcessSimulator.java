package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface MclProcessSimulator extends ProcessSimulator {
    
    public void setBundle(MclBootstrapEstimateBundle bundle);
}
