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
package org.jreserve.jrlib.bootstrap;

/**
 * Interface to wathc bootstrap progress.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface BootstrapCallBack {
    
    /**
     * Retunrs the step frequency, when the listener wants
     * to be notified (i.e return 100 to get notified after
     * each 100th step).
     */
    public int getCallbackStepSize();
    
    /**
     * Notifies about the total steps (`total`) and
     * the completed number of steps (`completed`).
     */
    public void progress(int total, int completed);
}
