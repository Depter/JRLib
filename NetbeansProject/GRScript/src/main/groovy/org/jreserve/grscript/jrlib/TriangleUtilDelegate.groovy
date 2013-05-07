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
        emc.decummulate  << {double[][] d -> TriangleUtil.deCummulate(d); d}
        emc.decummulate  << {double[] d -> TriangleUtil.deCummulate(d); d}
        emc.copy         << {double[][] d -> TriangleUtil.copy(d)}
        emc.copy         << {double[] d -> TriangleUtil.copy(d)}
        
        emc.add          << {double[] a, double[] b -> TriangleUtil.add(a, b)}
        emc.subtract     << {double[] a, double[] b -> TriangleUtil.subtract(a, b)}
        emc.divide       << {double[] a, double[] b -> TriangleUtil.divide(a, b)}
        emc.multiply     << {double[] a, double[] b -> TriangleUtil.multiply(a, b)}
        
        emc.add          << {double[][] a, double[][] b -> TriangleUtil.add(a, b)}
        emc.subtract     << {double[][] a, double[][] b -> TriangleUtil.subtract(a, b)}
        emc.divide       << {double[][] a, double[][] b -> TriangleUtil.divide(a, b)}
        emc.multiply     << {double[][] a, double[][] b -> TriangleUtil.multiply(a, b)}
        
        emc.printData   << {Triangle t -> printData(t.toArray())}
        emc.printData   << {String title, Triangle t -> printData(title, t.toArray())}
        emc.printData   << {org.jreserve.jrlib.vector.Vector v -> printData(v.toArray())}
        emc.printData   << {String title, org.jreserve.jrlib.vector.Vector v -> printData(title, v.toArray())}
        
        def meta = new double[0].getClass().metaClass
        meta.plus     << {double[] b -> TriangleUtil.add(delegate, b)}
        meta.minus    << {double[] b -> TriangleUtil.subtract(delegate, b)}
        meta.multiply << {double[] b -> TriangleUtil.multiply(delegate, b)}
        meta.div      << {double[] b -> TriangleUtil.divide(delegate, b)}
        
        meta = new double[0][0].getClass().metaClass
        meta.plus     << {double[][] b -> TriangleUtil.add(delegate, b)}
        meta.minus    << {double[][] b -> TriangleUtil.subtract(delegate, b)}
        meta.multiply << {double[][] b -> TriangleUtil.multiply(delegate, b)}
        meta.div      << {double[][] b -> TriangleUtil.divide(delegate, b)}
    }
}

