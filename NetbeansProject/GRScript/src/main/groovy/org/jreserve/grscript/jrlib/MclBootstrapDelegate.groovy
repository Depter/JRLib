package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle
import org.jreserve.jrlib.bootstrap.mcl.MclBootstrapper
import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.bootstrap.mack.MackProcessSimulator
import org.jreserve.jrlib.util.random.Random as JRandom
import org.jreserve.jrlib.bootstrap.mack.MackConstantProcessSimulator
import org.jreserve.jrlib.bootstrap.mack.MackGammaProcessSimulator
import org.jreserve.jrlib.bootstrap.mack.MackNormalProcessSimulator
import org.jreserve.jrlib.estimate.mcl.MclCorrelation
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData
import org.jreserve.jrlib.bootstrap.mcl.MclBootstrapEstimateBundle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclBootstrapDelegate extends AbstractBootstrapDelegate implements FunctionProvider {
    
    private MapUtil mapUtil = MapUtil.getInstance()
    
    private String paidProcessType = "Gamma"
    private String incurredProcessType = "Gamma"
    
    private LRResidualTriangle paidLrRes
    private CRResidualTriangle paidCrRes
    private LRResidualTriangle incurredLrRes
    private CRResidualTriangle incurredCrRes
    
    //private OdpScaledResidualTriangle residuals
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        emc.mclBootstrap << this.&mclBootstrap
    }
    
    MclBootstrapper mclBootstrap(Closure cl) {
        cl.delegate = this
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return buildBootstrap()
    }
    
    void process(String type) {
        if(type == null)
            type = "Constant"
        this.process(type, type)
    }
    
    void process(String paid, String incurred) {
        this.paidProcessType = paid ?: "Constant"
        this.incurredProcessType = incurred ?: "Constant"
    }
    
    void process(Map map) {
        String paid = mapUtil.getString(map, "paid", "p")
        String incurred = mapUtil.getString(map, "incurred", "i")
        this.process(paid, incurred)
    }
    
    void process(Closure cl) {
        ProcessBuilder builder = new ProcessBuilder()
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        this.process(builder.paidType, builder.incurredType)
    }
    
    void residuals(Closure cl) {
        ResidualBuilder builder = new ResidualBuilder(this)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
    }
    
    private MclBootstrapper buildBootstrap() {
        super.checkState()
        checkResiduals()
        
        MclCorrelation paidLambda = new MclCorrelation(paidLrRes, paidCrRes)
        MclCorrelation incurredLambda = new MclCorrelation(incurredLrRes, incurredCrRes)
        MclCalculationBundle calcBundle = new MclCalculationBundle(paidLambda, incurredLambda)
        MclResidualBundle resBundle = new MclResidualBundle(paidLrRes, paidCrRes, incurredLrRes, incurredCrRes)

        JRandom rnd = super.getRandom()
        MclPseudoData pseudoData = new MclPseudoData(rnd, resBundle, super.getSegments());
        MackProcessSimulator ps = createProcessSimulator(rnd, paidProcessType, paidLrRes)
        MackProcessSimulator is = createProcessSimulator(rnd, incurredProcessType, incurredLrRes)
        
        MclBootstrapEstimateBundle extimate = new MclBootstrapEstimateBundle(calcBundle, pseudoData, ps, is);
        return new MclBootstrapper(extimate, super.getCount());
    }
    
    private void checkResiduals() {
        if(!paidLrRes)
            throw new IllegalStateException("Paid link-ratio residuals are not set!")
        if(!paidCrRes)
            throw new IllegalStateException("Incurred/Paid residuals are not set!")
        if(!incurredLrRes)
            throw new IllegalStateException("Incurred link-ratio residuals are not set!")
        if(!incurredCrRes)
            throw new IllegalStateException("Paid/Incurred residuals are not set!")
    }
    
    private MackProcessSimulator createProcessSimulator(JRandom rnd, String type, LRResidualTriangle residuals) {
        switch(type.toLowerCase()) {
            case "gamma":
                return new MackGammaProcessSimulator(rnd, residuals.getSourceOdpResidualScales())
            case "normal":
                return new MackNormalProcessSimulator(rnd, residuals.getSourceOdpResidualScales())
            case "constant":
                return new MackConstantProcessSimulator();
            default:
                throw new IllegalStateException("Unknown process type: ${processType}!")
        }
    }    
    
    private class ProcessBuilder {
        
        private String paidType;
        private String incurredType;
        
        void paid(String type) {
            this.paidType = type
        }
        
        void incurred(String type) {
            this.incurredType = type
        }
    }
    
    private class ResidualBuilder {
        private MclBootstrapDelegate mbd
        
        ResidualBuilder(MclBootstrapDelegate mbd) {
            this.mbd = mbd
        }
        
        void paidLr(LRResidualTriangle res) {
            mbd.paidLrRes = res
        }
        
        void iPerP(CRResidualTriangle res) {
            mbd.paidCrRes = res
        }
        
        void incurredLr(LRResidualTriangle res) {
            mbd.incurredLrRes = res
        }
        
        void pPerI(CRResidualTriangle res) {
            mbd.incurredCrRes = res
        }
    }
}
