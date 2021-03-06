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

package org.jreserve.grscript.plot.residual

import org.jreserve.grscript.plot.AbstractResidualPlot
import org.jreserve.jrlib.triangle.Triangle
import org.jreserve.grscript.plot.PlotFormat
import org.jreserve.jrlib.triangle.TriangleUtil

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AccidentResidualPlot extends AbstractResidualPlot {

    private Triangle residuals
    
    AccidentResidualPlot(Triangle residuals, PlotFormat format) {
        super(format)
        this.residuals = residuals
    }
    
    protected int getDomainLength() {
        residuals.getAccidentCount()
    }
    
    protected double[] getResiduals(int accident) {
        int size = residuals.getDevelopmentCount(accident)
        double[] result = new double[size]
        for(int d=0; d<size; d++) 
            result[d] = residuals.getValue(accident, d)
        return result
    }
    
}


