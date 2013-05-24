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
package org.jreserve.grscript.gui.classpath;

import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClassPathEvent {
    
    public static void publishAdd(ClassPathItem item) {
        ClassPathEvent evt = new ClassPathEvent(item, Type.ADDED);
        EventBusRegistry.getDefault().publishEvent(evt);
    }
    
    public static void publishDelete(ClassPathItem item) {
        ClassPathEvent evt = new ClassPathEvent(item, Type.DELETED);
        EventBusRegistry.getDefault().publishEvent(evt);
    }
    
    private final ClassPathItem item;
    private final Type type;
    
    public ClassPathEvent(ClassPathItem item, Type type) {
        if(item == null)
            throw new NullPointerException("ClassPathItem is null!");
        this.item = item;
        
        if(type == null)
            throw new NullPointerException("Type is null!");
        this.type = type;
    }
    
    public ClassPathItem getItem() {
        return item;
    }
    
    public Type getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ClassPathEvent) &&
               equals((ClassPathEvent)o);
    }
    
    public boolean equals(ClassPathEvent evt) {
        return type.equals(evt.type) &&
               item.equals(evt.item);
    }
    
    @Override
    public int hashCode() {
        return 17 * type.hashCode() + item.hashCode();
    }
    
    public static enum Type {
        ADDED,
        DELETED;
    }
}
