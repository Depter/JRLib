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

