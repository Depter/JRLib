package org.jreserve.triangle;

import org.jreserve.AbstractCalculationData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleOutlierFilter extends AbstractCalculationData<Triangle> {

    private final static double DEFAULT_TRESHOLD = 1.5;
    
    private boolean[][] isOutlier;
    private double treshold;
    
    public TriangleOutlierFilter(Triangle source) {
        this(source, DEFAULT_TRESHOLD);
    }

    public TriangleOutlierFilter(Triangle source, double treshold) {
        super(source);
        this.treshold = (treshold < 0d)? 0d : treshold;
        doRecalculate();
    }
    
    public double getTreshold() {
        return treshold;
    }
    
    public void setTreshold(double treshold) {
        this.treshold = (treshold < 0d)? 0d : treshold;
        recalculateLayer();
        fireChange();
    }
    
    public int getAccidentCount() {
        return isOutlier.length;
    }
    
    public int getDevelopmentCount() {
        return getAccidentCount() == 0?
                0:
                isOutlier[0].length;
    }

    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return isOutlier[accident].length;
        return 0;
    }
    
    private boolean withinBounds(int accident) {
        return accident>=0 && accident < isOutlier.length;
    }
    
    public boolean[][] toArray() {
        int accidents = isOutlier.length;
        boolean[][] copy = new boolean[accidents][];
        for(int a=0; a<accidents; a++)
            copy[a] = toArray(a);
        return copy;
    }
    
    private boolean[] toArray(int accident) {
        boolean[] data = isOutlier[accident];
        boolean[] copy = new boolean[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);
        return copy;
    }
    
    public boolean isOutlier(int accident, int development) {
        return withinBounds(accident, development) &&
               isOutlier[accident][development];
    } 
    
    private boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               development>=0 &&
               development < isOutlier[accident].length;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        double[] means = getMeans();
        double[] sigmas = getSigmas(means);
        createData(means, sigmas);
    }
    
    private double[] getMeans() {
        int developments = source.getDevelopmentCount();
        double[] means = new double[developments];
        for(int d=0; d<developments; d++)
            means[d] = getMean(d);
        return means;
    }
    
    private double getMean(int development) {
        double sum = 0d;
        int n = 0;
        int a = -1;
        while(source.getDevelopmentCount(++a) > development) {
            double value = source.getValue(a, development);
            if(!Double.isNaN(value)) {
                sum += value;
                n++;
            }
        }
        
        return (n>0)? sum / (double)n : Double.NaN;
    }
    
    private double[] getSigmas(double[] means) {
        int developments = means.length;
        double[] sigmas = new double[developments];
        for(int d=0; d<developments; d++)
            sigmas[d] = getSigma(d, means[d]);
        return sigmas;
    }
    
    private double getSigma(int development, double mean) {
        double sum = 0d;
        int n = 0;
        int a=-1;
        while(source.getDevelopmentCount(++a) > development) {
            double value = source.getValue(a, development);
            if(!Double.isNaN(value)) {
                sum += Math.pow(value - mean, 2d);
                n++;
            }
        }
        return (--n > 0)? Math.sqrt(sum / (double)n) : Double.NaN;
    }
    
    private void createData(double[] means, double[] sigmas) {
        int accidents = source.getAccidentCount();
        isOutlier = new boolean[accidents][];
        for(int a=0; a<accidents; a++) {
            int devs = source.getDevelopmentCount(a);
            isOutlier[a] = new boolean[devs];
            for(int d=0; d<devs; d++) {
                double value = source.getValue(a, d);
                isOutlier[a][d] = isOutlier(value, means[d], sigmas[d]);
            }
        }
    }
    
    private boolean isOutlier(double value, double mean, double sigma) {
        return !Double.isNaN(sigma) &&
               !Double.isNaN(mean) &&
               !Double.isNaN(value) &&
               Math.abs(value - mean) > treshold * sigma;
    }
}
