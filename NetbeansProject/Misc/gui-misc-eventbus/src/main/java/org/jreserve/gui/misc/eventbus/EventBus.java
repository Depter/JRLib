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

package org.jreserve.gui.misc.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EventBus {
    
    final static Logger logger = Logger.getLogger(EventBus.class.getName());
    
    final static char PATH_SEPARATOR = '.';
    
    private EventBus parent;
    private final Set<EventBus> children = new HashSet<EventBus>();
    
    private final String name;
    private final String path;
    private final List<Subscription> subscriptions = new ArrayList<Subscription>();

    EventBus() {
        this.parent = null;
        this.name = "";
        this.path = "";
    }
    
    private EventBus(EventBus parent, String name) {
        this.parent = parent;
        this.name = name;
        
        if(parent == null) {
            path = name;
        } else {
            String parentPath = parent.getPath();
            path = parentPath.length()>0? parentPath+PATH_SEPARATOR+name : name;
        }
    }
    
    String getName() {
        return name;
    }
    
    String getPath() {
        return path;
    }
    
    EventBus getChild(String path) {
        if(path.length() == 0)
            return this;
        
        int index = path.indexOf('.');
        
        String childName;
        if(index < 0) {
            childName = path;
        } else {
           childName = path.substring(0, index);
        }
        
        EventBus child = getChildByName(childName);
        path = path.substring(childName.length());
        if(path.length()>0 && path.charAt(0) == PATH_SEPARATOR)
            path = path.substring(1);

        return child.getChild(path);
    }
    
    private EventBus getChildByName(String name) {
        for(EventBus bus : children)
            if(bus.name.equals(name))
                return bus;
        
        EventBus bus = new EventBus(this, name);
        children.add(bus);
        return bus;
    }
    
    void subscribe(Object listener) {
        cleanSubscriptions();
        for(Method method : getAnnotatedMethods(listener)) {
            Subscription s = new Subscription(listener, method);
            String msg = String.format(
                "Subscription '%s' for EventBus '%s', for event class '%s'.",
                listener, name, s.getEventClass().getName());
            logger.log(Level.FINER, msg);
            subscriptions.add(s);
        }
    }
    
    private void cleanSubscriptions() {
        int size = subscriptions.size();
        for(int i=0; i<size; i++) {
            Subscription s = subscriptions.get(i);
            if(s.isEmpty()) {
                subscriptions.remove(s);
                i--;
                size--;
            }
        }
    }
    
//    private List<Method> getAnnotatedMethods(Object listener) {
//        List<Method> result = new ArrayList<Method>();
//        for(Method method : listener.getClass().getMethods())
//            if(isListenerMethod(method))
//                result.add(method);
//        return result;
//    }
    
    private List<Method> getAnnotatedMethods(Object listener) {
        List<Method> result = new ArrayList<Method>();
        Class clazz = listener.getClass();
        
        while(clazz != null) {
            for(Method method : clazz.getDeclaredMethods())
                if(isListenerMethod(method))
                    result.add(method);
            clazz = clazz.getSuperclass();
        }
        
        return result;
    }
    
    private boolean isListenerMethod(Method m) {
        return m.getAnnotation(EventBusListener.class) != null &&
               m.getParameterTypes().length == 1 &&
               !m.getParameterTypes()[0].isPrimitive();
    }
    
    void unsubscribe(Object listener) {
        cleanSubscriptions();
        int index = indexOf(listener);
        if(index >= 0)
            subscriptions.remove(index);
        
        if(subscriptions.isEmpty() && isChildrenEmpty())
            removeFromParent();
    }
    
    private boolean isChildrenEmpty() {
        for(EventBus bus : children) {
            if(!bus.subscriptions.isEmpty() || !bus.isChildrenEmpty())
                return false;
        }
        return true;
    }
    
    private void removeFromParent() {
        if(this.parent != null) {
            this.parent.children.remove(this);
            this.parent = null;
        }
    }
    
    private int indexOf(Object listener) {
        int size = subscriptions.size();
        for(int i=0; i<size; i++)
            if(subscriptions.get(i).references(listener))
                return i;
        return -1;
    }
    
    void fillSubscriptions(List<Subscription> subscriptions, Class eventClass) {
        for(Subscription s : this.subscriptions)
            s.addSelf(subscriptions, eventClass);
        if(parent != null)
            parent.fillSubscriptions(subscriptions, eventClass);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof EventBus) &&
               path.equals(((EventBus)o).path);
    }
    
    @Override
    public int hashCode() {
        return path.hashCode();
    }
    
    @Override
    public String toString() {
        return path;
    }
}
