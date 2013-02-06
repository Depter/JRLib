package org.jreserve;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractChangeable implements Changeable {

    protected List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
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
    
    protected void fireChange() {
        if(listeners != null) {
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
}
