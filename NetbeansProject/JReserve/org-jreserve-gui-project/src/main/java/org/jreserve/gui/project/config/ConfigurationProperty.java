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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
class ConfigurationProperty {
    
    @XmlAttribute(name="name", required=true)
    private String name;
    @XmlAttribute(name="value", required=true)
    private String value;
    
    ConfigurationProperty() {
    }

    public ConfigurationProperty(String name, String value) {
        if(isNull(name))
            throw new IllegalArgumentException("Name can not be null or empty or whitespace! it was: "+name);
        this.name = name;
        this.value = value;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }
    
    void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof ConfigurationProperty)
            return equals((ConfigurationProperty)o);
        return false;
    }
    
    private boolean equals(ConfigurationProperty o) {
        if(isNull(name))
            return isNull(o.name)? true : false;
        return isNull(o.name)? false : name.equals(o.name);
    }
    
    static boolean isNull(String s) {
        return s==null || s.trim().length()==0;
    }
    
    @Override
    public int hashCode() {
        return isNull(name)? 0 : name.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format(
                "<property name=\"%s\" value=\"%s\"/>", 
                isNull(name)? "" : name, 
                value==null? "" : value);
    }
    
    void afterUnmarshal(Unmarshaller um, Object parent) {
        if(isNull(name))
            throw new IllegalStateException("ConfigurationProperty unmarshalled with empty name: '"+name+"'");
    }
}
