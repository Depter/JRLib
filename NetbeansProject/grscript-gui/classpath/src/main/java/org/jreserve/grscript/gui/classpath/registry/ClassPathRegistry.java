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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 */
@XmlRootElement(name = "registry")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassPathRegistry {
    
    @XmlElementRef
    private Set<ClassPathItem> items = new TreeSet<ClassPathItem>();
    
    public boolean addItem(ClassPathItem item) {
        if(item == null)
            return false;
        return items.add(item);
    }
    
    public boolean removeItem(ClassPathItem item) {
        if(item == null)
            return false;
        return items.remove(item);
    }
    
    public List<ClassPathItem> getItems() {
        return new ArrayList<ClassPathItem>(items);
    }
    
    public List<ClassPathItem> getItems(ClassPathItemType type) {
        List<ClassPathItem> result = new ArrayList<ClassPathItem>();
        for(ClassPathItem item : items)
            if(item.getType() == type)
                result.add(item);
        return result;
    }
    
    public List<String> getPathes() {
        List<String> result = new ArrayList<String>(items.size());
        for(ClassPathItem item : items)
            result.add(item.getPath());
        return result;
    }
    
}
