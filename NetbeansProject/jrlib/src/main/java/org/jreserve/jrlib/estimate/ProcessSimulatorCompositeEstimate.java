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

import java.util.Collection;
import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.bootstrap.StaticProcessSimulator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProcessSimulatorCompositeEstimate
    extends CompositeEstimate 
    implements ProcessSimulatorEstimate {
    
    private final static ProcessSimulator DEFAULT_PROCESS_SIMULATOR = new StaticProcessSimulator();

    private ProcessSimulator process = DEFAULT_PROCESS_SIMULATOR;

    public ProcessSimulatorCompositeEstimate(Collection<ProcessSimulatorEstimate> estimates) {
        this(estimates.toArray(new ProcessSimulatorEstimate[estimates.size()]));
    }

    public ProcessSimulatorCompositeEstimate(ProcessSimulatorEstimate... estimates) {
        super(estimates);
    }
    
    @Override
    public ProcessSimulator getProcessSimulator() {
        return process==DEFAULT_PROCESS_SIMULATOR? null : process;
    }
    
    @Override
    public void setProcessSimulator(ProcessSimulator simulator) {
        invalidateSources();
        initProcessSimulator(simulator);
    }
    
    private void invalidateSources() {
        int size = getSourceCount();
        for(int i=0; i<size; i++)
            sources[i].setState(CalculationState.INVALID);
    }
    
    private void initProcessSimulator(ProcessSimulator simulator) {
        this.process = simulator==null? DEFAULT_PROCESS_SIMULATOR : simulator;
        for(Estimate e : sources)
            ((ProcessSimulatorEstimate)e).setProcessSimulator(this.process);
    }
}
