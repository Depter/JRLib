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

import org.jdom2.Element;
import org.jreserve.gui.calculations.api.modification.CalculationModifier;
import org.jreserve.gui.calculations.api.modification.CalculationModifierFactory;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@CalculationModifierFactory.Registration(
    category = FactorTriangleCalculationImpl.CATEGORY,
    rootName = FactorTriangleCorrectionModifier.ROOT_ELEMENT
)
public class FactorTriangleCorrectionModifierFactory implements CalculationModifierFactory<FactorTriangle> {
    @Override
    public CalculationModifier<FactorTriangle> fromXml(Element element) throws Exception {
        int accident = JDomUtil.getExistingInt(element, FactorTriangleCorrectionModifier.ACCIDENT_ELEMENT);
        int development = JDomUtil.getExistingInt(element, FactorTriangleCorrectionModifier.DEVELOPMENT_ELEMENT);
        double value = JDomUtil.getExistingDouble(element, FactorTriangleCorrectionModifier.VALUE_ELEMENT);
        return new FactorTriangleCorrectionModifier(accident, development, value);
    }
}
