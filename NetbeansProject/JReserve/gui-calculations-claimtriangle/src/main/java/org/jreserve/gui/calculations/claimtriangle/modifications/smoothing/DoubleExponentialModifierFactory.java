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
package org.jreserve.gui.calculations.claimtriangle.modifications.smoothing;

import java.util.List;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.calculations.api.CalculationModifierFactory;
import org.jreserve.gui.calculations.claimtriangle.impl.ClaimTriangleCalculationImpl;
import org.jreserve.gui.calculations.smoothing.calculation.SmoothingModifierUtil;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@CalculationModifierFactory.Registration(
    category = ClaimTriangleCalculationImpl.CATEGORY,
    rootName = ExponentialModifier.ROOT_TAG,
    position = 200
)
public class DoubleExponentialModifierFactory implements CalculationModifierFactory<ClaimTriangle> {

    @Override
    public CalculationModifier<ClaimTriangle> fromXml(Element root) throws Exception {
        double alpha = JDomUtil.getExistingDouble(root, DoubleExponentialModifier.ALPHA_TAG);
        double beta = JDomUtil.getExistingDouble(root, DoubleExponentialModifier.BETA_TAG);
        List<SmoothingCell> cells = SmoothingModifierUtil.readCells(root);
        return new DoubleExponentialModifier(cells, alpha, beta);
    }
    
}
