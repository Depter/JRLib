package org.jrlib.bootstrap.residualgenerator;

import java.util.ArrayList;
import java.util.List;
import org.jrlib.triangle.Triangle;
import org.jrlib.util.random.Random;

/**
 * Instances of DoubleResidualGenerator generate residuals from a triangle,
 * containing double values.
 * 
 * @see AbstractResidualGenerator
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleResidualGenerator<E extends Triangle> extends AbstractResidualGenerator<Double, DoubleResidualSegment, E> {
    
    /**
     * Creates an instance with the given random generator.
     * 
     * @throws NullPointerException if `rnd` is null.
     */
    public DoubleResidualGenerator(Random rnd) {
        super(rnd);
    }

    /**
     * Creates an initialised instance with the given random generator.
     * 
     * @see #initialize(java.lang.Object, java.util.List) 
     * @throws NullPointerException if on of the parameters is null.
     */
    public DoubleResidualGenerator(Random rnd, E residuals, List<int[][]> segments) {
        super(rnd, residuals, segments);
    }
    
    @Override
    protected DoubleResidualSegment createSegment() {
        return new DoubleResidualSegment(rnd);
    }

    @Override
    protected DoubleResidualSegment[] createEmptySegmentArray(int count) {
        return new DoubleResidualSegment[count];
    }

    @Override
    protected DoubleResidualSegment createSegment(int[][] cells) {
        return new DoubleResidualSegment(rnd, cells);
    }

    @Override
    protected List<Double> getValuesForSegment(E residuals, DoubleResidualSegment segment) {
        List<Double> values = new ArrayList<Double>();
        int accidents = residuals.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double r = residuals.getValue(a, d);
                if(!Double.isNaN(r) && segment.containsCell(a, d))
                    values.add(r);
            }
        }
        return values;
    }

    @Override
    protected List<Double> getValuesForDefaultSegment(E residuals) {
        List<Double> values = new ArrayList<Double>();
        int accidents = residuals.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                double r = residuals.getValue(a, d);
                if(!Double.isNaN(r) && getSegment(a, d) == defaultSegment)
                    values.add(r);
            }
        }
        return values;
    }
    
    /**
     * Generates a residual for the given accident and development
     * period.
     */
    public double getValue(int accident, int development) {
        DoubleResidualSegment s = getSegment(accident, development);
        return getSegment(accident, development).getResidual();
    }
}
