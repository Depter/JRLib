package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.bootstrap.odp.OdpEstimate
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper
import org.jreserve.jrlib.estimate.Estimate
//import org.jreserve.jrlib.bootstrap.odp.r

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OdpBootstrapDelegate implements FunctionProvider {
    
    private String processType = "Gamma"
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.odpBootstrap << this.&odpBootstrap
    }
    
    EstimateBootstrapper<Estimate> odpBootstrap(Closure cl) {
        cl.delegate = this
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        
        return null
    }
    
    void process(String type) {
        this.processType = type
    }
    
}

