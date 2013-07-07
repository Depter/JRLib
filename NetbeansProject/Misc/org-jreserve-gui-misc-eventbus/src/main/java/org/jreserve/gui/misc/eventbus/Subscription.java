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

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class Subscription {
    private final static String NO_METHOD_FOUND = 
        "Instance of class '%s' (%s) is subscribed to event bus events, but "+
        "it does not have a public method annotated with EventBusListener, "+
        "or the annotated method has more then one parameter, or the one "+
        "parameter is a primitive type!";
    private final static String INVOCATION_ERROR = 
        "Method '%s()' in '%s' has thrown an unexpected exception during event publishing!";
    
    private final static Logger logger = Logger.getLogger(Subscription.class.getName());
    
    private final WeakReference reference;
    private final String methodName;
    private final Class[] eventClass;
    private final boolean forceEdt;
    
    Subscription(Object o) {
        reference = new WeakReference(o);
        
        Method m = getMethod(o);
        methodName = m.getName();
        eventClass = new Class[]{m.getParameterTypes()[0]};
        
        EventBusListener annotation = m.getAnnotation(EventBusListener.class);
        forceEdt = annotation.forceEDT();
    }
    
    private Method getMethod(Object o) {
        for(Method m : o.getClass().getMethods())
            if(isListenerMethod(m))
                return m;
        String msg = String.format(NO_METHOD_FOUND, o.getClass().getName(), o);
        throw new IllegalArgumentException(msg);
    }
    
    private boolean isListenerMethod(Method m) {
        return m.getAnnotation(EventBusListener.class) != null &&
               m.getParameterTypes().length == 1 &&
               !m.getParameterTypes()[0].isPrimitive();
    }
    
    boolean isEmpty() {
        return reference.get() == null;
    }
    
    boolean references(Object listener) {
        return listener != null &&
               listener == reference.get();
    }
    
    void addSelf(List<Subscription> subscriptions, Class eventClass) {
        if(isInterested(eventClass) && !isListenerContained(subscriptions))
            subscriptions.add(this);
    }
    
    private boolean isInterested(Class eventClass) {
        return this.eventClass[0].isAssignableFrom(eventClass);
    }
    
    private boolean isListenerContained(List<Subscription> subscriptions) {
        Object listener = reference.get();
        for(Subscription s : subscriptions) {
            if(listener == s.reference.get())
                return true;
        }
        return false;
    }
    
    void publish(Object event) {
        final Object listener = reference.get();
        if(listener == null)
            return;
        
        EDTRunnable runnable = new EDTRunnable(listener, event);
        if(forceEdt && !SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }
    
    private class EDTRunnable implements Runnable {
        private final Object listener;
        private final Object event;

        EDTRunnable(Object listener, Object event) {
            this.listener = listener;
            this.event = event;
        }
        
        @Override
        public void run() {
            try {
                Method m = listener.getClass().getMethod(methodName, eventClass);
                m.invoke(listener, event);
            } catch (Exception ex) {
                String  msg = String.format(INVOCATION_ERROR, methodName, listener);
                logger.log(Level.WARNING, msg, ex);
            }
        }
    }
}
