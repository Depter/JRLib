package org.jrlib.bootstrap.mcl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jrlib.bootstrap.residualgenerator.AbstractResidualGenerator;
import org.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclResidualGenerator extends AbstractResidualGenerator<MclResidualCell, MclResidualSegment, MclResidualBundle> {
    
    /**
     * Creates an instance with the given random generator.
     * 
     * @throws NullPointerException if `rnd` is null.
     */
    public MclResidualGenerator(Random rnd) {
        super(rnd);
    }

    /**
     * Creates an initialised instance with the given random generator.
     * 
     * @see #initialize(java.lang.Object, java.util.List) 
     * @throws NullPointerException if on of the parameters is null.
     */
    public MclResidualGenerator(Random rnd, MclResidualBundle residuals) {
        super(rnd, residuals, Collections.EMPTY_LIST);
    }

    /**
     * Creates an initialised instance with the given random generator.
     * 
     * @see #initialize(java.lang.Object, java.util.List) 
     * @throws NullPointerException if on of the parameters is null.
     */
    public MclResidualGenerator(Random rnd, MclResidualBundle residuals, List<int[][]> segments) {
        super(rnd, residuals, segments);
    }
    
    @Override
    protected MclResidualSegment createSegment() {
        return new MclResidualSegment(rnd);
    }

    @Override
    protected MclResidualSegment[] createEmptySegmentArray(int count) {
        return new MclResidualSegment[count];
    }

    @Override
    protected MclResidualSegment createSegment(int[][] cells) {
        return new MclResidualSegment(rnd, cells);
    }

    @Override
    protected List<MclResidualCell> getValuesForSegment(MclResidualBundle residuals, MclResidualSegment segment) {
        List<MclResidualCell> values = new ArrayList<MclResidualCell>();
        int accidents = residuals.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                if(segment.containsCell(a, d) && !residuals.isNaN(a, d))
                    values.add(new MclResidualCell(a, d, residuals));
            }
        }
        return values;
    }

    @Override
    protected List<MclResidualCell> getValuesForDefaultSegment(MclResidualBundle residuals) {
        List<MclResidualCell> values = new ArrayList<MclResidualCell>();
        int accidents = residuals.getAccidentCount();
        for(int a=0; a<accidents; a++) {
            int devs = residuals.getDevelopmentCount(a);
            for(int d=0; d<devs; d++) {
                if(getSegment(a, d) == defaultSegment && !residuals.isNaN(a, d))
                    values.add(new MclResidualCell(a, d, residuals));
            }
        }
        return values;
    }
    
    /**
     * Generates a cell for the given accident and development
     * period.
     */
    public MclResidualCell getCell(int accident, int development) {
        MclResidualSegment s = getSegment(accident, development);
        return getSegment(accident, development).getCell();
    }
}
