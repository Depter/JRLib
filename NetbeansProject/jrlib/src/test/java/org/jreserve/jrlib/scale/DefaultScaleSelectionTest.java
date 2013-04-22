package org.jreserve.jrlib.scale;

import org.jreserve.jrlib.scale.DefaultScaleEstimator;
import org.jreserve.jrlib.scale.MinMaxScaleEstimator;
import org.jreserve.jrlib.scale.ScaleExtrapolation;
import org.jreserve.jrlib.scale.ScaleEstimator;
import org.jreserve.jrlib.scale.DefaultScaleSelection;
import java.util.HashMap;
import java.util.Map;
import org.jreserve.jrlib.ChangeCounter;
import org.jreserve.jrlib.TestConfig;
import org.jreserve.jrlib.TestData;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultScaleSelectionTest {
    
    private final static double[] EXPECTED = {
        111.67022649, 55.88282502, 32.67349505, 23.27434456, 
         19.98988306,  1.01291755,  1.44741021
    };
    
    private LinkRatioScaleInput source;
    private DefaultScaleSelection<LinkRatioScaleInput> selection;
    private ChangeCounter changeCounter;

    @Before
    public void setUp() {
        ClaimTriangle cik = TestData.getCummulatedTriangle(TestData.MACK_DATA2);
        source = new LinkRatioScaleInput(cik);
        selection = new DefaultScaleSelection<LinkRatioScaleInput>(source);
        changeCounter = new ChangeCounter();
        selection.addChangeListener(changeCounter);
    }

    @Test
    public void testSetDefaultMethod() {
        assertTrue(selection.getDefaultMethod() instanceof DefaultScaleEstimator);
        selection.setDefaultMethod(new ScaleExtrapolation<LinkRatioScaleInput>());
        assertTrue(selection.getDefaultMethod() instanceof ScaleExtrapolation);
        assertEquals(0, changeCounter.getChangeCount());
        
        selection.setDefaultMethod(null);
        assertTrue(selection.getDefaultMethod() instanceof DefaultScaleEstimator);
    }

    @Test
    public void testGetLength() {
        assertEquals(source.getDevelopmentCount(), selection.getLength());
    }

    @Test
    public void testGetValue() {
        Map<Integer, ScaleEstimator<LinkRatioScaleInput>> estimators = new HashMap<Integer, ScaleEstimator<LinkRatioScaleInput>>(2);
        estimators.put(6, new ScaleExtrapolation<LinkRatioScaleInput>());
        estimators.put(7, new MinMaxScaleEstimator<LinkRatioScaleInput>());
        selection.setMethods(estimators);
        
        assertEquals(Double.NaN, selection.getValue(-1), TestConfig.EPSILON);
        for(int d=0; d<EXPECTED.length; d++)
            assertEquals(EXPECTED[d], selection.getValue(d), TestConfig.EPSILON);
    }

    @Test
    public void testToArray() {
        Map<Integer, ScaleEstimator<LinkRatioScaleInput>> estimators = new HashMap<Integer, ScaleEstimator<LinkRatioScaleInput>>(2);
        estimators.put(6, new ScaleExtrapolation<LinkRatioScaleInput>());
        estimators.put(7, new MinMaxScaleEstimator<LinkRatioScaleInput>());
        selection.setMethods(estimators);
        
        double[] found = selection.toArray();
        assertEquals(EXPECTED.length, found.length);
        assertArrayEquals(EXPECTED, found, TestConfig.EPSILON);
    }
}
