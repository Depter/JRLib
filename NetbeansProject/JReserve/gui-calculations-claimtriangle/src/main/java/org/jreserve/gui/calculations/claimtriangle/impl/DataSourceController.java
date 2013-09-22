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

import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.DataSourceObjectProvider;
import org.jreserve.gui.misc.utils.dataobject.DataObjectChooser;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataSourceController.Title=Select Storage"
})
public class DataSourceController extends DataObjectChooser.DefaultController {

    public static DataSource selectOne(DataSourceObjectProvider dop) {
        DataObject obj = DataObjectChooser.selectOne(new DataSourceController(dop));
        if(obj == null)
            return null;
        return obj.getLookup().lookup(DataSource.class);
    }
    
    private DataSourceController(DataSourceObjectProvider dop) {
        super(Bundle.LBL_DataSourceController_Title(), dop.getRootFolder());
    }

    @Override
    public boolean showDataObject(DataObject obj) {
        return super.showDataObject(obj) ||
                obj.getLookup().lookup(DataSource.class) != null;
    }

    @Override
    public boolean canSelectObject(DataObject obj) {
        return obj.getLookup().lookup(DataSource.class) != null;
    }
}
