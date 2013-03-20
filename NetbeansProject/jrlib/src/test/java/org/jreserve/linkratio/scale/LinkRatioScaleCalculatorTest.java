package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.scale.LinkRatioScaleCaclulator;
import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.linkratio.DefaultLinkRatioSelection;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class LinkRatioScaleCalculatorTest {
    
    private final static double[] EXPECTED = {
        5.09840661, 2.81129417, 3.34113131, 1.55856978, 2.10383801, 1.50026769, 
        4.44352222, 0.83591027, 0.61983092, 0.65462278, 0.43663965, 0.73083009, 
        0.47413736, 0.50983808, 0.48409495, 0.22359615, 0.40111860, 0.09013813, 
        0.32243600, 0.13180775, 0.18606523, 0.23206291, 0.16864792, 0.12885120, 
        0.12982162, 0.30938220, 0.22534841, 0.07189430, 0.03490764, 0.09008184, 
        0.24988217, 0.05921756, 0.00232665, 0.03149351, 0.07154208, 0.01668870, 
        0.00337297, 0.06316792, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 
        Double.NaN, Double.NaN
    };
    
    private ClaimTriangle cik;
    private LinkRatio source;
    private LinkRatioScaleCaclulator scale;
    private ChangeCounter changeCounter;
    
    public LinkRatioScaleCalculatorTest() {
    }

    @Before
    public void setUp() {
        cik = TestData.getCummulatedTriangle(TestData.Q_PAID);
        source = new DefaultLinkRatioSelection(new DevelopmentFactors(cik));
        scale = new LinkRatioScaleCaclulator(source);
        changeCounter = new ChangeCounter();
        scale.addChangeListener(changeCounter);
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(source.getDevelopmentCount(), scale.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        int developments = EXPECTED.length;
        assertEquals(Double.NaN, scale.getValue(-1), JRLibTestUtl.EPSILON);
        
        for(int d=0; d<developments; d++)
            assertEquals(EXPECTED[d], scale.getValue(d), JRLibTestUtl.EPSILON);
        
        assertEquals(Double.NaN, scale.getValue(developments), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testToArray() {
        double[] found = scale.toArray();
        assertArrayEquals(EXPECTED, found, JRLibTestUtl.EPSILON);
    }
}