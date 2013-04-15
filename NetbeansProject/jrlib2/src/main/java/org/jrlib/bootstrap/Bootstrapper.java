package org.jrlib.bootstrap;

import org.jrlib.CalculationData;

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
    
    /**
     * Creates in instance, which will bootstrap the given source.
     * 
     * @throws NullPointerException if `source` is null.
     * @throws IllegalArgumentException if `bootstrapCount` is less then 1.
     */
    protected Bootstrapper(T source, int bootstrapCount) {
        if(source == null) throw new NullPointerException("Source is null!");
        this.source = source;
        if(bootstrapCount < 1) throw new IllegalArgumentException("BootstrapCount was less then 1! "+bootstrapCount);
        this.bootstrapCount = bootstrapCount;
    }
    
    /**
     * Calls {@link #bootstrap() bootstrap()} n times, where
     * n is equal to {@link #getBootstrapCount() getBootstrapCount()}.
     */
    @Override
    public void run() {
        for(int n=0; n<bootstrapCount; n++)
            bootstrap();
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
    protected abstract void bootstrap();
}
