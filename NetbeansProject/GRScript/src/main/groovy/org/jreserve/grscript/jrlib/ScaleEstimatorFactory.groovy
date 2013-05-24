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

import org.jreserve.jrlib.scale.ScaleEstimator
import org.jreserve.jrlib.scale.MinMaxScaleEstimator
import org.jreserve.jrlib.scale.ScaleExtrapolation
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ScaleEstimatorFactory {
    
    private static ScaleEstimatorFactory INSTANCE
    
    private static ScaleEstimatorFactory getInstance() {
        if(INSTANCE == null)
            INSTANCE = new ScaleEstimatorFactory()
        return INSTANCE
    }
    
    private ScaleEstimatorFactory() {
    }
    
    ScaleEstimator createEstimator(String type) {
        switch(type.toLowerCase()) {
            case "minmax":
            case "min max":
            case "min-max":
                return new MinMaxScaleEstimator();
            case "extrapolation":
            case "loglin":
            case "log lin":
            case "log-lin":
            case "loglinear":
            case "log linear":
            case "log-linear":
                return new ScaleExtrapolation()
            default:
                String msg = "Unknow scale estimator type: ${type}! Valid "+
                "types are ([MinMax | Min Max | Min-Max] | "+
                "[Extrapolation | LogLinear | Log-Linear | Log Linear | "+
                "LogLin | Log Lin | Log-Lin]).";
                throw new IllegalArgumentException(msg)
        }
    }
    
}

