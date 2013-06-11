/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.bootstrap.EstimateBootstrapper
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.estimate.ProcessSimulatorEstimate
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
    private ProcessSimulatorEstimate estimate
    private LRResidualTriangle residuals
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
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
    
    void estimate(ProcessSimulatorEstimate estimate) {
        this.estimate = estimate
    }
    
    private EstimateBootstrapper<MackBootstrapEstimate> buildBootstrap() {
        super.checkState()
        JRandom rnd = super.getRandom()
        
        if(residuals == null)
            throw new IllegalStateException("Residuals not set!")
        MackPseudoFactorTriangle pseudoFik = new MackPseudoFactorTriangle(rnd, residuals, super.getSegments());
        
        LinkRatio lrs = residuals.getSourceLinkRatios()
        Estimate bsEstimate = buildEstimate(lrs, rnd);
        
        lrs.setSource(pseudoFik)
        int count = super.getCount()
        return new EstimateBootstrapper(pseudoFik, count, bsEstimate);
    }
    
    private Estimate buildEstimate(LinkRatio lrs, JRandom rnd) {
        MackProcessSimulator procSim = createProcessSimulator(rnd)
        if(estimate) {
            procSim.setEstimate(estimate)
            estimate.setProcessSimulator(procSim)
            return estimate
        } else {
            new MackBootstrapEstimate(lrs, procSim);
        }
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