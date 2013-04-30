package org.jreserve.grscript.gui.eventbus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EventBusImpl<T> implements EventBus<T> {
    
    
    private final InstanceContent content;
    private final Lookup lkp;
    private final List<Registration> registrations = new ArrayList<Registration>();
    
    EventBusImpl() {
        this(new InstanceContent());
    }
    
    EventBusImpl(InstanceContent content) {
        this.content = content;
        this.lkp = new AbstractLookup(content);
    }

    @Override
    public synchronized void publishEvent(Object event) {
        publishEvents(Collections.singleton(event));
    }

    @Override
    public synchronized void publishEvents(Collection events) {
        Set values = new HashSet(events.size());
        for(Object event : events) {
            if(event == null)
                throw new NullPointerException("Can not publish null events!");
            values.add(event);
        }
        publisContentChange(values);
    }
    
    private void publisContentChange(Collection<T> events) {
        for(T event : events)
            content.add(event);

        for(Registration r : new ArrayList<Registration>(registrations))
            r.publish();
        content.set(Collections.EMPTY_LIST, null);
    }

    @Override
    public synchronized void subscribe(EventBusListener<T> listener) {
        new Registration(Object.class, listener).subscribe();
    }
    
    @Override
    public synchronized <L extends T> void subscribe(Class<L> clazz, EventBusListener<L> listener) {
        new Registration(clazz, listener).subscribe();
    }

    @Override
    public synchronized void unsubscribe(EventBusListener listener) {
        List<Registration> toRemove = new ArrayList<Registration>();
        for(Registration r : new ArrayList<Registration>(registrations))
            if(r.listener.equals(listener))
                toRemove.add(r);
        registrations.removeAll(toRemove);
    }
    
    private class Registration<L> {
        
        private Class clazz;
        private EventBusListener<L> listener;
        
        Registration(Class clazz, EventBusListener<L> listener) {
            if(clazz == null)
                throw new NullPointerException("Class can not be null!");
            this.clazz = clazz;
            
            if(listener == null)
                throw new NullPointerException("EventBusListener can not be null!");
            this.listener = listener;
        }
        
        void subscribe() {
            if(alreadySubscribed())
                return;
            removeReplaced();
            registrations.add(this);
        }
        
        private boolean alreadySubscribed() {
            for(Registration r : registrations)
                if(r.listener.equals(listener) && r.clazz.isAssignableFrom(clazz))
                    return true;
            return false;
        }
        
        private void removeReplaced() {
            List<Registration> replaced = new ArrayList<Registration>();
            for(Registration r : registrations)
                if(isReplaces(r))
                    replaced.add(r);
            registrations.removeAll(replaced);
        }
        
        private boolean isReplaces(Registration r) {
            return listener.equals(r.listener) &&
                   this.clazz.isAssignableFrom(r.clazz);
        }
        
        void unsubscribe() {
            registrations.remove(this);
        }
        
        void publish() {
            Collection<? extends L> result = lkp.lookupAll(clazz);
            if(!result.isEmpty())
                listener.published(new ArrayList<L>(result));
        }
        
        @Override
        public boolean equals(Object o) {
            return (o instanceof Registration) &&
                   equals((Registration) o);
        }
        
        boolean equals(Registration r) {
            return clazz.equals(r.clazz) &&
                   listener.equals(r.listener);
        }
        
        @Override
        public int hashCode() {
            return 17 * clazz.hashCode() + listener.hashCode();
        }
    }
}
