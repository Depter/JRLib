package org.jrlib;

import javax.swing.event.ChangeListener;

/**
 * Basic interface, to represent the ability to handle 
 * {@link ChangeListener ChangeListeners}.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface Changeable {

    /**
     * Registers the listener on the instance. If <i>listener</i>
     * is <i>null</i> then nothing should happen.
     */
    public void addChangeListener(ChangeListener listener);

    /**
     * Removes the listener from the instance if it is not null and
     * if it was registered.
     */
    public void removeChangeListener(ChangeListener listener);

    /**
     * Enable/Disable fireing of events.
     */
    public void setEventsFired(boolean eventsFired);
    
    /**
     * Retunrs if fireing events are enabled.
     */
    public boolean isEventsFired();
}
