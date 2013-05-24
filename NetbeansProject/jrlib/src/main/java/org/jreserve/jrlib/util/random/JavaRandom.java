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
     * Implementation of the {@link org.jreserve.jrlib.util.random.Random.Factory Random.Factory}
     * interface, which creates {@link org.jreserve.jrlib.util.random.JavaRandom JavaRandom}
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
