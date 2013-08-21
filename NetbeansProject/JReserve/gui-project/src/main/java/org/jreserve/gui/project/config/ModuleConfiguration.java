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
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import org.jreserve.gui.project.api.ProjectConfigurator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name="configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleConfiguration implements ProjectConfigurator {
    
    @XmlAttribute(name="ownerId", required = true)
    private String ownerId;
    @XmlElementRef(name="property", type=ConfigurationProperty.class)
    private Set<ConfigurationProperty> properties = new LinkedHashSet<ConfigurationProperty>();
    
    public ModuleConfiguration() {
    }
    
    public ModuleConfiguration(String ownerId) {
        if(ConfigurationProperty.isNull(ownerId))
            throw new IllegalArgumentException("OwnerId can not be null or empty or whitespace! it was: "+ownerId);
        this.ownerId = ownerId;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public boolean hasProperty(String property) {
        return property!=null &&
               getConfigurationProperty(property) != null;
    }
    
    private ConfigurationProperty getConfigurationProperty(String property) {
        if(property == null) return null;
        for(ConfigurationProperty prop : properties)
            if(property.equals(prop.getName()))
                return prop;
        return null;
    }
    
    @Override
    public String getProperty(String property) {
        if(property == null) return null;
        ConfigurationProperty p = getConfigurationProperty(property);
        return p==null? null : p.getValue();
    }
    
    @Override
    public void setProperty(String property, String value) {
        if(property == null)
            throw new NullPointerException("Property can not be null!");
        ConfigurationProperty p = getConfigurationProperty(property);
        
        if(p == null) {
            if(value != null)
                properties.add(new ConfigurationProperty(property, value));
        } else {
            if(value == null) {
                properties.remove(p);
            } else {
                p.setValue(value);
            }
        }
    }    
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ModuleConfiguration) &&
               ownerId.equals(((ModuleConfiguration)o).ownerId);
    }
    
    @Override
    public int hashCode() {
        return ownerId.hashCode();
    }
    
    @Override
    public String toString() {
        return "ModuleConfiguration ["+ownerId+"]";
    }
    
    void afterUnmarshal(Unmarshaller um, Object parent) {
        if(ConfigurationProperty.isNull(ownerId))
            throw new IllegalStateException("ModuleConfiguration unmarshalled with empty ownerId: '"+ownerId+"'");
    }
}
