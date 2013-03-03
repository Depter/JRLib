package org.jreserve.factor;

import org.jreserve.ChangeCounter;
import org.jreserve.JRLibTestSuite;
import org.jreserve.TestData;
import org.jreserve.triangle.Cell;
import org.jreserve.triangle.Triangle;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class FactorTriangleCorrectionTest {
    
    private final static Cell CELL = new Cell(1, 1);
    private final static double CORRECTION = 20d;
    
    private FactorTriangle source;
    private FactorTriangleCorrection correction;
    private ChangeCounter changeCounter;
    
    public FactorTriangleCorrectionTest() {
    }

    @Before
    public void setUp() {
        Triangle cik = TestData.getCummulatedTriangle(TestData.PAID);
        source = new DevelopmentFactors(cik);
        correction = new FactorTriangleCorrection(source, CELL, CORRECTION);
        changeCounter = new ChangeCounter();
        correction.addChangeListener(changeCounter);
    }

    @Test
    public void testGetCorrigatedAccident() {
        assertEquals(CELL.getAccident(), correction.getCorrigatedAccident());
    }

    @Test
    public void testGetCorrigatedDevelopment() {
        assertEquals(CELL.getDevelopment(), correction.getCorrigatedDevelopment());
    }

    @Test
    public void testGetCorrigatedValue() {
        assertEquals(CORRECTION, correction.getCorrigatedValue(), JRLibTestSuite.EPSILON);
    }

    @Test
    public void testSetCorrigatedValue() {
        correction.setCorrigatedValue(1d);
        assertEquals(1, changeCounter.getChangeCount());
    }

    @Test
    public void testGetValue() {
        int accidents = source.getAccidentCount();
        int developments = source.getDevelopmentCount();
        
        FactorTriangleCorrection outsider = new FactorTriangleCorrection(source, 1, developments-1, CORRECTION);
        for(int a=-1; a<accidents; a++) {
            for(int d=-1; d<developments; d++) {
                double sd = source.getValue(a, d);
                double expected = CELL.equals(a, d)? CORRECTION : sd;
                assertEquals(expected, correction.getValue(a, d), JRLibTestSuite.EPSILON);
                assertEquals(sd, outsider.getValue(a, d), JRLibTestSuite.EPSILON);
            }
        }
    }
}