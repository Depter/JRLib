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
package org.jreserve.jrlib.linkratio;

import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.MethodSelection;

/**
 * This interface represents the calculation of link ratios, from
 * a triangle of development factors.
 * 
 * The class enables the users to use different calculation 
 * methods ({@link LinkRatioMethod LinkRatioMethod}) for different 
 * development periods.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSelection extends LinkRatio, MethodSelection<FactorTriangle, LinkRatioMethod> {
}
