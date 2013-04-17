package org.jrlib.bootstrap;

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
