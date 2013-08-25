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
package org.jreserve.gui.misc.audit.event;

import java.util.Date;
import org.netbeans.api.project.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractAuditEvent implements AuditEvent {
    
    private final Date changeDate = new Date();
    private final String user = System.getProperty("user.name", "unknown");
    
    private final Project project;
    private final long componentId;
    private final String component;
    private final String change;
    
    public AbstractAuditEvent(AuditedObject obj, String change) {
        this(obj.getAuditedProject(), obj.getAuditId(), obj.getAuditName(), change);
    }
    
    public AbstractAuditEvent(Project project, long componentId, String component, String change) {
        if(project == null)
            throw new NullPointerException("Project was null!");
        this.componentId = componentId;
        this.project = project;
        this.component = component==null? "" : component;
        this.change = change==null? "" : change;
    }
    
    @Override
    public Project getAuditedProject() {
        return project;
    }

    @Override
    public Date getChangeDate() {
        return changeDate;
    }

    @Override
    public String getUserName() {
        return user;
    }

    @Override
    public long getComponentId() {
        return componentId;
    }

    @Override
    public String getComponent() {
        return component;
    }

    @Override
    public String getChange() {
        return change;
    }
}
