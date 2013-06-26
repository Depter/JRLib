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
package org.jreserve.gui.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import org.jreserve.gui.project.api.ProjectConfigurator;
import org.jreserve.gui.project.config.ConfigFactory;
import org.jreserve.gui.project.config.ProjectConfiguration;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class JReserveProjectInformation implements ProjectInformation, ProjectConfigurator.Manager {

    private JReserveProject project;
    private PropertyChangeSupport pcs;
    private ProjectConfiguration configuration;
    
    public JReserveProjectInformation(JReserveProject project) {
        this.project = project;
        try {
            configuration = ConfigFactory.readConfig(project);
        } catch (Exception ex) {
            configuration = new ProjectConfiguration();
            configuration.setName(project.getProjectDirectory().getName());
            Exceptions.printStackTrace(ex);
        }
    }
    
    @Override
    public String getName() {
        return project.getProjectDirectory().getName();
    }

    @Override
    public String getDisplayName() {
        return configuration.getName();
    }

    @Override
    public Icon getIcon() {
        return JReserveProjectFactory.getProjectIcon();
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public boolean hasConfigurator(String id) {
        return configuration.hasConfigurator(id);
    }

    @Override
    public ProjectConfigurator getConfigurator(String id) {
        return configuration.getConfigurator(id);
    }
}
