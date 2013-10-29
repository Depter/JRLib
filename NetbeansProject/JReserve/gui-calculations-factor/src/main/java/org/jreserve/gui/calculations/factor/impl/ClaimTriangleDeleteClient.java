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
package org.jreserve.gui.calculations.factor.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.factor.impl.factors.FactorTriangleCalculationImpl;
import org.jreserve.gui.misc.utils.actions.deletable.Deletable;
import org.netbeans.api.actions.Savable;
import org.openide.loaders.DataObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service = Deletable.Client.class)
public class ClaimTriangleDeleteClient
    extends AbstractClaimTriangleClient<Deletable.Task>
    implements Deletable.Client {

    @Override
    public List<Deletable.Task> getTasks(Object deleted) {
        if(deleted instanceof DataObject)
            return super.createTasks((DataObject) deleted);
        return Collections.EMPTY_LIST;
    }

    @Override
    protected Deletable.Task createTask(DataObject calcObj, ClaimTriangleCalculation ds) {
        return new DeletedTask(calcObj);
    }
    
    private class DeletedTask implements Deletable.Task {
        
        private final DataObject calcObj;
        
        private DeletedTask(DataObject calcObj) {
            this.calcObj = calcObj;
        }

        @Override
        public void objectDeleted() throws Exception {
            updateFile();
            saveObject();
        }
        
        private void updateFile() {
            FactorBundleImpl bundle = calcObj.getLookup().lookup(FactorBundleImpl.class);
            FactorTriangleCalculationImpl factors = bundle.getFactors();
            factors.setSource(ClaimTriangleCalculation.EMPTY);
        }
        
        private void saveObject() throws IOException {
            Savable s = calcObj.getLookup().lookup(Savable.class);
            if(s != null)
                s.save();
        }
    }    
    
}
