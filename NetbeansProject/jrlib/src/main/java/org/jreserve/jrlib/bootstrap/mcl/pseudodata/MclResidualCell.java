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
package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclResidualCell {

    private final double paidLr;
    private final double paidCr;
    private final double incurredLr;
    private final double incurredCr;
    
    MclResidualCell(int accident, int development, MclResidualBundle bundle) {
        paidLr = bundle.getPaidLRResidual(accident, development);
        paidCr = bundle.getPaidCRResidual(accident, development);
        incurredLr = bundle.getIncurredLRResidual(accident, development);
        incurredCr = bundle.getIncurredCRResidual(accident, development);
    }
    
    double getPaidLRResidual() {
        return paidLr;
    }
    
    double getPaidCRResidual() {
        return paidCr;
    }
    
    double getIncurredLRResidual() {
        return incurredLr;
    }
    
    double getIncurredCRResidual() {
        return incurredCr;
    }
}
