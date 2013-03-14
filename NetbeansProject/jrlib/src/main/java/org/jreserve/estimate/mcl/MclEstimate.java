package org.jreserve.estimate.mcl;

import org.jreserve.AbstractMultiSourceCalculationData;
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
    
    private double[][] paidValues;
    private double[][] incurredValues;
    
    public MclEstimate(LinkRatioScale paidScale, LinkRatioScale incurredScale) {
        super(paidScale, incurredScale);
        initState();
        doRecalculate();
    }
    
    private void initState() {
        incurred = new MclEstimateDelegate();
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
        
        paidValues = paid.getValues();
        incurredValues = incurred.getValues();
        
        for(int a=0; a<accidents; a++) {
            paidValues[a][0] = paid.cik.getValue(a, 0);
            incurredValues[a][0] = incurred.cik.getValue(a, 0);
            
            int paidDevCount = paid.cik.getDevelopmentCount(a);
            int incurredDevCount = incurred.cik.getDevelopmentCount(a);
            
            for(int d=1; d<developments; d++) {
                paidValues[a][d] = d<paidDevCount? paid.cik.getValue(a, d) : calculatePaidClaim(a, d, paidLPSR[d-1]);
                incurredValues[a][d] = d<incurredDevCount? incurred.cik.getValue(a, d) : calculateIncurredClaim(a, d, incurredLPSR[d-1]);
            }
        }
    }
    
    private double calculatePaidClaim(int accident, int development, double lpsr) {
        int prevDev = development-1;
        double p = paidValues[accident][prevDev];
        double i = incurredValues[accident][prevDev];
        double lr = paid.lrs.getValue(prevDev);
        double rate = paid.rho.getRatio(prevDev);
        
        if(Double.isNaN(p) || Double.isNaN(i) ||
           Double.isNaN(lr) || Double.isNaN(rate) || p == 0d)
            return Double.NaN;
        return p * (lr + lpsr * (i / p - rate));
    }
    
    private double calculateIncurredClaim(int accident, int development, double lpsr) {
        int prevDev = development-1;
        double p = paidValues[accident][prevDev];
        double i = incurredValues[accident][prevDev];
        double lr = incurred.lrs.getValue(prevDev);
        double rate = incurred.rho.getRatio(prevDev);
        
        if(Double.isNaN(p) || Double.isNaN(i) ||
           Double.isNaN(lr) || Double.isNaN(rate) || i == 0d)
            return Double.NaN;
        return i * (lr + lpsr * (p / i - rate));
    }
    
    private void fillRatios() {
        for(int a=0; a<accidents; a++) {
            for(int d=0; d<developments; d++) {
                double p = paidValues[a][d];
                double i = incurredValues[a][d];
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
        
//        private void setEstimatedValues(int accidents, int developments, double[][] values) {
//            super.accidents = accidents;
//            super.developments = developments;
//            super.values = values;
//        }
        
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
        
        void initValues() {
            super.accidents = MclEstimate.this.accidents;
            super.developments = MclEstimate.this.developments;
            values = new double[accidents][developments];
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
