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
package org.jreserve.jrlib.bootstrap.odp.scale;

/**
 * This class simply mirrors the fitted {@link OdpRSMethod OdpRSMethod}. 
 * For development periods, where there is no input link ratio returns 
 * NaN.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultOdpRSMethod implements OdpRSMethod {

    private OdpResidualScale scales;
    
    @Override
    public void fit(OdpResidualScale source) {
        this.scales = source;
    }

    @Override
    public double getValue(int development) {
        return scales==null? Double.NaN : scales.getValue(development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DefaultOdpRSMethod);
    }
    
    @Override
    public int hashCode() {
        return DefaultOdpRSMethod.class.hashCode();
    }

    @Override
    public String toString() {
        return "DefaultOdpRSMethod";
    }
}
