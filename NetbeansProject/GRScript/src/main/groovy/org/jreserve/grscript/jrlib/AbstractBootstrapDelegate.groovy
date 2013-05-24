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
package org.jreserve.grscript.jrlib

import org.jreserve.jrlib.util.random.Random
import org.jreserve.jrlib.util.random.JavaRandom
import org.jreserve.grscript.AbstractDelegate
import org.jreserve.grscript.util.MapUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AbstractBootstrapDelegate extends AbstractDelegate {

    private int count = -1;
    private Random rnd;
    private SegmentBuilder segmentBuilder = new SegmentBuilder();
    private def segments = []
    
    void count(int count) {
        if(count < 1)
            throw new IllegalArgumentException("Count must be at least 1! Trying to set to ${count}.");
        this.count = count;
    }
    
    void random(Random rnd) {
        if(rnd == null)
            throw new NullPointerException("Random can not be null!");
        this.rnd = rnd;
    }
    
    void random(String type) {
        switch(type?.toLowerCase()) {
            case "java":
                this.rnd = new JavaRandom()
                break;
            default:
                throw new IllegalArgumentException("Unknown random type: ${type}!")
        }
    }
    
    void random(String type, long seed) {
        switch(type?.toLowerCase()) {
            case "java":
                this.rnd = new JavaRandom(seed)
                break;
            default:
                throw new IllegalArgumentException("Unknown random type: ${type}!")
        }
    }
    
    void random(Closure cl) {
        RndBuilder builder = new RndBuilder()
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        
        if(builder.seedSet) {
            this.random(builder.type, builder.seed)
        } else {
            this.random(builder.type)
        }
    }
    
    void segment(Closure cl) {
        cl.delegate = segmentBuilder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        int[][] segment = segmentBuilder.getCells()
        if(segment.length > 0)
            segments << segment
    }
    
    protected void checkState() {
        if(count < 1)
            throw new IllegalStateException("Bootstrap count not set!")
    }
    
    protected int getCount() {
        return count;
    }
    
    protected Random getRandom() {
        return this.rnd ?: new JavaRandom()
    }
    
    protected List<int[][]> getSegments() {
        return segments;
    }
    
    private class RndBuilder {
        private String type
        private long seed
        private boolean seedSet = false
        
        void type(String type) {
            this.type = type
        }
        
        void seed(long seed) {
            this.seed = seed
            this.seedSet = true
        }
    }
}

