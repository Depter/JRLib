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
package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 * The class calculates the standard chain-ladder reserve estimates. The
 * input claim triangle is completed as desribed ar
 * {@link EstimateUtil#completeTriangle(ClaimTriangle, LinkRatio) EstimateUtil.completeTriangle()}.
 * 
 * @see EstimateUtil#completeTriangle(ClaimTriangle, LinkRatio) 
 * @author Peter Decsi
 * @version 1.0
 */
public class ChainLadderEstimate extends AbstractTriangleEstimate<LinkRatio> {

    public ChainLadderEstimate(LinkRatio lrs) {
        super(lrs, lrs.getSourceTriangle());
        super.recalculateLayer();
    }

    public LinkRatio getSourceLinkRatios() {
        return source;
    }
    
    @Override
    protected void initDimensions() {
        if(source == null) {
            accidents = 0;
            developments = 0;
        } else {
            accidents = triangle.getAccidentCount();
            developments = source.getLength()+1;
        }
    }
    
    protected double getEstimatedValue(int accident, int development) {
        int d = development-1;
        return d<0?
                Double.NaN :
                values[accident][d] * source.getValue(d);
    }
}