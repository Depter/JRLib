package org.jreserve.estimate.mcl;

import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.factor.linkratio.LinkRatio;
import org.jreserve.factor.linkratio.SimpleLinkRatio;
import org.jreserve.factor.linkratio.curve.DefaultLinkRatioSmoothing;
import org.jreserve.factor.linkratio.curve.LinkRatioFunction;
import org.jreserve.factor.linkratio.curve.LinkRatioSmoothingSelection;
import org.jreserve.factor.linkratio.curve.UserInputLRFunction;
import org.jreserve.factor.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.factor.linkratio.scale.LinkRatioScale;
import org.jreserve.factor.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.factor.linkratio.scale.LinkRatioScaleSelection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Peter Decsi
 */
public class MCLLambdaCalculatorTest {

    private final static double PAID_LAMBDA = 0.15210439;
    private final static double INCURRED_LAMBDA = 0.40562407;
    
    private static LinkRatioScale PAID_SCALE;
    private static MclRho PAID_RHO;
    private static LinkRatioScale INCURRED_SCALE;
    private static MclRho INCURRED_RHO;
    
    public MCLLambdaCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PAID_SCALE = createLrScales(TestData.PAID, 1.05, 1.02);
        INCURRED_SCALE = createLrScales(TestData.INCURRED, 1.03, 1.01);
        PAID_RHO = createRhos(INCURRED_SCALE.getSourceLinkRatios(), PAID_SCALE.getSourceLinkRatios());
        INCURRED_RHO = createRhos(PAID_SCALE.getSourceLinkRatios(), INCURRED_SCALE.getSourceLinkRatios());        
    }
    
    private static LinkRatioScale createLrScales(String triangleName, double lr7, double lr8) {
        LinkRatio lr = new SimpleLinkRatio(TestData.getCummulatedTriangle(triangleName));
        LinkRatioSmoothingSelection smoothing = new DefaultLinkRatioSmoothing(lr);
        smoothing.setDevelopmentCount(9);
        smoothing.setMethod(createTail(lr7, lr8), 7, 8);
        
        LinkRatioScaleSelection scales = new DefaultLinkRatioScaleSelection(smoothing);
        scales.setMethod(new LinkRatioScaleMinMaxEstimator(), 7, 8);
        return scales;
    }
    
    private static LinkRatioFunction createTail(double lr7, double lr8) {
        UserInputLRFunction tail = new UserInputLRFunction();
        tail.setValue(7, lr7);
        tail.setValue(8, lr8);
        return tail;
    }
    
    private static MclRho createRhos(LinkRatio numerator, LinkRatio denominator) {
        MclRhoSelection rhos = new DefaultMclRhoSelection(numerator, denominator);
        rhos.setMethod(new MclRhoMinMaxEstimator(), 7, 8);
        return rhos;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PAID_RHO = null;
        PAID_SCALE = null;
        INCURRED_RHO = null;
        INCURRED_SCALE = null;
    }

    @Test
    public void testRecalculateLayer() {
        MclLambdaCalculator paid = new MclLambdaCalculator(PAID_RHO, PAID_SCALE);
        assertEquals(PAID_LAMBDA, paid.getLambda(), JRLibTestSuite.EPSILON);
        
        MclLambdaCalculator incurred = new MclLambdaCalculator(INCURRED_RHO, INCURRED_SCALE);
        assertEquals(INCURRED_LAMBDA, incurred.getLambda(), JRLibTestSuite.EPSILON);
    }
}