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
package org.jreserve.gui.misc.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EDTExceptionLogger implements Thread.UncaughtExceptionHandler {

    static void install() {
        Thread.setDefaultUncaughtExceptionHandler(new EDTExceptionLogger());
        System.setProperty("sun.awt.exception.handler", EDTExceptionLogger.class.getName());
    }

    private final static Logger logger = Logger.getLogger(EDTExceptionLogger.class.getName());

    private EDTExceptionLogger() {
    }
    
    public void handle(Throwable e) {
        handleException(Thread.currentThread(), e);
    }
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleException(t, e);
    }
    
    private void handleException(Thread t, Throwable e) {
        String msg = "Uncaught exception at thread: %s";
        msg = String.format(msg, t.getName());
        logger.log(Level.SEVERE, msg, e);
    }
}
