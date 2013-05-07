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
import org.jreserve.jrlib.linkratio.scale.residuals.LRResidualTriangle
import org.jreserve.jrlib.claimratio.scale.residuals.CRResidualTriangle
import org.jreserve.jrlib.estimate.mcl.MclCorrelation
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle

import org.jreserve.grscript.util.MapUtil
import org.jreserve.grscript.util.PrintDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EstimateDelegate implements FunctionProvider {
    
    private final static String[] NUMBER_LRS = ["numbers", "numberlrs", "number"]
    private final static String[] COST_LRS = ["costs", "costlrs", "cost"]
    private final static String[] EXPOSURE = ["exposure", "exposures", "exp"]
    private final static String[] LINK_RATIO = ["lrs", "linkratio", "linkratios", "link-ratio", "link-ratios"]
    private final static String[] LOSS_RATIO = ["lossratio", "lossratioss", "loss-ratio", "loss-ratios"]
    private final static String[] LINK_RATIO_SE = ["se", "lrse", "standarderror"]
    
    private MapUtil mapUtil = MapUtil.getInstance()
    private Script script
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        this.script = script
        
        emc.averageCostEstimate         << this.&averageCostEstimate
        emc.bornhuetterFergusonEstimate << this.&bornhuetterFergusonEstimate
        emc.BFEstimate                  << this.&BFEstimate
        emc.expectedLossRatioEstimate   << this.&expectedLossRatioEstimate
        emc.capeCodEstimate             << this.&capeCodEstimate
        emc.chainLadderEstimate         << this.&chainLadderEstimate
        emc.mackEstimate                << this.&mackEstimate
        emc.munichChainLadderEstimate   << this.&munichChainLadderEstimate
        emc.estimate                    << this.&estimate
        emc.printData                   << this.&printData
    }
    
    Estimate averageCostEstimate(LinkRatio numberLrs, LinkRatio costLrs) {
        return new AverageCostEstimate(numberLrs, costLrs)
    }

    Estimate bornhuetterFergusonEstimate(LinkRatio lrs, 
        org.jreserve.jrlib.vector.Vector exposure, 
        org.jreserve.jrlib.vector.Vector lossRatio) {
        
        return new BornhuetterFergusonEstimate(lrs, exposure, lossRatio)
    }

    Estimate BFEstimate(LinkRatio lrs, 
        org.jreserve.jrlib.vector.Vector exposure, 
        org.jreserve.jrlib.vector.Vector lossRatio) {
        
        return this.bornhuetterFergusonEstimate(lrs, exposure, lossRatio)
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
    
    MclEstimateBundle munichChainLadderEstimate(Closure cl) {
        MclBuilder builder = new MclBuilder()
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.createBundle()
    }

    Estimate estimate(Map map) {
        String type = mapUtil.getString(map, "method", "m")
        switch(type?.toLowerCase()) {
        case "average-cost":
        case "average cost":
            LinkRatio nLrs = mapUtil.getValue(map, NUMBER_LRS)
            LinkRatio cLrs = mapUtil.getValue(map, COST_LRS)
            return averageCostEstimate(nLrs, cLrs);
        case "bornhuetter ferguson":
        case "bornhuetter-ferguson":
            LinkRatio lrs = mapUtil.getValue(map, LINK_RATIO)
            org.jreserve.jrlib.vector.Vector exposure = mapUtil.getValue(map, EXPOSURE)
            org.jreserve.jrlib.vector.Vector lossRatio = mapUtil.getValue(map, LOSS_RATIO)
            return bornhuetterFergusonEstimate(lrs, exposure, lossRatio);
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
    
    void printData(String title, Estimate estimate) {
        script.println(title)
        printData estimate
    }
    
    void printData(Estimate estimate) {
        if(estimate instanceof MackEstimate) {
            new MackEstimatePrinter(script).printEstimate((MackEstimate)estimate)
        } else {
            new EstimatePrinter(script).printEstimate(estimate)
        }
    }
    
    void printData(MclEstimateBundle bundle) {
        EstimatePrinter printer = new EstimatePrinter(script)
        script.println("Paid estimate:")
        printer.printEstimate(bundle.getPaidEstimate())
        
        script.println()
        script.println("Incurred estimate:")
        printer.printEstimate(bundle.getIncurredEstimate())
    }
    
    private class EstimatePrinter {
        
        protected Script script
        protected PrintDelegate printer
        
        protected EstimatePrinter(Script script) {
            this.script = script
            this.printer = getDelegateFromScript()
            if(printer == null) {
                printer = new PrintDelegate()
                printer.script = script
            }
        }
        
        private PrintDelegate getDelegateFromScript() {
            return script?.binding?.variables?.get(PrintDelegate.PRINT_DELEGATE)
        }
        
        void printEstimate(Estimate estimate) {
            int accidents = estimate.getAccidentCount()
            int devs = estimate.getDevelopmentCount()-1
        
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
                
                printRow(a+1, last, ultimate, reserve)
            }
        
            script.println()
            printRow("Total", tLast, tU, tR)
        }
        
        private void printRow(def rowId, double... values) {
            int count = 0;
            script.print(rowId)
            values.each {script.print("\t${printer.formatNumber(it)}")}
            script.println()
        }
    }
    
    private class MackEstimatePrinter extends EstimatePrinter {
        
        private MackEstimatePrinter(Script script) {
            super(script)
        }
        
        void printEstimate(MackEstimate estimate) {
            int accidents = estimate.getAccidentCount()
            int devs = estimate.getDevelopmentCount()-1
            
            double tLast = 0d;
            double tU = 0d;
            double tR = 0d;
        
            script.println "Accident\tLast\tUltimate\tReserve\tProc.SE\tProc.SE%\tParam.SE\tParam.SE%\tSE\tSE%"
            for(int a=0; a<accidents; a++) {
                script.print(a+1)
                
                tLast += printValue {estimate.getValue(a, estimate.getObservedDevelopmentCount(a)-1)}
                tU += printValue {estimate.getValue(a, devs)}
            
                double reserve = printValue {estimate.getReserve(a)}
                tR += reserve
                
                printSE(reserve) {estimate.getProcessSE(a)}
                printSE(reserve) {estimate.getParameterSE(a)}
                printSE(reserve) {estimate.getSE(a)}
                script.println()
            }
        
            script.println()
            script.print "Total"
            printValue {tLast}
            printValue {tU}
            printValue {tR}
            printSE(tR) {estimate.getProcessSE()}
            printSE(tR) {estimate.getParameterSE()}
            printSE(tR) {estimate.getSE()}
            script.println()
        }
        
        private double printValue(Closure cl) {
            double value = cl()
            script.print "\t"+printer.formatNumber(value)
            return value
        }
        
        private void printSE(double r, Closure cl) {
            double se = cl()
            script.print "\t"+printer.formatNumber(se)
            if(r == 0d) {
                script.print "\t-"
            } else {
                script.print "\t"+getPercentage(r, se)
            }
        }
        
        private String getPercentage(double reserve, double se) {
            return printer.formatPercentage(se / reserve)
        }
        
    }
    
    private class MclBuilder {
        
        private LRResidualTriangle paidLrResiduals;
        private LRResidualTriangle incurredLrResiduals;
        private CRResidualTriangle pPerIResiduals;
        private CRResidualTriangle iPerPResiduals;
        
        void lrPaid(LRResidualTriangle residuals) {
            this.paidLrResiduals = residuals;
        }
        
        void lrIncurred(LRResidualTriangle residuals) {
            this.incurredLrResiduals = residuals;
        }
        
        void crPaid(CRResidualTriangle residuals) {
            this.iPerP(residuals)
        }
        
        void crIncurred(CRResidualTriangle residuals) {
            this.pPerI(residuals)
        }
        
        void iPerP(CRResidualTriangle residuals) {
            this.iPerPResiduals = residuals;
        }
        
        void pPerI(CRResidualTriangle residuals) {
            this.pPerIResiduals = residuals;
        }
        
        MclEstimateBundle createBundle() {
            MclCorrelation paid = new MclCorrelation(paidLrResiduals, iPerPResiduals)
            MclCorrelation incurred = new MclCorrelation(incurredLrResiduals, pPerIResiduals)
            return new MclEstimateBundle(paid, incurred)
        }
    }
}
