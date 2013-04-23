package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.grscript.PrintDelegate
import org.jreserve.jrlib.triangle.TriangleUtil
import org.jreserve.jrlib.triangle.Triangle

/**
 *
 * @author Peter Decsi
 */
class TriangleUtilDelegate implements FunctionProvider {
	
    @Override
    void initFunctions(ExpandoMetaClass emc) {
        emc.cummulate    << {double[][] d -> TriangleUtil.cummulate(d); d}
        emc.cummulate    << {double[] d -> TriangleUtil.cummulate(d); d}
        emc.decummulate  << {double[][] d -> TriangleUtil.decummulate(d); d}
        emc.decummulate  << {double[] d -> TriangleUtil.decummulate(d); d}
        emc.copy         << {double[][] d -> TriangleUtil.copy(d)}
        emc.copy         << {double[] d -> TriangleUtil.copy(d)}
        
        emc.add          << {double[] d -> TriangleUtil.add(d)}
        emc.subtract     << {double[] d -> TriangleUtil.subtract(d)}
        emc.divide       << {double[] d -> TriangleUtil.divide(d)}
        emc.multiply     << {double[] d -> TriangleUtil.multiply(d)}
        
        emc.add          << {double[][] d -> TriangleUtil.add(d)}
        emc.subtract     << {double[][] d -> TriangleUtil.subtract(d)}
        emc.divide       << {double[][] d -> TriangleUtil.divide(d)}
        emc.multiply     << {double[][] d -> TriangleUtil.multiply(d)}
        
        emc.printData   << {Triangle t -> PrintDelegate.printData(t.toArray())}
        emc.printData   << {String title, Triangle t -> PrintDelegate.printData(title, t.toArray())}
        emc.printData   << {org.jreserve.jrlib.vector.Vector v -> PrintDelegate.printData(v.toArray())}
        emc.printData   << {String title, org.jreserve.jrlib.vector.Vector v -> PrintDelegate.printData(title, v.toArray())}
    }
}

