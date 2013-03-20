package org.jreserve.bootstrap;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.triangle.Cell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ResidualGeneratorFactory {

    private Random rnd;
    private Random.Factory rndFactory;
    private ResidualTriangle residuals;
    private List<List<Cell>> cells = new ArrayList<List<Cell>>();
    
    public ResidualGeneratorFactory(Random rnd, Random.Factory rndFactory, ResidualTriangle residuals) {
        this.rnd = rnd;
        this.rndFactory = rndFactory;
        this.residuals = residuals;
    }
    
    public void addSegment(List<Cell> cells) {
        removeCellsFromExistingSegments(cells);
        this.cells.add(cells);
    }
    
    private void removeCellsFromExistingSegments(List<Cell> cells) {
        for(List<Cell> segment : this.cells)
            segment.removeAll(cells);
    }
    
    public DefaultResidualGenerator[] build(int count) {
        DefaultResidualGenerator[] generators = new DefaultResidualGenerator[count];
        for(int i=0; i<count; i++)
            generators[i] = build();
        return generators;
    }
    
    public DefaultResidualGenerator build() {
        DefaultResidualGenerator generator = new DefaultResidualGenerator(nextRandom());
        generator.initialize(residuals, createSegments());
        return generator;
    }
    
    private List<int[][]> createSegments() {
        List<int[][]> result = new ArrayList<int[][]>(cells.size());
        for(List<Cell> segment : cells)
            result.add(createSegment(segment));
        return result;
    }
    
    private int[][] createSegment(List<Cell> segment) {
        int size = segment.size();
        int[][] result = new int[size][2];
        for(int i=0; i<size; i++)
            result[i] = createCell(segment.get(i));
        return result;
    }
    
    private int[] createCell(Cell cell) {
        return new int[] {
            cell.getAccident(), 
            cell.getDevelopment()
        };
    }
    
    private Random nextRandom() {
        long seed = rnd.nextLong();
        return rndFactory.createRandom(seed);
    }
}
