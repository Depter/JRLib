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

import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.bootstrap.ProcessSimulator;
import org.jreserve.jrlib.bootstrap.StaticProcessSimulator;

/**
 * Abstract implementation for the 
 * {@link ProcessSimulatorEstimate ProcessSimulatorEstimate} interface.
 * 
 * The class handles the process varinace and most of the calculation,
 * extending classes should only implement the
 * <ul>
 *   <li>{@link #initDimensions() initDimensions()},</li>
 *   <li>{@link #getObservedValue(int, int) getObservedValue()},</li>
 *   <li>{@link #getEstimatedValue(int, int) getEstimatedValue()}</li>
 * </ul>,
 * methods in addition to the abstract methods in the 
 * {@link AbstractEstimate AbstractEstimate} class.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractProcessSimulatorEstimate<T extends CalculationData> 
    extends AbstractEstimate<T> 
    implements ProcessSimulatorEstimate {
    
    private final static ProcessSimulator DEFAULT_PROCESS_SIMULATOR = new StaticProcessSimulator();
    
    protected ProcessSimulator process = DEFAULT_PROCESS_SIMULATOR;
    
    /**
     * Creates an empty instance.
     */
    protected AbstractProcessSimulatorEstimate() {
    }
    
    /**
     * Creates an instance wiht the given source.
     * 
     * @throws NullPointerException if 'source' is null. 
     */
    protected AbstractProcessSimulatorEstimate(T source) {
        super(source);
    }
    
    @Override
    public ProcessSimulator getProcessSimulator() {
        return process==DEFAULT_PROCESS_SIMULATOR? null : process;
    }
    
    @Override
    public void setProcessSimulator(ProcessSimulator simulator) {
        setState(CalculationState.INVALID);
        this.process = simulator==null? 
                DEFAULT_PROCESS_SIMULATOR :
                simulator;
        recalculateLayer();
        setState(CalculationState.VALID);
    }
    
    @Override
    protected void recalculateLayer() {
        initDimensions();
        calculateValues();
        cleanUpCalculation();
    }
    
    /**
     * Extending classes should set the appropriate values
     * for the accident and development count.
     */
    protected abstract void initDimensions();
    
    /**
     * Calculates the estimate table, by calling
     * {@link #fillAccident(int) fillAccident()} for
     * each accident period.
     */
    protected void calculateValues() {
        this.values = new double[accidents][developments];
        for(int a=0; a<accidents; a++)
            fillAccident(a);
    }
    
    /**
     * Filles an accident period. A given cell will be filled with
     * either {@link #getObservedValue(int, int) getObservedValue()} or
     * {@link #getEstimatedValue(int, int) getEstimatedValue()}, according
     * to the method 
     * {@link #getObservedDevelopmentCount(int) getObservedDevelopmentCount()}.
     */
    protected void fillAccident(int accident) {
        int observedDevs = getObservedDevelopmentCount(accident);
        for(int d=0; d<observedDevs; d++)
            this.values[accident][d] = getObservedValue(accident, d);
        
        for(int d=observedDevs; d<developments; d++) {
            double v = getEstimatedValue(accident, d);
            this.values[accident][d] = process.simulateEstimate(v, accident, d);
        }
    }
    
    /**
     * Extending clases should return the original value for the given 
     * cell. It is safe to asume, that all cells for the accident periods 
     * above, and all cells before this cell wihtin this accident period
     * are already filled.
     */
    protected abstract double getObservedValue(int accident, int development);
    
    /**
     * Extending clases should return the estimated value for the given 
     * cell. It is safe to asume, that all cells for the accident periods 
     * above, and all cells before this cell wihtin this accident period
     * are already filled.
     */
    protected abstract double getEstimatedValue(int accident, int development);
    
    /**
     * Does nothing. Called after the estimate table is filled
     * to enable extending classes to do some clean up
     * adter the calculation.
     */
    protected void cleanUpCalculation() {
    }
}
