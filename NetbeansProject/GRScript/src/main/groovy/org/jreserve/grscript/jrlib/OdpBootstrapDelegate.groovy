package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.bootstrap.odp.OdpEstimate
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.bootstrap.odp.scaledresiduals.OdpScaledResidualTriangle
import org.jreserve.jrlib.bootstrap.odp.OdpPseudoClaimTriangle
import org.jreserve.jrlib.util.random.Random as JRandom
import org.jreserve.jrlib.bootstrap.odp.OdpGammaProcessSimulator
import org.jreserve.jrlib.bootstrap.odp.OdpConstantProcessSimulator
import org.jreserve.jrlib.bootstrap.ProcessSimulator
import org.jreserve.jrlib.linkratio.LinkRatio

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OdpBootstrapDelegate extends AbstractBootstrapDelegate implements FunctionProvider {
    
    private String processType = "Gamma"
    private OdpScaledResidualTriangle residuals
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.odpBootstrap << this.&odpBootstrap
    }
    
    EstimateBootstrapper<Estimate> odpBootstrap(Closure cl) {
        cl.delegate = this
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return buildBootstrap()
    }
    
    void process(String type) {
        if(type == null)
            type = "Constant"
        this.processType = type
    }
    
    void residuals(OdpScaledResidualTriangle residuals) {
        this.residuals = residuals
    }
    
    private EstimateBootstrapper<OdpEstimate> buildBootstrap() {
        super.checkState()
        JRandom rnd = super.getRandom()
        
        if(residuals == null)
            throw new IllegalStateException("Residuals not set!")
        OdpPseudoClaimTriangle pseudoCik = new OdpPseudoClaimTriangle(rnd, residuals, super.getSegments());
        ProcessSimulator procSim = createProcessSimulator(rnd)
        
        LinkRatio lrs = residuals.getSourceLinkRatios()
        lrs.getSourceFactors().setSource(pseudoCik);
        OdpEstimate odpEstimate = new OdpEstimate(lrs, procSim);
        int count = super.getCount()
        return new EstimateBootstrapper<OdpEstimate>(odpEstimate, count);
    }
    
    private ProcessSimulator createProcessSimulator(JRandom rnd) {
        switch(processType.toLowerCase()) {
            case "gamma":
                return new OdpGammaProcessSimulator(rnd, residuals.getSourceOdpResidualScales())
            case "constant":
                return new OdpConstantProcessSimulator();
            default:
                throw new IllegalStateException("Unknown process type: ${processType}!")
        }
    }
}