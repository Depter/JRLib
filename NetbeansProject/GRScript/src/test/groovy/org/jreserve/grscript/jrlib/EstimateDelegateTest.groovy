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

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.jreserve.grscript.ScriptExecutor
import org.jreserve.grscript.TestDataDelegate
import org.jreserve.grscript.util.PrintDelegate
import org.jreserve.jrlib.estimate.Estimate
import org.jreserve.jrlib.estimate.ChainLadderEstimate
import org.jreserve.jrlib.estimate.AverageCostEstimate
import org.jreserve.jrlib.estimate.BornhuetterFergusonEstimate
import org.jreserve.jrlib.estimate.ExpectedLossRatioEstimate
import org.jreserve.jrlib.estimate.CapeCodEstimate
import org.jreserve.jrlib.estimate.MackEstimate
import org.jreserve.jrlib.estimate.mcl.MclEstimateBundle

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EstimateDelegateTest {
    
    private final static String BASE_SCRIPT = 
        "paidData = apcPaid()\n"                            +   //1
        "cummulate(paidData)\n"                             +   //2
        "paid = triangle(paidData)\n"                       +   //3
        "exposure = vector(apcExposure())\n"                +   //4
        "lrs = smooth(linkRatio(paid), 10)\n"               +   //5
        "scale = scale(lrs)\n"                              +   //6
        "lossRatio = vector(exposure.getLength(), 16.5)\n"  +   //7
        "lrSE = standardError(scale)\n"                     ;   //8

    private ScriptExecutor executor
    
    @Before
    void setUp() {
        executor = new ScriptExecutor()
        executor.addFunctionProvider(new TestDataDelegate())
        executor.addFunctionProvider(new PrintDelegate())
        executor.addFunctionProvider(new TriangleUtilDelegate())
        executor.addFunctionProvider(new ClaimTriangleDelegate())
        executor.addFunctionProvider(new VectorDelegate())
        executor.addFunctionProvider(new LinkRatioDelegate())
        executor.addFunctionProvider(new LinkRatioCurveDelegate())
        executor.addFunctionProvider(new LinkRatioScaleDelegate())
        executor.addFunctionProvider(new LRResidualTriangleDelegate())
        executor.addFunctionProvider(new LinkRatioSEDelegate())
        executor.addFunctionProvider(new ClaimRatioDelegate())
        executor.addFunctionProvider(new ClaimRatioScaleDelegate())
        executor.addFunctionProvider(new CRResidualTriangleDelegate())
        executor.addFunctionProvider(new EstimateDelegate())
    }
    
    @Test
    public void testChainLadder() {
        Estimate estimate = runScript("estimate = chainLadderEstimate(lrs)\n")
        
        assertTrue(estimate instanceof ChainLadderEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    private Estimate runScript(String script) {
        script = BASE_SCRIPT + script
        return (Estimate) executor.runScript(script)
    }
    
    @Test
    public void testChainLadder_Map() {
        Estimate estimate = runScript("estimate = estimate(method:\"Chain-Ladder\", \"link-ratios\":lrs)\n")
        
        assertTrue(estimate instanceof ChainLadderEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testAverageCost() {
        String script = 
        "nocData = apcNoC()\n"                      +
        "cummulate(nocData)\n"                      +
        "noc = triangle(nocData)\n"                 +
        "cost = triangle(paidData / nocData)\n"     +
        "nLrs = smooth(linkRatio(noc), 10)\n"       +
        "cLrs = smooth(linkRatio(cost), 10)\n"      +
        "estimate = averageCostEstimate(nLrs, cLrs)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof AverageCostEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testAverageCost_Map() {
        String script = 
        "nocData = apcNoC()\n"                      +
        "cummulate(nocData)\n"                      +
        "noc = triangle(nocData)\n"                 +
        "cost = triangle(paidData / nocData)\n"     +
        "nLrs = smooth(linkRatio(noc), 10)\n"       +
        "cLrs = smooth(linkRatio(cost), 10)\n"      +
        "estimate = estimate(method:\"Average-Cost\", numbers:nLrs, costs:cLrs)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof AverageCostEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testBornhuetterFergusson() {
        String script = 
        "estimate = bornhuetterFergusonEstimate(lrs, exposure, lossRatio)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof BornhuetterFergusonEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testBornhuetterFergusson_Map() {
        String script = 
        "estimate = estimate(method:\"Bornhuetter-Ferguson\", lrs:lrs, exposure:exposure, LossRatio:lossRatio)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof BornhuetterFergusonEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testExpectedLossRatio() {
        String script = 
        "estimate = expectedLossRatioEstimate(lrs, exposure, lossRatio)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof ExpectedLossRatioEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testExpectedLossRatio_Map() {
        String script = 
        "estimate = estimate(method:\"Expected Loss Ratio\", lrs:lrs, exposure:exposure, LossRatio:lossRatio)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof ExpectedLossRatioEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testCapeCod() {
        String script = 
        "estimate = capeCodEstimate(lrs, exposure)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof CapeCodEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testCapeCod_Map() {
        String script = 
        "estimate = estimate(method:\"Cape-Cod\", lrs:lrs, exposure:exposure)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof CapeCodEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testMack() {
        String script = 
        "estimate = mackEstimate(lrSE)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof MackEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testMack_Map() {
        String script = 
        "estimate = estimate(method:\"Mack\", lrse:lrSE)";
        
        Estimate estimate = runScript(script)
        
        assertTrue(estimate instanceof MackEstimate)

        assertEquals(8, estimate.getAccidentCount())
        assertEquals(11, estimate.getDevelopmentCount())
        for(a in 0..<8)
            for(d in 0..<11)
                assertFalse(Double.isNaN(estimate.getValue(a, d)))
    }
    
    @Test
    public void testMcl() {
        String script = 
        "//Create paid data\n"                              +   //1
        "paidData = apcPaid()\n"                            +   //2
        "cummulate(paidData)\n"                             +   //3
        "paid = triangle(paidData)\n"                       +   //4
        "lrPaid = smooth(linkRatio(paid), 10)\n"            +   //5
        "def pScale = scale(lrPaid)\n"                      +   //6
        "\n"                                                +   //7
        "//Create incurred data\n"                          +   //8
        "incurredData = apcIncurred()\n"                    +   //9
        "cummulate(incurredData)\n"                         +   //10
        "incurred = triangle(incurredData)\n"               +   //11
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"    +   //12
        "def iScale = scale(lrIncurred)\n"                  +   //13
        "\n"                                                +   //14
        "//Claim ratios\n"                                  +   //15
        "crsPperI = ratios(lrPaid, lrIncurred)\n"           +   //16
        "crsIperP = ratios(lrIncurred, lrPaid)\n"           +   //17
        "def scalePperI = scale(crsPperI)\n"                +   //18
        "def scaleIperP = scale(crsIperP)\n"                +   //19
        "\n"                                                +   //20
        "//create the bundle\n"                             +   //21
        "bundle = munichChainLadderEstimate {\n"            +   //22
        "   lrPaid residuals(pScale)\n"                     +   //23
        "   lrIncurred residuals(iScale)\n"                 +   //24
        "   crPaid residuals(scaleIperP)\n"                 +   //25
        "   crIncurred residuals(scalePperI)\n"             +   //26
        "}\n"                                               ;   //27
        
        MclEstimateBundle bundle = (MclEstimateBundle) executor.runScript(script)
        
        Estimate paid = bundle.getPaidEstimate()
        Estimate incurred = bundle.getIncurredEstimate()
        
        assertEquals(8, paid.getAccidentCount())
        assertEquals(8, incurred.getAccidentCount())
        assertEquals(11, paid.getDevelopmentCount())
        assertEquals(11, incurred.getDevelopmentCount())
        for(a in 0..<8) {
            for(d in 0..<11) {
                assertFalse(Double.isNaN(paid.getValue(a, d)))
                assertFalse(Double.isNaN(incurred.getValue(a, d)))
            }
        }
    }

    @Test
    public void testComposite() {
        String script = 
        "cl = CLEstimate(lrs)\n"                            +
        "cc = CCEstimate(lrs, exposure)\n"                  +
        "compositeEstimate(cl, cl, cl, cl, cl, cl, cc, cc)" ;
        Estimate estimate = runScript(script)
        assertTrue(estimate instanceof org.jreserve.jrlib.estimate.ProcessSimulatorEstimate)
    }
    
    @Test
    public void testPrintData() {
        String script =
        "printData(estimate(method:\"Chain-Ladder\", \"link-ratios\":lrs))\n";
        
        StringWriter writer = new StringWriter()
        executor.setOutput(new PrintWriter(writer))
        executor.runScript(BASE_SCRIPT + script)
        
        String str = writer.toString()
        assertTrue(0 != str.length())
    }
    
    @Test
    public void testPrintData_Mack() {
        String script =
        "printData(estimate(method:\"Mack\", lrse:lrSE))\n";
        
        StringWriter writer = new StringWriter()
        executor.setOutput(new PrintWriter(writer))
        executor.runScript(BASE_SCRIPT + script)
        
        String str = writer.toString()
        assertTrue(0 != str.length())
    }
    
    @Test
    public void testPrintData_Mcl() {
        String script = 
        "//Create paid data\n"                              +   //1
        "paidData = apcPaid()\n"                            +   //2
        "cummulate(paidData)\n"                             +   //3
        "paid = triangle(paidData)\n"                       +   //4
        "lrPaid = smooth(linkRatio(paid), 10)\n"            +   //5
        "def pScale = scale(lrPaid)\n"                      +   //6
        "\n"                                                +   //7
        "//Create incurred data\n"                          +   //8
        "incurredData = apcIncurred()\n"                    +   //9
        "cummulate(incurredData)\n"                         +   //10
        "incurred = triangle(incurredData)\n"               +   //11
        "lrIncurred = smooth(linkRatio(incurred), 10)\n"    +   //12
        "def iScale = scale(lrIncurred)\n"                  +   //13
        "\n"                                                +   //14
        "//Claim ratios\n"                                  +   //15
        "crsPperI = ratios(lrPaid, lrIncurred)\n"           +   //16
        "crsIperP = ratios(lrIncurred, lrPaid)\n"           +   //17
        "def scalePperI = scale(crsPperI)\n"                +   //18
        "def scaleIperP = scale(crsIperP)\n"                +   //19
        "\n"                                                +   //20
        "//create the bundle\n"                             +   //21
        "bundle = munichChainLadderEstimate {\n"            +   //22
        "   lrPaid residuals(pScale)\n"                     +   //23
        "   lrIncurred residuals(iScale)\n"                 +   //24
        "   crPaid residuals(scaleIperP)\n"                 +   //25
        "   crIncurred residuals(scalePperI)\n"             +   //26
        "}\n"                                               +   //27
        "printData(bundle)\n"                               ;   //28
        
        StringWriter writer = new StringWriter()
        executor.setOutput(new PrintWriter(writer))
        executor.runScript(script)
        
        String str = writer.toString()
        assertTrue(0 != str.length())
    }
}

