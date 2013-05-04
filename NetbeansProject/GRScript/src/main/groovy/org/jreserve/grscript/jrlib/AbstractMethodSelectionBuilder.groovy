package org.jreserve.grscript.jrlib

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractMethodSelectionBuilder<T> {

    private java.util.Map<Integer, T> methods = new HashMap<Integer, T>()
    private Map methodInstances = [:]
    
    protected <E extends T> E getCachedMethod(Class<E> clazz, Closure cl) {
        E method = (E) methodInstances[clazz]
        if(method == null) {
            method = cl()
            methodInstances[clazz] = method
        }
        return method
    }
}

