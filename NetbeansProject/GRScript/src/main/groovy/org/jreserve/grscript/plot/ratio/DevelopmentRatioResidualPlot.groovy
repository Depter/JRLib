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

package org.jreserve.grscript.plot.ratio

import org.jreserve.grscript.plot.AbstractResidualPlot
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.grscript.plot.PlotFormat
import org.jreserve.jrlib.triangle.TriangleUtil
import org.jreserve.jrlib.triangle.Triangle
import org.jreserve.jrlib.claimratio.ClaimRatio
import org.jreserve.jrlib.vector.Vector as RVector

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DevelopmentRatioResidualPlot extends AbstractResidualPlot {
	
    private RVector lrs
    private Triangle factors
    private int development
    
    DevelopmentRatioResidualPlot(ClaimRatio crs, int development, PlotFormat format) {
        super(format)
        this.development = development
        this.lrs = crs
        this.factors = lrs.getSourceRatioTriangle()
    }
    
    DevelopmentRatioResidualPlot(LinkRatio lrs, int development, PlotFormat format) {
        super(format)
        this.development = development
        this.lrs = lrs
        this.factors = lrs.getSourceFactors()
    }
    
    protected int getDomainLength() {
        TriangleUtil.getAccidentCount(factors, development)
    }
    
    protected double[] getResiduals(int accident) {
        double factor = factors.getValue(accident, development);
        double lr = lrs.getValue(development)
        [factor-lr] as double[]
    }
    
}

