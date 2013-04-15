package org.jrlib.util.random;

/**
 * Implementation of the {@link Random Random} interface,
 * which wraps a {@link java.util.Random java.util.Random} instance.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class JavaRandom implements Random {

    private final java.util.Random rnd;

    public JavaRandom() {
        rnd = new java.util.Random();
    }
    
    public JavaRandom(long seed) {
        rnd = new java.util.Random(seed);
    }
    
    @Override
    public int nextInt(int n) {
        return rnd.nextInt(n);
    }

    @Override
    public long nextLong() {
        return rnd.nextLong();
    }
    
    @Override
    public double nextDouble() {
        return rnd.nextDouble();
    }
    
    @Override
    public double nextNonZeroDouble() {
        double d;
        do {
            d = rnd.nextDouble();
        } while(d == 0d);
        return d;
    }
    
    /**
     * Implementation of the {@link org.jrlib.util.random.Random.Factory Random.Factory}
     * interface, which creates {@link org.jrlib.util.random.JavaRandom JavaRandom}
     * instances.
     */
    public static class JavaRandomFactory implements Random.Factory {
        
        @Override
        public Random createRandom() {
            return new JavaRandom();
        }

        @Override
        public Random createRandom(long seed) {
            return new JavaRandom(seed);
        }
    }
}
