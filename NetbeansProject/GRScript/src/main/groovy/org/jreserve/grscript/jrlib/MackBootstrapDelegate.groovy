package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.bootstrap.mack.MackBootstrapEstimate
import org.jreserve.jrlib.util.random.Random as JRandom
import org.jreserve.jrlib.bootstrap.mack.MackPseudoFactorTriangle
import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator
import org.jreserve.jrlib.bootstrap.mack.MackGammaProcessSimulator
import org.jreserve.jrlib.bootstrap.mack.MackNormalProcessSimulator
import org.jreserve.jrlib.bootstrap.mack.MackConstantProcessSimulator
import org.jreserve.jrlib.linkratio.LinkRatio

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MackBootstrapDelegate extends AbstractBootstrapDelegate implements FunctionProvider {
    
    private String processType = "Gamma"
    private LRResidualTriangle residuals
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.mackBootstrap << this.&mackBootstrap
    }
    
    EstimateBootstrapper<Estimate> mackBootstrap(Closure cl) {
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
    
    void residuals(LRResidualTriangle residuals) {
        this.residuals = residuals
    }
    
    private EstimateBootstrapper<MackBootstrapEstimate> buildBootstrap() {
        super.checkState()
        JRandom rnd = super.getRandom()
        
        if(residuals == null)
            throw new IllegalStateException("Residuals not set!")
        MackPseudoFactorTriangle pseudoFik = new MackPseudoFactorTriangle(rnd, residuals, super.getSegments());
        MackProcessSimulator procSim = createProcessSimulator(rnd)
        
        LinkRatio lrs = residuals.getSourceLinkRatios()
        lrs.setSource(pseudoFik)
        MackBootstrapEstimate estimate = new MackBootstrapEstimate(lrs, procSim);
        int count = super.getCount()
        return new EstimateBootstrapper<MackBootstrapEstimate>(estimate, count);
    }
    
    private MackProcessSimulator createProcessSimulator(JRandom rnd) {
        switch(processType.toLowerCase()) {
            case "gamma":
                return new MackGammaProcessSimulator(rnd, residuals.getSourceLinkRatioScales())
            case "normal":
                return new MackNormalProcessSimulator(rnd, residuals.getSourceLinkRatioScales())
            case "constant":
                return new MackConstantProcessSimulator();
            default:
                throw new IllegalStateException("Unknown process type: ${processType}!")
        }
    }
}