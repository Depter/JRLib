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

package org.jreserve.gui.misc.audit.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jreserve.gui.misc.audit.db.AuditDbManager;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.netbeans.api.project.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AuditableObject implements Auditable, AuditedObject {
    
    @Override
    public List<AuditRecord> getAuditEvents() {
        List<AuditRecord> records = new ArrayList<AuditRecord>(getProjectRecords());
        filterRecords(records.iterator());
        return records;
    }

    private List<AuditRecord> getProjectRecords() {
        AuditDbManager dbManager = AuditDbManager.getInstance();
        Project project = getAuditedProject();
        return dbManager.getAuditRecords(project);
    }
    
    private void filterRecords(Iterator<AuditRecord> it) {
        long id = getAuditId();
        while(it.hasNext())
            if(id != it.next().getComponentId())
                it.remove();
    }
}
