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
package org.jreserve.gui.misc.audit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.audit.event.AuditEvent;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AuditLogger implements Runnable {
    
    private final static Logger logger = Logger.getLogger(AuditLogger.class.getName());
    private final static String THREAD_NAME = "AuditLoggerThread";
    private final static BlockingQueue<AuditEvent> events = new LinkedBlockingQueue<AuditEvent>();
    
    private static Thread auditThread;
    private static AuditLogger instance;
    
    static void startLogger() {
        if(instance != null) {
            instance = new AuditLogger();
            startAuditThread();
            EventBusManager.getDefault().subscribe(instance);
            logger.log(Level.INFO, "Audit logger thread started...");
        }
    }
    
    private static void startAuditThread() {
        auditThread = new Thread(instance, THREAD_NAME);
        auditThread.setDaemon(true);
        auditThread.start();
    }
    
    static void stopLogger() {
        logger.log(Level.INFO, "Audit logger thread stopping...");
        try {
            EventBusManager.getDefault().unsubscribe(instance);
            auditThread.interrupt();
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception when stopping audit thread", ex);
        }
        
        logger.log(Level.INFO, "Flushing audit event queu...");
        synchronized(events) {
            while(!events.isEmpty())
                instance.logEvent(events.poll());
        }
    }
    
    private AuditLogger() {
    }
    
    @EventBusListener
    public void auditEventPublished(AuditEvent evt) {
        try {
            events.put(evt);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Unable to publish event: "+evt, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            while(true)
                logEvent(events.take());
        } catch (InterruptedException ex) {
            logger.log(Level.WARNING, THREAD_NAME + " interrupted!", ex);
        }
    }
    
    private void logEvent(AuditEvent event) {
        try {
        
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to log audit event: "+event, ex);
        }
    }
}
