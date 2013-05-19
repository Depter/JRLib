package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.bootstrap.DummyProcessSimulator;
import org.jreserve.jrlib.estimate.Estimate;

/**
 * Dummy implementation of the {@link MackProcessSimulator MackProcessSimulator}
 * interface, which simply adds 1 to the estimated value.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DummyMackProcessSimulator extends DummyProcessSimulator implements MackProcessSimulator {

    @Override
    public void setEstimate(Estimate estimate) {
    }
}
