package org.jreserve.grscript.util

import org.jreserve.grscript.AbstractDelegate

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TimerDelegate extends AbstractDelegate {

    private Map timers = [:]
    
    void initFunctions(Script script, ExpandoMetaClass emc) {
        super.initFunctions(script, emc)
        emc.startTimer   << this.&startTimer
        emc.stopTimer    << this.&stopTimer
        emc.finnishTimer << this.&finnishTimer
    }
    
    void startTimer(String task) {
        timers.put(task, System.currentTimeMillis())
    }
    
    long stopTimer(String task) {
        def begin = timers.remove(task)
        if(begin) {
            System.currentTimeMillis() - begin
        } else {
            -1
        }
    }
    
    void finnishTimer(String task) {
        double dur = stopTimer(task)
        if(dur < 0) {
            super.script.println "Task '${task}' was never started."
        } else {
            super.script.println "Task '${task}' finnished in ${dur/1000d} seconds."
        }
    }
}

