package org.jreserve.bootstrap;

/**
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

