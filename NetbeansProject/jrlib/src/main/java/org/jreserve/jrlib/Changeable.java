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
package org.jreserve.jrlib;

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
