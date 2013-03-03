package org.jreserve;

import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Changeable {

    public void addChangeListener(ChangeListener listener);

    public void removeChangeListener(ChangeListener listener);
}
