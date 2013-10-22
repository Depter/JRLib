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
package org.jreserve.gui.calculations.vector;

import java.util.List;
import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.trianglewidget.model.TriangleLayer;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.vector.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface VectorModifier extends CalculationModifier<Vector> {
    
    public TriangleLayer createLayer(Vector input);
    
    public List<Cell> getAffectedCells();
}
