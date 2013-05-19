package org.jreserve.grscript

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractDelegate implements FunctionProvider {
    
    protected Script script
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        this.script = script
    }
    
    def getProperty(String name) {
        script?.getProperty(name) 
    }
}

