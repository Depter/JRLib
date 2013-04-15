package org.jrlib.util.random;

/**
 * Common interface to random number generators.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Random {
    
    /**
     * Returns the next pseudo random long value.
     */
    public long nextLong();
    
    /**
     * Draws a value from U[0;n] (uniformy distributed, inclusive 0, exclusive
     * n).
     */
    public int nextInt(int n);
    
    /**
     * Draws a value from U[0;1] (uniformy distributed, inclusive 0, exclusive
     * 1).
     */
    public double nextDouble();
    
    /**
     * Draws a value from U[0;1] (uniformy distributed, exclusive 0, exclusive
     * 1).
     */
    public double nextNonZeroDouble();
    
    /**
     * Factory interface to produce {@link Random Random}
     * instances.
     */
    public static interface Factory {
        
        /**
         * Creates a new instance.
         */
        public Random createRandom();
        
        /**
         * Creates a new instance with the given seed.
         */
        public Random createRandom(long seed);
    }

}
