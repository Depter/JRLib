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
package org.jreserve.gui.calculations.vector.modifications;

import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.CalculationModifierFactory;
import org.jreserve.gui.calculations.vector.impl.VectorCalculationImpl;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@CalculationModifierFactory.Registration(
    category = VectorCalculationImpl.CATEGORY,
    rootName = VectorCorrectionModifier.ROOT_ELEMENT
)
public class VectorCorrectionModifierFactory implements CalculationModifierFactory<Vector> {
    @Override
    public CalculationModifier<Vector> fromXml(Element element) throws Exception {
        int accident = JDomUtil.getExistingInt(element, VectorCorrectionModifier.ACCIDENT_ELEMENT);
        double value = JDomUtil.getExistingDouble(element, VectorCorrectionModifier.VALUE_ELEMENT);
        return new VectorCorrectionModifier(accident, value);
    }
}
