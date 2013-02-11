package org.jreserve;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChangeCounter implements ChangeListener {
    
    private int changeCount;
    
    public void stateChanged(ChangeEvent e) {
        changeCount++;
    }
    
    public int getChangeCount() {
        return changeCount;
    }
}
