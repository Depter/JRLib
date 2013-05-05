package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.estimate.AverageCostEstimate
import org.jreserve.jrlib.estimate.BornhuetterFergusonEstimate
import org.jreserve.jrlib.estimate.ExpectedLossRatioEstimate
import org.jreserve.jrlib.estimate.CapeCodEstimate
import org.jreserve.jrlib.estimate.ChainLadderEstimate
import org.jreserve.jrlib.linkratio.standarderror.LinkRatioSE
import org.jreserve.jrlib.estimate.MackEstimate
import org.jreserve.grscript.util.MapUtil
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EstimateDelegate implements FunctionProvider {
    
    private final static String[] NUMBER_LRS = ["numbers", "numberlrs"]
    private final static String[] COST_LRS = ["costs", "costlrs"]
    private final static String[] EXPOSURE = ["exposure", "exposures", "exp"]
    private final static String[] LINK_RATIO = ["lrs", "linkratio", "linkratios"]
    private final static String[] LOSS_RATIO = ["lossratio", "lossratioss"]
    private final static String[] LINK_RATIO_SE = ["se", "lrse", "standarderror"]
    
    private MapUtil mapUtil = MapUtil.getInstance()
    private Script script
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        this.script = script
        //emc.linkRatio << this.&linkRatio
    }
    
    Estimate averageCostEstimate(LinkRatio numberLrs, LinkRatio costLrs) {
        return new AverageCostEstimate(numberLrs, costLrs)
    }

    Estimate bornhuetterFergussonEstimate(LinkRatio lrs, 
        org.jreserve.jrlib.vector.Vector exposure, 
        org.jreserve.jrlib.vector.Vector lossRatio) {
        
        return new BornhuetterFergusonEstimate(lrs, exposure, lossRatio)
    }

    Estimate expectedLossRatioEstimate(LinkRatio lrs, 
        org.jreserve.jrlib.vector.Vector exposure, 
        org.jreserve.jrlib.vector.Vector lossRatio) {
        return new ExpectedLossRatioEstimate(lrs, exposure, lossRatio)
    }
                
    Estimate capeCodEstimate(LinkRatio lrs, org.jreserve.jrlib.vector.Vector exposure) {
        return new CapeCodEstimate(lrs, exposure)
    }

    Estimate chainLadderEstimate(LinkRatio lrs) {
        return new ChainLadderEstimate(lrs)
    }

    Estimate mackEstimate(LinkRatioSE lrSE) {
        return new MackEstimate(lrSE)
    }

    Estimate estimate(Map map) {
        String type = mapUtil.getString(map, "method", "m")
        switch(type?.toLowerCase()) {
            case "average-cost":
            case "average cost":
                LinkRatio nLrs = mapUtil.getValue(map, NUMBER_LRS)
                LinkRatio cLrs = mapUtil.getValue(map, COST_LRS)
                return averageCostEstimate(nLrs, cLrs);
            case "bornhuetter fergusson":
            case "bornhuetter-fergusson":
                LinkRatio lrs = mapUtil.getValue(map, LINK_RATIO)
                org.jreserve.jrlib.vector.Vector exposure = mapUtil.getValue(map, EXPOSURE)
                org.jreserve.jrlib.vector.Vector lossRatio = mapUtil.getValue(map, LOSS_RATIO)
                return bornhuetterFergussonEstimate(lrs, exposure, lossRatio);
            case "expected-loss-ratio":
            case "expected loss ratio":
                LinkRatio lrs = mapUtil.getValue(map, LINK_RATIO)
                org.jreserve.jrlib.vector.Vector exposure = mapUtil.getValue(map, EXPOSURE)
                org.jreserve.jrlib.vector.Vector lossRatio = mapUtil.getValue(map, LOSS_RATIO)
                return expectedLossRatioEstimate(lrs, exposure, lossRatio);
            case "cape-cod":
            case "cape cod":
                LinkRatio lrs = mapUtil.getValue(map, LINK_RATIO)
                org.jreserve.jrlib.vector.Vector exposure = mapUtil.getValue(map, EXPOSURE)
                return capeCodEstimate(lrs, exposure);
            case "chain-ladder":
            case "chain ladder":
                LinkRatio lrs = mapUtil.getValue(map, LINK_RATIO)
                return chainLadderEstimate(lrs);
            case "mack":
                LinkRatioSE lrSE = mapUtil.getValue(map, LINK_RATIO_SE)
                return mackEstimate(lrSE);
            default:
                String msg = "Unknown method type: ${type}";
                throw new IllegalArgumentException(msg)
        }
    }
    
    void printData(Estimate estimate) {
        int accidents = estimate.getAccidentCount()
        int devs = estimate.getDevelopmentCount()
        
        double tLast = 0d;
        double tU = 0d;
        double tR = 0d;
        
        script.println "Accident\tLast\tUltimate\tReserve"
        for(int a=0; a<accidents; a++) {
            int lastO = estimate.getObservedDevelopmentCount(a)-1
            double last = estimate.getValue(a, lastO)
            tLast += last
            
            double ultimate = estimate.getValue(a, devs)
            tU += ultimate 
            
            double reserve = estimate.getReserve(a)
            tR += reserve
            
            script.println "${a+1}\t${last}\t${ultimate}\t${reserve}"
        }
        
        script.println()
        script.println "Total\t${tLast}\t${tU}\t${tR}"
    }
    
    void printData(MackEstimate estimate) {
        int accidents = estimate.getAccidentCount()
        int devs = estimate.getDevelopmentCount()
        
        double tLast = 0d;
        double tU = 0d;
        double tR = 0d;
        double tProc = 0d;
        double tParam = 0d;
        double tSE = 0d;
        
        script.println "Accident\tUltimate\tReserve\tProc SE\tProc SE%\tParam SE\tParam SE%\tSE\tSE%"
        for(int a=0; a<accidents; a++) {
            int lastO = estimate.getObservedDevelopmentCount(a)-1
            double last = estimate.getValue(a, lastO)
            tLast += last
            
            double ultimate = estimate.getValue(a, devs)
            tU += ultimate 
            
            double reserve = estimate.getReserve(a)
            tR += reserve
            
            script.println "${a+1}\t${last}\t${ultimate}\t${reserve}"
        }
        
        script.println()
        script.println "Total\t${tLast}\t${tU}\t${tR}"
    }
}
