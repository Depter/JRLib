package org.jreserve.bootstrap;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Random {

    public long nextLong();
    
    public int nextInt(int n);
    
    public static interface Factory {
        
        public Random createRandom();
        
        public Random createRandom(long seed);
    }
}
