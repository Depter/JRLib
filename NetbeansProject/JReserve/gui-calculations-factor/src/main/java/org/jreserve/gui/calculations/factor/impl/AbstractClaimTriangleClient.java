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
package org.jreserve.gui.calculations.factor.impl;

import org.jreserve.gui.calculations.api.AbstractCalculationSourceClient;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractClaimTriangleClient<T> extends AbstractCalculationSourceClient<T, ClaimTriangleCalculation> {

    @Override
    protected ClaimTriangleCalculation getSource(DataObject obj) {
        return obj.getLookup().lookup(ClaimTriangleCalculation.class);
    }
    
    @Override
    protected boolean hasSource(DataObject obj, ClaimTriangleCalculation ct) {
        FactorBundleImpl bundle = obj.getLookup().lookup(FactorBundleImpl.class);
        if(bundle == null)
            return false;
        return ct == bundle.getFactors().getSource();
    }
}
