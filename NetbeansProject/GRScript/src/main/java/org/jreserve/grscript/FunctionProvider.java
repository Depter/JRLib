package org.jreserve.grscript;

import groovy.lang.ExpandoMetaClass;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FunctionProvider {

    public void initFunctions(ExpandoMetaClass emc);
}
