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
package org.jreserve.jrlib.bootstrap.mcl;

import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class SimpleAbstractMclProcessSimulator extends AbstractMclProcessSimulator {
    
    private double[][] values;
    
    protected SimpleAbstractMclProcessSimulator(LinkRatioScale scales, boolean isPaid) {
        super(scales, isPaid);
    }

    @Override
    public void setBundle(MclBootstrapEstimateBundle bundle) {
        super.setBundle(bundle);
    }
    
    @Override
    protected double getVariance(int accident, int development) {
        if(values == null)
            values = isPaid? bundle.getPaidValues() : bundle.getIncurredValues();
            
        int d = development - 1;
        double cik = getClaim(accident, d);
        double scale = getScale(accident, d);
        return cik * scale * scale;
    }
    
    private double getClaim(int accident, int development) {
        double cik = values[accident][development];
        return cik < 0d? -cik : cik;
    }    
}
