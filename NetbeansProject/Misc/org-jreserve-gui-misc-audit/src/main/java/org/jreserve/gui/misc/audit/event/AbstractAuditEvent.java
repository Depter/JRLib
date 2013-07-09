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
 * @author AA461472
 */
public class AbstractAuditEvent implements AuditEvent {
    
    private final Date changeDate = new Date();
    private final String user = System.getProperty("user.name", "unknown");
    
    private final Project project;
    private final String component;
    private final String change;

    public AbstractAuditEvent(Project project, String component, String change) {
        if(project == null)
            throw new NullPointerException("Project was null!");
        this.project = project;
        this.component = component==null? "" : component;
        this.change = change==null? "" : change;
    }
    
    @Override
    public Project getAuditedProject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public String getComponent() {
        return component;
    }

    @Override
    public String getChange() {
        return change;
    }
    
}
