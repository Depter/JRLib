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
package org.jreserve.grscript.gui.classpath.registry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassPathItem implements Comparable<ClassPathItem> {
    
    @XmlAttribute(name="type", required=true)
    private ClassPathItemType type;
    @XmlValue
    private String path;
    
    public ClassPathItem() {}
    
    public ClassPathItem(ClassPathItemType type, String path) {
        if(type == null)
            throw new NullPointerException("ClassPathItemType is null!");
        this.type = type;
        
        if(path == null)
            throw new NullPointerException("Path is null!");
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
    
    public ClassPathItemType getType() {
        return type;
    }
    
    @Override
    public int compareTo(ClassPathItem o) {
        int dif = type.compareTo(o.type);
        return dif != 0? dif : path.compareToIgnoreCase(o.path);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ClassPathItem) &&
               compareTo((ClassPathItem)o) == 0;
    }
    
    @Override
    public int hashCode() {
        return 17 * type.hashCode() + path.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ClassPathItem [%s; %s]", type, path);
    }
}
