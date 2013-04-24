package org.jreserve.grscript.gui.eventbus;

import java.util.Collection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface EventBusListener<T> {
    
    public void published(Collection<T> instances);
}
