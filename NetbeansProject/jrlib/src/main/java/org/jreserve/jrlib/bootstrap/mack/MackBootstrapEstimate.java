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
package org.jreserve.jrlib.bootstrap.mack;

import org.jreserve.jrlib.estimate.ChainLadderEstimate;
import org.jreserve.jrlib.linkratio.LinkRatio;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackBootstrapEstimate extends ChainLadderEstimate {

    private final MackProcessSimulator processSimulator;
    
    public MackBootstrapEstimate(LinkRatio lrs, MackProcessSimulator processSimulator) {
        super(lrs);
        this.detach();
        this.processSimulator = processSimulator;
        processSimulator.setEstimate(this);
        simulateProcess();
    }

    @Override
    protected void recalculateLayer() {
        super.recalculateLayer();
        simulateProcess();
    }
    
    private void simulateProcess() {
        for(int a=0; a<accidents; a++)
            for(int d=getObservedDevelopmentCount(a); d<developments; d++)
                values[a][d] = processSimulator.simulateEstimate(values[a][d], a, d);
    }
}
