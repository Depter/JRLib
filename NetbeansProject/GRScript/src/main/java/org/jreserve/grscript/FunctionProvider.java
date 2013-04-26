package org.jreserve.grscript;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.Script;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FunctionProvider {

    public void initFunctions(Script script, ExpandoMetaClass emc);
}
