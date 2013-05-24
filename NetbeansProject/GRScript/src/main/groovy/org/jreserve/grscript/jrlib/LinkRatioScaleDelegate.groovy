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
package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.linkratio.LinkRatio
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale
import org.jreserve.jrlib.linkratio.scale.SimpleLinkRatioScale
import org.jreserve.jrlib.linkratio.scale.DefaultLinkRatioScaleSelection
import org.jreserve.jrlib.scale.MinMaxScaleEstimator
import org.jreserve.jrlib.scale.ScaleExtrapolation
import org.jreserve.jrlib.scale.UserInputScaleEstimator
import org.jreserve.jrlib.scale.ScaleEstimator
import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LinkRatioScaleDelegate extends AbstractDelegate {
    
    private ScaleEstimatorFactory estimators = ScaleEstimatorFactory.getInstance()
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.scale    << this.&scale
    }
    
    LinkRatioScale scale(LinkRatio lrs) {
        return scale(lrs, "minmax")
    }
    
    LinkRatioScale scale(LinkRatio lrs, String type) {
        ScaleEstimator estimator = estimators.createEstimator(type);
        return new SimpleLinkRatioScale(lrs, estimator)
    }
    
    LinkRatioScale scale(LinkRatio lrs, Closure cl) {
        LinkRatioScaleBuilder builder = new LinkRatioScaleBuilder(lrs)
        cl.delegate = builder
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        return builder.scales
    }
}

