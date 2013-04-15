package org.jrlib.bootstrap.residualgenerator;

import java.util.List;
import org.jrlib.util.random.Random;

/**
 * AbstractResidualGenerator is a utility class to reshuffle segmented
 * triangular residuals. Extending classes should also provide an 
 * implementation for {@link AbstractResidualSegment AbstractResidualSegment}.
 * 
 * The idea is that the user defines areas within a triangular data 
 * (called segments), within which residuals are reshuffled. Cells
 * not belonging to any defined segments, will belong to a default segment.
 * 
 * This approach gives the ability to fix residuals to a given location
 * or draw residuals separatly before/after a specified development
 * period.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractResidualGenerator<V, S extends AbstractResidualSegment<V>, R> {
    
    protected final Random rnd;
    
    protected S[] segments;
    protected int segmentCount;
    protected S defaultSegment;
    
    /**
     * Creates an instance with the given random generator.
     * 
     * @throws NullPointerException if `rnd` is null.
     */
    protected AbstractResidualGenerator(Random rnd) {
        if(rnd == null)
            throw new NullPointerException("Rnd is null!");
        this.rnd = rnd;
    }
    
    /**
     * Creates an initialised instance with the given random generator.
     * 
     * @see #initialize(java.lang.Object, java.util.List) 
     * @throws NullPointerException if on of the parameters is null.
     */
    protected AbstractResidualGenerator(Random rnd, R residuals, List<int[][]> segments) {
        this(rnd);
        initState(residuals, segments);
    }
    
    private void initState(R residuals, List<int[][]> segments) {
        if(segments == null) throw new NullPointerException("Segments is null!");
        if(residuals == null) throw new NullPointerException("Residuals is null!");
        initSegments(segments);
        fillSegments(residuals);
    }
    
    /**
     * Initialises the segments for the given values. All non null/NaN
     * values will be allocated to segments.
     * 
     * @throws NullPointerException if on of the parameters is null.
     */
    public void initialize(R residuals, List<int[][]> segments) {
        initState(residuals, segments);
    }
    
    private void initSegments(List<int[][]> segments) {
        defaultSegment = createSegment();
        segmentCount = segments.size();
        this.segments = createEmptySegmentArray(segmentCount);
        for(int i = 0; i<segmentCount; i++)
            this.segments[i] = createSegment(segments.get(i));
    }
    
    protected abstract S createSegment();
    
    protected abstract S[] createEmptySegmentArray(int count);
    
    protected abstract S createSegment(int[][] cells);
    
    private void fillSegments(R residuals) {
        for(S segment : segments)
            fillSegment(residuals, segment);
        defaultSegment.setValues(getValuesForDefaultSegment(residuals));
    }
    
    private void fillSegment(R residuals, S segment) {
        List<V> values = getValuesForSegment(residuals, segment);
        segment.setValues(values);
    }
    
    protected abstract List<V> getValuesForSegment(R residuals, S segment);
    
    protected abstract List<V> getValuesForDefaultSegment(R residuals);
    
    public S getSegment(int accident, int development) {
        for(int i=0; i<segmentCount; i++) {
            S s = segments[i];
            if(s.containsCell(accident, development))
                return s;
        }
        return defaultSegment;
    }

}
