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

/**
 *
 * @author AA461472
 */
public final class AuditRecord {
    
    private final Date changeDate;
    private final Date logDate;
    private final long componentId;
    private final String component;
    private final String user;
    private final String change;

    public AuditRecord(Date changeDate, Date logDate, long componentId, String component, String user, String change) {
        this.changeDate = changeDate;
        this.logDate = logDate;
        this.componentId = componentId;
        this.component = component;
        this.user = user;
        this.change = change;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public Date getLogDate() {
        return logDate;
    }
    
    public long getComponentId() {
        return componentId;
    }
    
    public String getComponent() {
        return component;
    }

    public String getUser() {
        return user;
    }

    public String getChange() {
        return change;
    }
}
