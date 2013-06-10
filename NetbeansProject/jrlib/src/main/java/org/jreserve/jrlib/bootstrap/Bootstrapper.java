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

import org.jreserve.jrlib.CalculationData;

/**
 * Abstract class for bootstrappers. The instance simply takes
 * a {@link CalculationData CalculationData}, which should be 
 * bootstrapped for some times.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class Bootstrapper<T extends CalculationData> implements Runnable {
    
    protected final T source;
    protected final int bootstrapCount;
    private final BootstrapCallBack callBack;
    private final int cbStep;
    
    /**
     * Creates in instance, which will bootstrap the given source.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws IllegalArgumentException if `bootstrapCount` is less then 1.
     */
    protected Bootstrapper(T source, int bootstrapCount) {
        this(source, bootstrapCount, null);
    }
    
    /**
     * Creates in instance, which will bootstrap the given source.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws IllegalArgumentException if `bootstrapCount` is less then 1.
     * @throws IllegalArgumentException if `cb` is not null and
     * {@link BootstrapCallBack#getCallbackStepSize() cb.getCallbackStepCount()} 
     * is less then 1.
     */
    protected Bootstrapper(T source, int bootstrapCount, BootstrapCallBack cb) {
        if(source == null) throw new NullPointerException("Source is null!");
        this.source = source;
        
        if(bootstrapCount < 1) throw new IllegalArgumentException("BootstrapCount was less then 1! "+bootstrapCount);
        this.bootstrapCount = bootstrapCount;
        
        if(cb == null) {
            cbStep = bootstrapCount+1;
            callBack = null;
        } else {
            cbStep = cb.getCallbackStepSize();
            if(cbStep < 1)
                throw new IllegalArgumentException("Call-back step count is less then 1! "+cbStep);
            callBack = cb;
        }
    }
    
    /**
     * Calls {@link #bootstrap() bootstrap()} n times, where
     * n is equal to {@link #getBootstrapCount() getBootstrapCount()}.
     */
    @Override
    public void run() {
        for(int n=0; n<bootstrapCount; n++) {
            bootstrap();
            if(callBack != null && ((n+1) % cbStep)==0)
                callBack.progress(bootstrapCount, n+1);
        }
        
        if(callBack!=null && (bootstrapCount % cbStep) != 0)
            callBack.progress(bootstrapCount, bootstrapCount);
    }
    
    /**
     * Retunrs the number of bootstrap iterations.
     */
    public int getBootstrapCount() {
        return bootstrapCount;
    }
    
    /**
     * Retunrs the bootstrapped calculation.
     */
    public T getSource() {
        return source;
    }
    
    /**
     * Extending class, should make a bootstrap iteration an 
     * store the results.
     */
    protected void bootstrap() {
        source.recalculate();
        collectBootstrapResult();
    }
    
    /**
     * Called after the source is recalculated. The
     * implementations should collect their results (estimates)
     * at this call.
     */
    protected abstract void collectBootstrapResult();
}
