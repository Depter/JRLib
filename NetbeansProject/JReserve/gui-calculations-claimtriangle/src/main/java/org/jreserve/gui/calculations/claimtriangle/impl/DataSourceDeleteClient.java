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

import java.io.IOException;
import java.util.List;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.utils.actions.DeletableDataObject;
import org.netbeans.api.actions.Savable;
import org.openide.loaders.DataObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service = DeletableDataObject.Client.class)
public class DataSourceDeleteClient
    extends AbstractDataSourceClient<DeletableDataObject.Task>
    implements DeletableDataObject.Client {

    @Override
    public List<DeletableDataObject.Task> getTasks(DataObject deleted) {
        return super.createTasks(deleted);
    }

    @Override
    protected DeletableDataObject.Task createTask(DataObject calcObj, DataSource ds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private class DeletedTask implements DeletableDataObject.Task {
        
        private final DataObject calcObj;
        
        private DeletedTask(DataObject calcObj) {
            this.calcObj = calcObj;
        }
        
        @Override
        public void deleted() throws IOException {
            updateFile();
            saveObject();
        }
        
        private void updateFile() {
            ClaimTriangleCalculationImpl calc = calcObj.getLookup().lookup(ClaimTriangleCalculationImpl.class);
            calc.setDataSource(DataSource.EMPTY_TRIANGLE);
        }
        
        private void saveObject() throws IOException {
            Savable s = calcObj.getLookup().lookup(Savable.class);
            if(s != null)
                s.save();
        }
    }    
}
