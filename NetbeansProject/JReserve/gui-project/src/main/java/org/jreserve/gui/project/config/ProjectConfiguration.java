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
package org.jreserve.gui.project.config;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.jreserve.gui.project.api.ProjectConfigurator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectConfiguration implements ProjectConfigurator.Manager {
    
    @XmlElementWrapper(name="configurations", required=true)
    @XmlElementRef
    private Set<ModuleConfiguration> configs = new LinkedHashSet<ModuleConfiguration>();
    
    @Override
    public boolean hasConfigurator(String id) {
        return id!=null && getExistingConfigurator(id) != null;
    }
    
    private ModuleConfiguration getExistingConfigurator(String id) {
        for(ModuleConfiguration config : configs)
            if(id.equals(config.getOwnerId()))
                return config;
        return null;
    }
    
    @Override
    public ModuleConfiguration getConfigurator(String id) {
        if(id == null) 
            throw new NullPointerException("Id was null!");
        ModuleConfiguration config = getExistingConfigurator(id);
        return config==null? createConfiguration(id) : config;
    }
    
    private ModuleConfiguration createConfiguration(String id) {
        ModuleConfiguration config = new ModuleConfiguration(id);
        configs.add(config);
        return config;
    }
}
