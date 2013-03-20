package org.jreserve.linkratio.scale;

import org.jreserve.linkratio.scale.LinkRatioScale;
import org.jreserve.linkratio.scale.DefaultLinkRatioScaleSelection;
import org.jreserve.linkratio.scale.LinkRatioScaleExtrapolation;
import org.jreserve.linkratio.scale.LinkRatioScaleEstimator;
import org.jreserve.linkratio.scale.EmptyLinkRatioScaleEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleMinMaxEstimator;
import org.jreserve.linkratio.scale.LinkRatioScaleCaclulator;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestUtl;
import org.jreserve.TestData;
import org.jreserve.triangle.factor.DevelopmentFactors;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.linkratio.SimpleLinkRatio;
import org.jreserve.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class DefaultLinkRatioScaleSelectionTest {
    
    private final static double[] EXPECTED = {
        111.67022649, 55.88282502, 32.67349505, 23.27434456, 
         19.98988306,  1.01291755,  1.44741021
    };
    
    private LinkRatioScale source;
    private DefaultLinkRatioScaleSelection selection;
    private ChangeCounter changeCounter;
    
    public DefaultLinkRatioScaleSelectionTest() {
    }

    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA2);
        LinkRatio lr = new SimpleLinkRatio(new DevelopmentFactors(cik));
        source = new LinkRatioScaleCaclulator(lr);
        selection = new DefaultLinkRatioScaleSelection(source);
        changeCounter = new ChangeCounter();
        selection.addChangeListener(changeCounter);
    }

    @Test
    public void testSetDefaultMethod() {
        assertTrue(selection.getDefaultMethod() instanceof EmptyLinkRatioScaleEstimator);
        selection.setDefaultMethod(new LinkRatioScaleExtrapolation());
        assertTrue(selection.getDefaultMethod() instanceof LinkRatioScaleExtrapolation);
        assertEquals(0, changeCounter.getChangeCount());
        
        selection.setDefaultMethod(null);
        assertTrue(selection.getDefaultMethod() instanceof EmptyLinkRatioScaleEstimator);
    }

    @Test
    public void testGetDevelopmentCount() {
        assertEquals(source.getDevelopmentCount(), selection.getDevelopmentCount());
    }

    @Test
    public void testGetValue() {
        Map<Integer, LinkRatioScaleEstimator> estimators = new HashMap<Integer, LinkRatioScaleEstimator>(2);
        estimators.put(6, new LinkRatioScaleExtrapolation());
        estimators.put(7, new LinkRatioScaleMinMaxEstimator());
        selection.setMethods(estimators);
        
        assertEquals(Double.NaN, selection.getValue(-1), JRLibTestUtl.EPSILON);
        for(int d=0; d<EXPECTED.length; d++)
            assertEquals(EXPECTED[d], selection.getValue(d), JRLibTestUtl.EPSILON);
    }

    @Test
    public void testToArray() {
        Map<Integer, LinkRatioScaleEstimator> estimators = new HashMap<Integer, LinkRatioScaleEstimator>(2);
        estimators.put(6, new LinkRatioScaleExtrapolation());
        estimators.put(7, new LinkRatioScaleMinMaxEstimator());
        selection.setMethods(estimators);
        
        double[] found = selection.toArray();
        assertEquals(EXPECTED.length, found.length);
        assertArrayEquals(EXPECTED, found, JRLibTestUtl.EPSILON);
    }
}