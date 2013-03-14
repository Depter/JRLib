package org.jreserve.estimate.mcl;

import org.jreserve.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface MclRhoSelection extends MclRho, MethodSelection<MclRho, MclRhoEstimator> {

    public MclRho getSourceRhos();
}
