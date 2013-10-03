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

package org.jreserve.gui.calculations.claimtriangle.impl;

import org.jreserve.gui.calculations.api.AbstractCalculationSourceClient;
import org.jreserve.gui.data.api.DataSource;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractDataSourceClient<T> extends AbstractCalculationSourceClient<T, DataSource> {

    @Override
    protected DataSource getSource(DataObject obj) {
        return obj.getLookup().lookup(DataSource.class);
    }
    
    @Override
    protected boolean hasSource(DataObject obj, DataSource ds) {
        ClaimTriangleCalculationImpl calc = obj.getLookup().lookup(ClaimTriangleCalculationImpl.class);
        if(calc == null)
            return false;
        return ds == calc.getDataSource();
    }
}
