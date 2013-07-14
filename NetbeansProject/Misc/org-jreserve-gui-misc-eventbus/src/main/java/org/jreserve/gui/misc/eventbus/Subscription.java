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
    private final static String ACCESS_ERROR = 
        "Can not access method '%s()' in '%s' for event publishing!";
    private final static String INVOCATION_ERROR = 
        "Method '%s()' in '%s' has thrown an unexpected exception during event publishing!";
    
    private final static Logger logger = Logger.getLogger(Subscription.class.getName());
    
    private final WeakReference reference;
    private final String methodName;
    private final Class[] eventClass;
    private final boolean forceEdt;
    
    Subscription(Object listener, Method method) {
        reference = new WeakReference(listener);
        methodName = method.getName();
        eventClass = new Class[]{method.getParameterTypes()[0]};
        
        EventBusListener annotation = method.getAnnotation(EventBusListener.class);
        forceEdt = annotation.forceEDT();
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
            if(listener == s.reference.get() && methodName.equals(s.methodName))
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
            Method m = getMethod();
            if(m != null) {
                if(m.isAccessible()) {
                    invokeMethod(m);
                } else {
                    invokeInaccessibleMethod(m);
                }
            }
        }
        
        private Method getMethod() {
            
            try {
                Class clazz = listener.getClass();
                return clazz.getMethod(methodName, eventClass);
            } catch (Exception ex) {
                String  msg = String.format(ACCESS_ERROR, methodName, listener);
                logger.log(Level.WARNING, msg, ex);
                return null;
            }
        }
        
        private void invokeMethod(Method m) {
            try {
                m.invoke(listener, event);
            } catch (Exception ex) {
                String  msg = String.format(INVOCATION_ERROR, methodName, listener);
                logger.log(Level.WARNING, msg, ex);
            }
        }
        
        private void invokeInaccessibleMethod(Method m) {
            m.setAccessible(true);
            invokeMethod(m);
            m.setAccessible(false);
        }
    }
}
