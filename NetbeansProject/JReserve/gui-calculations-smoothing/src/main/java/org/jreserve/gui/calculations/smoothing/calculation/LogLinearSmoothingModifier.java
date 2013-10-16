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
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.LogLinearRegressionSmoothing;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.LogLinearSmoothingModifier.Name=Log-Linear Regression",
    "# {0} - hasIntercept",
    "# {1} - cells",
    "LBL.LogLinearSmoothingModifier.Description=Log-Linear Regression [hasIntercept={0}], [{1}]"
})
public abstract class LogLinearSmoothingModifier<T extends Triangle> extends AbstractRegressionSmoothingModifier<T> {

    public final static String ROOT_TAG = "logLinearRegressionSmoothing";

    public LogLinearSmoothingModifier(List<SmoothingCell> cells, Class<T> clazz, boolean hasIntercept) {
        super(cells, clazz, hasIntercept);
    }
    
    @Override
    protected String getRootTag() {
        return ROOT_TAG;
    }
    
    @Override
    protected String getDisplayName() {
        return Bundle.LBL_LogLinearSmoothingModifier_Name();
    }

    @Override
    public String getDescription() {
        return Bundle.LBL_LinearSmoothingModifier_Description(hasIntercept(), getCellsAsString());
    }

    protected final LogLinearRegressionSmoothing createSmoothing() {
        return new LogLinearRegressionSmoothing(getCellsAsArray(), hasIntercept());
    }
}
