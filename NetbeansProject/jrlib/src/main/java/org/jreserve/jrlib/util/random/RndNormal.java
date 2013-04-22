package org.jreserve.jrlib.util.random;

/**
 * Utility class to generate random normal variables, with the given
 * mean and variance.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class RndNormal {
    
    private final Random rnd;
    private double next;
    private boolean hasNext = false;
    
    /**
     * Creates an instance which uses the given {@link Random Random} instance.
     * 
     * @throws NullPointerException if `random` is null.
     */
    public RndNormal(Random random) {
        if(random == null)
            throw new NullPointerException("Random is null!");
        this.rnd = random;
    }
    
    public double nextNormalFromVariance(double mean, double variance) {
        double sigma = Math.sqrt(variance);
        return nextNormal(mean, sigma);
    }
    
    public double nextNormal(double mean, double sigma) {
        double srnd;
        if(hasNext) {
            hasNext = false;
            srnd = next;
        } else {
            srnd = nextStandardNormal();
        }
        return mean + srnd * sigma;
    }

    private double nextStandardNormal() {
        double v1, v2, sum;
        do {
            v1 = 2 * rnd.nextDouble() - 1;
            v2 = 2 * rnd.nextDouble() - 1;
            sum = square(v1) + square(v2);
        } while(sum >= 1d);
        
        double srnd = Math.sqrt(-2d * Math.log(sum) / sum);
        next = srnd * v1;
        hasNext = true;
        return srnd * v2;
    }
    
    private double square(double d) {
        return d * d;
    }
}
