package org.jreserve.triangle;

import static org.jreserve.JRLibTestSuite.EPSILON;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Peter Decsi
 */
public class AbstractTriangleModificationTest {

    private AbstractTriangleModification data;
    private Triangle source;
    
    public AbstractTriangleModificationTest() {
    }

    @Before
    public void setUp() {
        source = InputTriangleTest.createData();
        data = new AbstractTriangleModificationImpl(source);
    }

    @Test
    public void testGetAccidentCount() {
        assertEquals(source.getAccidentCount(), data.getAccidentCount());
    }

    @Test
    public void testGetDevelopmentCount_0args() {
        assertEquals(source.getDevelopmentCount(), data.getDevelopmentCount());
    }

    @Test
    public void testGetDevelopmentCount_int() {
        assertEquals(0, data.getDevelopmentCount(-1));
        int accidents = source.getAccidentCount();
        
        for(int a=0; a<accidents; a++)
            assertEquals(source.getDevelopmentCount(a), data.getDevelopmentCount(a));
        
        assertEquals(0, data.getDevelopmentCount(accidents));
    }

    @Test
    public void testGetValue() {
        int accidents = source.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double e = source.getValue(a, d);
                double f = data.getValue(a, d);
                assertEquals(e, f, EPSILON);
            }
        }
    }
    
    @Test
    public void testToArray() {
        double[][] values = data.toArray();
        int accidents = source.getAccidentCount();
        assertEquals(accidents, values.length);
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            assertEquals(devs, values[a].length);
            
            for(int d=0; d<devs; d++)
                assertEquals(source.getValue(a, d), values[a][d], EPSILON);
        }
    }

    public class AbstractTriangleModificationImpl extends AbstractTriangleModification<Triangle> {

        public AbstractTriangleModificationImpl(Triangle source) {
            super(source);
        }
        
        @Override
        protected void recalculateLayer() {
        }
    }

}