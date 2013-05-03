package org.jreserve.grscript.jrlib

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractMethodSelectionBuilder<T> {

    private java.util.Map<Integer, T> methods = new HashMap<Integer, T>()
    private Map methodInstances = [:]
    
    protected T getCachedMethod(Class clazz, Closure cl) {
        T method = methodInstances[clazz]
        if(method == null) {
            method = cl()
            methodInstances[clazz] = method
        }
        return method
    }
}

