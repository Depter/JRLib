package org.jreserve.bootstrap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultResidualGenerator implements ResidualGenerator {
    
    private final Random rnd;
    private Segment[] segments;
    private int segmentCount;
    private Segment defaultSegment;
    
    public DefaultResidualGenerator(Random rnd) {
        this.rnd = rnd;
    }
    
    public void initialize(ResidualTriangle residuals, List<int[][]> segments) {
        initSegments(segments);
        fillSegments(residuals);
        fillDefaultSegment(residuals);
    }
    
    private void initSegments(List<int[][]> segments) {
        defaultSegment = new Segment();
        segmentCount = segments.size();
        this.segments = new Segment[segmentCount];
        for(int i = 0; i<segmentCount; i++)
            this.segments[i] = new Segment(segments.get(i));
    }
    
    private void fillSegments(ResidualTriangle residuals) {
        for(Segment segment : segments)
            fillSegment(residuals, segment);
    }
    
    private void fillSegment(ResidualTriangle residuals, Segment segment) {
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
        segment.setResiduals(values);
    }
    
    private void fillDefaultSegment(ResidualTriangle residuals) {
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
        defaultSegment.setResiduals(values);
    }
    
    @Override
    public double getValue(int accident, int development) {
        return getSegment(accident, development).getResidual();
    }
    
    private Segment getSegment(int accident, int development) {
        for(int i=0; i<segmentCount; i++) {
            Segment s = segments[i];
            if(s.containsCell(accident, development))
                return s;
        }
        return defaultSegment;
    }
    
    private class Segment {
        
        private int[][] cells;
        private int cellCount;
        private double[] residuals;
        private int residualCount;
        
        Segment() {
        }
        
        Segment(int[][] cells) {
            this.cells = cells;
            this.cellCount = cells.length;
        }
        
        void setResiduals(List<Double> residuals) {
            residualCount = residuals.size();
            this.residuals = new double[residualCount];
            for(int i=0; i<residualCount; i++)
                this.residuals[i] = residuals.get(i);
        }
        
        boolean containsCell(int accident, int development) {
            //TODO Binary search
            for(int c = 0; c<cellCount; c++) {
                int[] cell = cells[c];
                if(cell[0] == accident && cell[1] == development)
                    return true;
            }
            return false;    
        }
        
        double getResidual() {
            return residualCount==0? 
                    Double.NaN : 
                    residuals[rnd.nextInt(residualCount)];
        }
    }
}
