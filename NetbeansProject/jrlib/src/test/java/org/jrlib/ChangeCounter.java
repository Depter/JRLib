package org.jrlib;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Utility class that simply counts the calls for the
 * {@link #stateChanged(javax.swing.event.ChangeEvent) stateChanged} 
 * method.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class ChangeCounter implements ChangeListener {
    
    private int changeCount;
    
    @Override
    public void stateChanged(ChangeEvent e) {
        changeCount++;
    }
    
    /**
     * Returns how many times the
     * {@link #stateChanged(javax.swing.event.ChangeEvent) stateChanged}
     * has been called.
     */
    public int getChangeCount() {
        return changeCount;
    }
}
