package org.jreserve.grscript.jrlib

import org.jreserve.grscript.FunctionProvider
import org.jreserve.jrlib.triangle.TriangleUtil
import org.jreserve.jrlib.triangle.Triangle
import org.jreserve.grscript.util.PrintDelegate

/**
 *
 * @author Peter Decsi
 */
class TriangleUtilDelegate implements FunctionProvider {
	
    @Override
    void initFunctions(Script script, ExpandoMetaClass emc) {
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
        
        emc.printData   << {Triangle t -> printData(t.toArray())}
        emc.printData   << {String title, Triangle t -> printData(title, t.toArray())}
        emc.printData   << {org.jreserve.jrlib.vector.Vector v -> printData(v.toArray())}
        emc.printData   << {String title, org.jreserve.jrlib.vector.Vector v -> printData(title, v.toArray())}
    }
}

