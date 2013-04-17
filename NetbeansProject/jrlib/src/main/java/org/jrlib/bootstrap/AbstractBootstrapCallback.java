package org.jrlib.bootstrap;

/**
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractBootstrapCallback implements BootstrapCallBack {

    private final int stepSize;
    
    /**
     * Creates a new instance.
     * 
     * @throws IllegalArgumentException if `stepSize` is less tehn 1.
     */
    protected AbstractBootstrapCallback(int stepSize) {
        if(stepSize < 1)
            throw new IllegalArgumentException("Stepsize is less then 1! "+stepSize);
        this.stepSize = stepSize;
    }
    
    @Override
    public int getCallbackStepSize() {
        return stepSize;
    }
}
