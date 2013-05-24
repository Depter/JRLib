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
package org.jreserve.jrlib.linkratio.curve;

import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 * This class simply mirrors the fitted {@link LinkRatio LinkRatio}. 
 * For development periods, where there is no input link ratio returns 
 * NaN.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLRCurve implements LinkRatioCurve {
    
    private double[] lrs;
    private int developments;
    
    @Override
    public void fit(LinkRatio lr) {
        this.lrs = lr.toArray();
        this.developments = lrs.length;
    }

    @Override
    public double getValue(int development) {
        if(development >= 0 && development < developments)
            return lrs[development];
        return Double.NaN;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultLRCurve);
    }
    
    @Override
    public int hashCode() {
        return DefaultLRCurve.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultLRCurve";
    }
}
