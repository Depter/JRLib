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

