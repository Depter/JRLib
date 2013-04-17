package org.jrlib.bootstrap.mack;

import org.jrlib.bootstrap.DummyProcessSimulator;
import org.jrlib.estimate.Estimate;

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
