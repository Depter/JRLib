package org.jrlib;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Default implementation for the {@link Changeable Changeable} 
 * interface. This class does nothing extra, just reduces 
 * boiler-plate codeing.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractChangeable implements Changeable {

    protected List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private boolean eventsFired = true;
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(listeners!=null && listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        if(listeners != null)
            listeners.remove(listener);
    }
    
    /**
     * Fires a {@link ChangeEvent ChangeEvent}. The source
     * of the event will be this object.
     */
    protected void fireChange() {
        if(eventsFired && listeners != null) {
            ChangeEvent evt = new ChangeEvent(this);
            for(ChangeListener listener : getListeners())
                listener.stateChanged(evt);
        }
    }

    private ChangeListener[] getListeners() {
        int size = listeners.size();
        ChangeListener[] result = new ChangeListener[size];
        return listeners.toArray(result);
    }

    @Override
    public void setEventsFired(boolean firesEvents) {
        this.eventsFired = firesEvents;
    }

    @Override
    public boolean isEventsFired() {
        return eventsFired;
    }
}
