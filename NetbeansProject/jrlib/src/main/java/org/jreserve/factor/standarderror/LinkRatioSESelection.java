package org.jreserve.factor.standarderror;

import org.jreserve.util.MethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSESelection extends LinkRatioSE, MethodSelection<LinkRatioScale, LinkRatioSEFunction> {

    public void setDevelopmentCount(int developments);
}
