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
package org.jreserve.gui.calculations.api;

import org.jdom2.Element;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.jrlib.CalculationData;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCalculationProvider<C extends CalculationData> 
    implements CalculationProvider<C>, AuditedObject {

    private final Project project;
    final CalculationDataObject obj;
    private String path;
    protected final CalculationEventUtil events;
    protected final Object lock;
    
    protected AbstractCalculationProvider(CalculationDataObject obj) {
        this.events = new CalculationEventUtil(this);
        this.obj = obj;
        this.lock = obj.lock;
        this.project = FileOwnerQuery.getOwner(obj.getPrimaryFile());
        path = Displayable.Utils.displayProjectPath(obj.getPrimaryFile());
    }
    
    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public C getCalculation() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPath() {
        synchronized(lock) {
            return path;
        }
    }
    
    protected void setPath(String path) {
        synchronized(lock) {
            String oldPath = path;
            this.path = path;
            events.fireRenamed(oldPath);
        }
    }

    @Override
    public Project getAuditedProject() {
        return project;
    }

    @Override
    public String getAuditName() {
        return getPath();
    }
    
    protected abstract Element toXml();
}