package org.jreserve.bootstrap.odp;

import java.util.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class DefaultResidualGenerator implements ResidualGenerator {

    private long seed;
    private Random rnd;
    
    private Segment defaultSegment = new Segment();
    private List<Segment> segments = new ArrayList<Segment>();
    private int segmentCount;
    
    public void addSegment(int firstAccident, int firstDevelopment, int lastAccident, int lastDevelopment) {
        checkCoordinates(firstAccident, firstDevelopment, lastAccident, lastDevelopment);
        checkNewSegment(firstAccident, firstDevelopment);
        segments.add(new Segment(firstAccident, lastAccident, firstDevelopment, lastDevelopment));
    }
    
    private void checkCoordinates(int firstAccident, int firstDevelopment, int lastAccident, int lastDevelopment) {
        if(firstAccident < 0)
            throw new IllegalArgumentException("First accident is less then 0! "+firstAccident);
        if(lastAccident < firstAccident)
            throw new IllegalArgumentException("Last accident is less then lastAccident! ["+firstAccident+"; "+lastAccident+"]");
        if(firstDevelopment < 0)
            throw new IllegalArgumentException("First development is less then 0! "+firstDevelopment);
        if(lastDevelopment < firstDevelopment)
            throw new IllegalArgumentException("Last development is less then last development! ["+firstDevelopment+"; "+lastDevelopment+"]");
    }
    
    private void checkNewSegment(int firstAccident, int firstDevelopment, int lastAccident, int lastDevelopment) {
        for(int a=firstAccident; a<=lastAccident; a++)
            for(int d=firstDevelopment; d<=lastDevelopment; d++)
                checkNewSegment(a, d);
    }
    
    private void checkNewSegment(int accident, int development) {
        Segment old = getSegment(accident, development);
        if(old != defaultSegment)
            throw new IllegalArgumentException("New segment would overlap with existing segment: "+old);
    }
    
    public void initialize(double[][] residuals) {
        int accidents = residuals.length;
        for(int a=0; a<accidents; a++)
            initialize(a, residuals[a]);
    }
    
    private void initialize(int accident, double[] residuals) {
        int devs = residuals.length;
        for(int d=0; d<devs; d++) {
            double r = residuals[d];
            if(!Double.isNaN(r))
                getSegment(accident, d).addResidual(r);
        }
    }
    
    @Override
    public double getResidual(int accidnet, int development) {
        Segment segment = getSegment(accidnet, development);
        double scaled = segment.getResidual();
        return unScaleResidual(accidnet, development, scaled);
    }
    
    private Segment getSegment(int accident, int development) {
        for(int i=0; i<segmentCount; i++) {
            Segment segment = segments.get(i);
            if(segment.containsCell(accident, development))
                return segment;
        }
        return defaultSegment;
    }
    
    abstract double unScaleResidual(int accidnet, int development, double residual);
    
    private class Segment {
        private int firstAccidnet = -1;
        private int lastAccident = -1;
        private int firstDevelopment = -1;
        private int lastDevelopment = -1;
        
        private List<Double> tempResiduals = new ArrayList<Double>();
        
        private double[] residuals;
        private int residualCount;
        
        Segment() {
        }
        
        Segment(int firstAccident, int lastAccident, int firstDevelopment, int lastDevelopment) {
            this.firstAccidnet = firstAccident;
            this.lastAccident = lastAccident;
            this.firstDevelopment = firstDevelopment;
            this.lastDevelopment = lastDevelopment;
        }
        
        void addResidual(double residual) {
            tempResiduals.add(residual);
        }
        
        void initializeSegment() {
            if(tempResiduals.isEmpty())
                throw new IllegalStateException(this + " contains no residuals!");
            fillResiduals();
            tempResiduals = null;
        }
        
        private void fillResiduals() {
            residualCount = tempResiduals.size();
            residuals = new double[residualCount];
            for(int i=0; i<residualCount; i++)
                residuals[i] = tempResiduals.get(i);
        }
        
        boolean containsCell(int accidnet, int development) {
            return firstAccidnet <= accidnet && accidnet <= lastAccident &&
                   firstDevelopment <= development && development <= lastDevelopment;
        }
        
        double getResidual() {
            int index = rnd.nextInt(residualCount);
            return residuals[index];
        }
        
        @Override
        public boolean equals(Object o) {
            return (o instanceof Segment) &&
                   equals((Segment) o);
        }
        
        public boolean equals(Segment s) {
            return firstAccidnet == s.firstAccidnet &&
                   lastAccident == s.lastAccident &&
                   firstDevelopment == s.firstDevelopment &&
                   lastDevelopment == s.lastDevelopment;
        }
        
        @Override
        public int hashCode() {
            int hash = 31 + firstAccidnet;
            hash = 17 * hash + lastAccident;
            hash = 17 * hash + firstDevelopment;
            return 17 * hash + lastDevelopment;
        }
        
        @Override
        public String toString() {
            return String.format("Segment begin=[%d;%sd], end=[%d; %d]", 
                    firstAccidnet, firstDevelopment,
                    lastAccident, lastDevelopment);
        }
    }
}
