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

