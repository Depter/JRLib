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
package org.jreserve.gui.calculations.factor.impl.factors;

import org.jreserve.gui.calculations.api.modification.triangle.TriangleCorrectionModifier;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FactorTriangleCorrectionModifier extends TriangleCorrectionModifier<FactorTriangle> {

    public FactorTriangleCorrectionModifier(int accident, int development, double value) {
        super(FactorTriangle.class, accident, development, value);
    }
    
    @Override
    public FactorTriangle createCalculation(FactorTriangle sourceCalculation) {
        return new FactorTriangleCorrection(
                sourceCalculation, 
                getAccident(), getDevelopment(), 
                getValue()
                );
    }
}
