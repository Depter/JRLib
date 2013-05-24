/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.util.random;

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
