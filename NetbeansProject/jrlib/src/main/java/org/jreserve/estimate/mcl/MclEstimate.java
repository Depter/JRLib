package org.jreserve.estimate.mcl;

import org.jreserve.CalculationData;
import org.jreserve.estimate.AbstractEstimate;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.factor.linkratio.scale.LinkRatioScaleErrorTriangle;
import org.jreserve.triangle.Triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclEstimate extends AbstractEstimate<CalculationData> {
    
    private final static int PAID = 0;
    private final static int INCURRED = 1;

    private MclEstimateDelegate paid;
    private MclEstimateDelegate incurred;
    
    public MclEstimate(LinkRatioScale paidScale, LinkRatioScale incurredScale) {
        super(paidScale, incurredScale);
        initState();
        doRecalculate();
    }
    
    private void initState() {
        initPaidState();
        initIncurredState();
        paid.rho = createRho(incurred.lrs, paid.lrs);
        paid.lambda = new MclLambdaCalculator(paid.rho, paid.scale);
        incurred.rho = createRho(paid.lrs, incurred.lrs);
        incurred.lambda = new MclLambdaCalculator(incurred.rho, incurred.scale);
    }
    
    private void initPaidState() {
        paid = new MclEstimateDelegate();
        paid.scale = (LinkRatioScale) sources[PAID];
        paid.lrs = paid.scale.getSourceLinkRatios();
        paid.cik = paid.scale.getSourceTriangle();
    }
    
    private void initIncurredState() {
        incurred = new MclEstimateDelegate();
        incurred.scale = (LinkRatioScale) sources[INCURRED];
        incurred.lrs = incurred.scale.getSourceLinkRatios();
        incurred.cik = incurred.scale.getSourceTriangle();
    }
    
    private MclRhoSelection createRho(LinkRatio numerator, LinkRatio denominator) {
        DefaultMclRhoSelection selection = new DefaultMclRhoSelection(numerator, denominator);
        MclRho base = selection.getSourceRhos();
        
        MclRhoEstimator estimator = new MclRhoMinMaxEstimator();
        int devs = numerator.getDevelopmentCount();
        for(int d=0; d<devs; d++) {
            if(Double.isNaN(base.getRho(d)))
                selection.setMethod(estimator, d);
        }
        
        return selection;
    }
    
    @Override
    protected void recalculateLayer() {
        paid.rho.recalculate();
        incurred.rho.recalculate();
        paid.lambda.recalculate();
        incurred.lambda.recalculate();
        doRecalculate();
    }
    
    private void doRecalculate() {
        initCalculationState();
        
        double[] paidLPSR = paid.calculateLPSR();
        double[] incurredLPSR = incurred.calculateLPSR();
        
        fillTriangles(paidLPSR, incurredLPSR);
        fillRatios();
    }
 
    private void initCalculationState() {
        accidents = Math.min(paid.cik.getAccidentCount(), incurred.cik.getAccidentCount());
        developments = Math.min(paid.scale.getDevelopmentCount(), incurred.scale.getDevelopmentCount()) + 1;
        paid.initValues();
        incurred.initValues();
        values = new double[accidents][developments];
    }
    
    private void fillTriangles(double[] paidLPSR, double[] incurredLPSR) {
        if(developments < 1)
            return;
        
        for(int a=0; a<accidents; a++) {
            paid.fillCellFromCik(a, 0);
            incurred.fillCellFromCik(a, 0);
            
            int paidDevCount = paid.cik.getDevelopmentCount(a);
            int incurredDevCount = incurred.cik.getDevelopmentCount(a);
            
            for(int d=1; d<developments; d++) {
                if(d<paidDevCount) {
                    paid.fillCellFromCik(a, d);
                } else {
                    paid.estimateCell(incurred, a, d, paidLPSR[d-1]);
                }
                
                if(d<incurredDevCount) {
                    incurred.fillCellFromCik(a, d);
                } else {
                    incurred.estimateCell(paid, a, d, incurredLPSR[d-1]);
                }
            }
        }
    }
    
    private void fillRatios() {
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                double p = paid.getValue(a, d);
                double i = incurred.getValue(a, d);
                values[a][d] = (p==0)? Double.NaN : p/i;
            }
        }
    }
    
    public MclEstimateDelegate getPaidEstimate() {
        return paid;
    }
    
    public MclEstimateDelegate getIncurredDelegate() {
        return incurred;
    }

    @Override
    protected int getObservedDevelopmentCount(int accident) {
        int op = paid.getObservedDevelopmentCount(accident);
        int oi = incurred.getObservedDevelopmentCount(accident);
        return op < oi? op : oi;
    }
    
    public class MclEstimateDelegate extends AbstractEstimate<CalculationData> {
        
        private LinkRatioScale scale;
        private LinkRatio lrs;
        private Triangle cik;
        private MclRhoSelection rho;
        private MclLambdaCalculator lambda;
        
        
        void initValues() {
            super.accidents = MclEstimate.this.accidents;
            super.developments = MclEstimate.this.developments;
            values = new double[accidents][developments];
        }
        
        double[] calculateLPSR() {
            double l = lambda.getLambda();

            int devCount = developments - 1;
            double[] lpsr = new double[devCount];
            for(int d=0; d<devCount; d++)
                lpsr[d] = calculateLPSR(l, scale.getValue(d), rho.getRho(d));
            return lpsr;
        }

        private double calculateLPSR(double lambda, double scale, double rho) {
            if(Double.isNaN(lambda) || Double.isNaN(scale) ||
            Double.isNaN(rho) || rho == 0d)
                return Double.NaN;
            return lambda * scale / rho;
        }
        
        void fillCellFromCik(int accident, int development) {
            values[accident][development] = cik.getValue(accident, development);
        }
        
        void estimateCell(MclEstimateDelegate other, int accident, int development, double lpsr) {
            int prevDev = development-1;
            double c = values[accident][prevDev];
            double oc = other.values[accident][prevDev];
            double lr = lrs.getValue(prevDev);
            double rate = rho.getRatio(prevDev);
        
            if(Double.isNaN(c) || Double.isNaN(oc) || Double.isNaN(lr) ||
               Double.isNaN(lpsr) || Double.isNaN(rate) || c == 0d)
                 values[accident][development] = Double.NaN;
            else
                values[accident][development] = c * (lr + lpsr * (oc / c - rate));
        }
        
        double[][] getValues() {
            return values;
        }
        
        public double getLambda() {
            return lambda.getLambda();
        }
    
        public MclRhoErrorTriangle getRatioErrors() {
            return lambda.getRatioErrors();
        }

        public LinkRatioScaleErrorTriangle getFactorErrors() {
            return lambda.getFactorErrors();
        }
        
        public LinkRatioScale getSourceLRScales() {
            return scale;
        }
        
        public LinkRatio getSourceLinkRatios() {
            return lrs;
        }
        
        public MclRho getSourceMclRhos() {
            return rho;
        }
        
        @Override
        protected int getObservedDevelopmentCount(int accident) {
            return cik.getDevelopmentCount(accident);
        }

        @Override
        protected void recalculateLayer() {
        }
    }
}
