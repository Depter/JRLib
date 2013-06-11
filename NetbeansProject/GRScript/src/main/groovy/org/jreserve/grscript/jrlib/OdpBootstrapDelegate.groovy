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
import org.jreserve.jrlib.estimate.ProcessSimulatorEstimate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OdpBootstrapDelegate extends AbstractBootstrapDelegate implements FunctionProvider {
    
    private String processType = "Gamma"
    private ProcessSimulatorEstimate estimate;
    private OdpScaledResidualTriangle residuals
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
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
    
    void estimate(ProcessSimulatorEstimate estimate) {
        this.estimate = estimate
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
        
        LinkRatio lrs = residuals.getSourceLinkRatios()
        Estimate bsEstimate = buildEstimate(lrs, rnd);
        
        lrs.getSourceFactors().setSource(pseudoCik);
        int count = super.getCount()
        return new EstimateBootstrapper(pseudoCik, count, bsEstimate);
    }
    
    private Estimate buildEstimate(LinkRatio lrs, JRandom rnd) {
        ProcessSimulator procSim = createProcessSimulator(rnd)
        if(estimate) {
            estimate.setProcessSimulator(procSim)
            return estimate
        } else {
            new OdpEstimate(lrs, procSim);
        }
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