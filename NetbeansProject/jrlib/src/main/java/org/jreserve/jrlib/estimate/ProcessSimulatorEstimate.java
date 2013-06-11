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
package org.jreserve.jrlib.estimate;

import org.jreserve.jrlib.bootstrap.ProcessSimulator;

/**
 * Instances of this interface, are {@link Estimate Estimates},
 * with the ability to apply process variance on the estimated
 * values.
 * 
 * @see org.jreserve.jrlib.bootstrap.ProcessSimulator
 * @author Peter Decsi
 */
public interface ProcessSimulatorEstimate extends Estimate {
    
    /**
     * Retunrs the {@link ProcessSimulator ProcessSimulator} used by this 
     * instance, or 'null' if no process varinace is applied.
     */
    public ProcessSimulator getProcessSimulator();
    
    /**
     * Sets the {@link ProcessSimulator ProcessSimulator} to use. A
     * 'null' value indicates, that no process variance should
     * be applied.
     */
    public void setProcessSimulator(ProcessSimulator simulator);
}
