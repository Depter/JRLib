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
package org.jreserve.jrlib.claimratio;

/**
 * This class simply mirrors the fitted {@link ClaimRatio ClaimRatio}. 
 * For development periods, where there is no input link ratio returns 
 * NaN.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultCRMethod implements ClaimRatioMethod {
    
    private ClaimRatio cr;
    
    @Override
    public void fit(ClaimRatio cr) {
        this.cr = cr;
    }

    @Override
    public double getValue(int development) {
        return cr==null? Double.NaN : cr.getValue(development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultCRMethod);
    }
    
    @Override
    public int hashCode() {
        return DefaultCRMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultCRMethod";
    }
}
