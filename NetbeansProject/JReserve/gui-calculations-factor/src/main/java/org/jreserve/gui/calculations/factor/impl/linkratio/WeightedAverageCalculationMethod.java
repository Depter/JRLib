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
package org.jreserve.gui.calculations.factor.impl.linkratio;

import org.jdom2.Element;
import org.jreserve.gui.calculations.api.method.CalculationMethod;
import org.jreserve.jrlib.linkratio.LinkRatioMethod;
import org.jreserve.jrlib.linkratio.WeightedAverageLRMethod;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.WeightedAverageCalculationMethod.Name=Weighted Average"
})
public class WeightedAverageCalculationMethod implements CalculationMethod<LinkRatioMethod> {

    final static String ROOT_ELEMENT = "weightedAverage";
    
    @Override
    public LinkRatioMethod createMethod() {
        return new WeightedAverageLRMethod();
    }

    @Override
    public Element toXml() {
        return new Element(ROOT_ELEMENT);
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_WeightedAverageCalculationMethod_Name();
    }

    @Override
    public String getId() {
        return WeightedAverageCalculationMethod.class.getName();
    }
}
