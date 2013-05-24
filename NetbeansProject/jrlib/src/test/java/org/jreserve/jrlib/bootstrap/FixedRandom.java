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
package org.jreserve.jrlib.bootstrap;

import org.jreserve.jrlib.util.random.Random;

/**
 * Dummy implementation for the
 * {@link Random Random} interface, which cycles throug the values [0, 3].
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FixedRandom implements Random {

    private int max = 4;
    private int n = 0;
    
    public int getMax() {
        return max;
    }
    
    @Override
    public long nextLong() {
        if(n >= max)
            n = 0;
        return n++;
    }

    @Override
    public int nextInt(int n) {
        if(this.n >= max)
            this.n = 0;
        int result = this.n >= n ? n - 1 : this.n;
        this.n++;
        return result;
    }

    @Override
    public double nextDouble() {
        return ((double) nextInt(0)) / 4d;
    }

    @Override
    public double nextNonZeroDouble() {
        double v = nextDouble();
        while(v == 0d)
            v = nextDouble();
        return v;
    }
}
