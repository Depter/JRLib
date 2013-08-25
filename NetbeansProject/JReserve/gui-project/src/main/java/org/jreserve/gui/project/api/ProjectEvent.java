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
package org.jreserve.gui.project.api;

import org.jreserve.gui.misc.audit.event.AbstractAuditEvent;
import org.jreserve.gui.misc.audit.event.AuditEvent;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.netbeans.api.project.Project;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "MSG.ProjectEvent.ProjectCreated=Project created",
    "# {0} - propertyName",
    "# {1} - oldValue",
    "# {2} - newValue",
    "MSG.ProjectEvent.ProjectPropertyChanged=Project property \"{0}\" changed from \"{1}\" to \"{2}\"."
})
public abstract class ProjectEvent {
    
    public static void publishProjectCreatedEvent(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        publishEvent(new ProjectCreatedEvent(project));
    }
    
    private static void publishEvent(ProjectEvent event) {
        EventBusManager.getDefault().publish(event);
    }
    
    public static void publishPropertyChangedEvent(Project project, String configName, String property, String oldValue, String newValue) {
        publishPropertyChangedEvent(project, configName, property, oldValue, newValue);
    }
    
    public static void publishPropertyChangedEvent(Project project, String configName, String property, String userProperty, String oldValue, String newValue) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        publishEvent(new ProjectPropertyChangedEvent(project, configName, property, userProperty, oldValue, newValue));
    }
    
    
    private final Project project;
    
    private ProjectEvent(Project project) {
        this.project = project;
    }
    
    public Project getProject() {
        return project;
    }
    
    public static class ProjectCreatedEvent extends ProjectEvent implements AuditEvent.Provider {
        
        private final AuditEvent auditEvent;
        
        private ProjectCreatedEvent(Project project) {
            super(project);
            auditEvent = new AbstractAuditEvent(project, AuditEvent.UNKOWN_ID, "", Bundle.MSG_ProjectEvent_ProjectCreated());
        }

        @Override
        public AuditEvent getAuditEvent() {
            return auditEvent;
        }
    }
    
    public static class ProjectPropertyChangedEvent extends ProjectEvent implements AuditEvent.Provider {
        
        private final AuditEvent auditEvent;
        private final String configName;
        private final String property;
        private final String oldValue;
        private final String newValue;
        
        private ProjectPropertyChangedEvent(Project project, String configName, String property, String userProperty, String oldValue, String newValue) {
            super(project);
            this.configName = configName;
            this.property = property;
            this.oldValue = oldValue;
            this.newValue = newValue;
            auditEvent = new AbstractAuditEvent(project, AuditEvent.UNKOWN_ID, "", Bundle.MSG_ProjectEvent_ProjectPropertyChanged(userProperty, oldValue, newValue));
        }

        public String getConfigName() {
            return configName;
        }
        
        public String getProperty() {
            return property;
        }

        public String getOldValue() {
            return oldValue;
        }

        public String getNewValue() {
            return newValue;
        }
        
        @Override
        public AuditEvent getAuditEvent() {
            return auditEvent;
        }
    
    }
}
