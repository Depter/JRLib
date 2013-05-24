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

import org.jreserve.jrlib.triangle.factor.FactorTriangle
import org.jreserve.jrlib.triangle.claim.ClaimTriangle
import org.jreserve.jrlib.linkratio.*
import org.jreserve.jrlib.triangle.factor.DevelopmentFactors

/**
 *
 * @author Peter Decsi
 */
class LinkRatioBuilder extends AbstractMethodSelectionBuilder<LinkRatioMethod> {
    
    private DefaultLinkRatioSelection lrs
    
    LinkRatioBuilder(FactorTriangle factors) {
        LinkRatioMethod method = getCachedMethod(WeightedAverageLRMethod.class) {new WeightedAverageLRMethod()}
        lrs = new DefaultLinkRatioSelection(factors, method);
    }
    
    LinkRatio getLinkRatios() {
        return lrs
    }
    
    void average(int index) {
        LinkRatioMethod method = getCachedMethod(AverageLRMethod.class) {new AverageLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void average(Collection<Integer> indices) {
        for(i in indices)
            average(i)
    }
    
    void mack(int index) {
        LinkRatioMethod method = getCachedMethod(MackRegressionLRMethod.class) {new MackRegressionLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void mack(Collection<Integer> indices) {
        for(i in indices)
            mack(i)
    }
    
    void max(int index) {
        LinkRatioMethod method = getCachedMethod(MaxLRMethod.class) {new MaxLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void max(Collection<Integer> indices) {
        for(i in indices)
            max(i)
    }

    void min(int index) {
        LinkRatioMethod method = getCachedMethod(MinLRMethod.class) {new MinLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void min(Collection<Integer> indices) {
        for(i in indices)
            min(i)
    }

    void weightedAverage(int index) {
        LinkRatioMethod method = getCachedMethod(WeightedAverageLRMethod.class) {new WeightedAverageLRMethod()}
        lrs.setMethod(method, index)
    }
    
    void weightedAverage(Collection<Integer> indices) {
        for(i in indices)
            weightedAverage(i)
    }
    
    void fixed(int index, double value) {
        LinkRatioMethod method = getCachedMethod(UserInputLRMethod.class) {new UserInputLRMethod()}
        (method as UserInputLRMethod).setValue(index, value)
        lrs.setMethod(method, index)
    }
    
    void fixed(Map map) {
        map.each {index, value -> fixed(index, value)}
    }
}

