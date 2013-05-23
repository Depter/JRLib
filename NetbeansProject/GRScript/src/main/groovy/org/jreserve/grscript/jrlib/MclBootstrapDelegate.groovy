package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle
import org.jreserve.jrlib.bootstrap.mcl.MclBootstrapper
import org.jreserve.grscript.util.MapUtil
import org.jreserve.jrlib.util.random.Random as JRandom
import org.jreserve.jrlib.estimate.mcl.MclCorrelation
import org.jreserve.jrlib.bootstrap.mcl.MclProcessSimulator
import org.jreserve.jrlib.bootstrap.mcl.SimpleGammaMclProcessSimulator
import org.jreserve.jrlib.bootstrap.mcl.SimpleNormalMclProcessSimulator
import org.jreserve.jrlib.bootstrap.mcl.WeightedGammaMclProcessSimulator
import org.jreserve.jrlib.bootstrap.mcl.WeightedNormalMclProcessSimulator
import org.jreserve.jrlib.bootstrap.mcl.MclConstantProcessSimulator
import org.jreserve.jrlib.estimate.mcl.MclCalculationBundle
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclResidualBundle
import org.jreserve.jrlib.bootstrap.mcl.pseudodata.MclPseudoData
import org.jreserve.jrlib.bootstrap.mcl.MclBootstrapEstimateBundle
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclBootstrapDelegate extends AbstractBootstrapDelegate {
    
    private MapUtil mapUtil = MapUtil.getInstance()
    
    private String paidProcessType = "Gamma"
    private String incurredProcessType = "Gamma"
    
    private LRResidualTriangle paidLrRes
    private CRResidualTriangle paidCrRes
    private LRResidualTriangle incurredLrRes
    private CRResidualTriangle incurredCrRes
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
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
        String paid = this.mapUtil.getString(map, "paid", "p")
        String incurred = this.mapUtil.getString(map, "incurred", "i")
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
        

        JRandom rnd = super.getRandom()
        MclProcessSimulator ps = createProcessSimulator(rnd, this.paidProcessType, this.paidLrRes, true)
        MclProcessSimulator is = createProcessSimulator(rnd, this.incurredProcessType, this.incurredLrRes, false)
        
        MclResidualBundle resBundle = new MclResidualBundle(this.paidLrRes, this.paidCrRes, this.incurredLrRes, this.incurredCrRes)
        MclPseudoData pseudoData = new MclPseudoData(rnd, resBundle, super.getSegments());
        
        MclBootstrapEstimateBundle estimate = new MclBootstrapEstimateBundle(pseudoData, ps, is);
        return new MclBootstrapper(estimate, super.getCount());
    }
    
    private void checkResiduals() {
        if(!this.paidLrRes)
            throw new IllegalStateException("Paid link-ratio residuals are not set!")
        if(!this.paidCrRes)
            throw new IllegalStateException("Incurred/Paid residuals are not set!")
        if(!this.incurredLrRes)
            throw new IllegalStateException("Incurred link-ratio residuals are not set!")
        if(!this.incurredCrRes)
            throw new IllegalStateException("Paid/Incurred residuals are not set!")
    }
    
    private MclProcessSimulator createProcessSimulator(JRandom rnd, String type, LRResidualTriangle residuals, boolean isPaid) {
        LinkRatioScale scale = residuals.getSourceLinkRatioScales()
        switch(type.toLowerCase()) {
            case "gamma":
            case "simple gamma":
            case "simple-gamma":
               return new SimpleGammaMclProcessSimulator(scale, rnd, isPaid)
            case "normal":
            case "simple normal":
            case "simple-normal":
                return new SimpleNormalMclProcessSimulator(scale, rnd, isPaid)
            case "weighted gamma":
            case "weighted-gamma":
               return new WeightedGammaMclProcessSimulator(scale, rnd, isPaid)
            case "weighted normal":
            case "weighted-normal":
                return new WeightedNormalMclProcessSimulator(scale, rnd, isPaid)
            case "constant":
                return new MclConstantProcessSimulator();
            default:
                throw new IllegalStateException("Unknown process type: ${type}!")
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
        
        def getProperty(String name) {
            this.mbd.getProperty(name)
        }
        
        void paidLr(LRResidualTriangle res) {
            this.mbd.paidLrRes = res
        }
        
        void iPerP(CRResidualTriangle res) {
            this.mbd.paidCrRes = res
        }
        
        void paidCr(CRResidualTriangle res) {
            this.mbd.paidCrRes = res
        }
        
        void incurredLr(LRResidualTriangle res) {
            this.mbd.incurredLrRes = res
        }
        
        void pPerI(CRResidualTriangle res) {
            this.mbd.incurredCrRes = res
        }
        
        void incurredCr(CRResidualTriangle res) {
            this.mbd.incurredCrRes = res
        }
    }
}
