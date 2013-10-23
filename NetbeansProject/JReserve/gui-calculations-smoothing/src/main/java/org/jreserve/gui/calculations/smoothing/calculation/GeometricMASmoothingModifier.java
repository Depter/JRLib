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
package org.jreserve.gui.calculations.smoothing.calculation;

import java.util.List;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.triangle.smoothing.GeometricMovingAverage;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.jreserve.jrlib.vector.smoothing.AbstractSmoothing;
import org.jreserve.jrlib.vector.smoothing.GeometricMovingAverageMethod;
import org.jreserve.jrlib.vector.smoothing.VectorSmoothing;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.GeometricMASmoothingModifier.Name=Geometric MA",
    "# {0} - length",
    "# {1} - cells",
    "LBL.GeometricMASmoothingModifier.Description=Geometric MA [length={0}], [{1}]",
    "LBL.GeometricMASmoothingModifier.ProgressName=Geometric MA Smoothing"
})
public abstract class GeometricMASmoothingModifier<C extends CalculationData>
    extends AbstractMASmoothingModifier<C> {
    
    public final static String ROOT_TAG = "geometricMovingAverage";
    
    public GeometricMASmoothingModifier(List<SmoothingCell> cells, Class<C> clazz, int length) {
        super(cells, clazz, length);
    }

    @Override
    protected String getRootName() {
        return ROOT_TAG;
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_GeometricMASmoothingModifier_Description(getLength(), getCellsAsString());
    }

    @Override
    protected String getDisplayName() {
        return Bundle.LBL_GeometricMASmoothingModifier_Name();
    }

    protected final GeometricMovingAverage createSmoothing() {
        return new GeometricMovingAverage(getCellsAsArray(), getLength());
    }
    
    protected final VectorSmoothing createVectorSmoothing() {
        return new AbstractSmoothing(
                getCellsAsIndices(),
                new GeometricMovingAverageMethod(getLength()));
    }
    
    @Override
    protected String getUpdatePHTitle() {
        return Bundle.LBL_GeometricMASmoothingModifier_ProgressName();
    }
}
