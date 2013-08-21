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
import org.jreserve.gui.misc.audit.db.AuditDbManager;
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
        if (instance == null) {
            instance = new AuditLogger();
            startAuditThread();
            EventBusManager.getDefault().subscribe(instance.eventListener);
            EventBusManager.getDefault().subscribe(instance.providerListener);
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
            EventBusManager.getDefault().unsubscribe(instance.eventListener);
            EventBusManager.getDefault().unsubscribe(instance.providerListener);
            auditThread.interrupt();
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception when stopping audit thread", ex);
        }
    }
    
    private final EventListener eventListener = new EventListener();
    private final ProviderListener providerListener = new ProviderListener();
    private final Object stopLock = new Object();
    private boolean isStopped = false;
    
    private AuditLogger() {
    }

    @Override
    public void run() {
        try {
            while (true) {
                logEvent(events.take());
            }
        } catch (InterruptedException ex) {
            flushEvents();
            logger.info("Audit logger thread stopped.");
        }
    }
    
    private void flushEvents() {
        logger.log(Level.INFO, "Flushing audit event queu...");
        synchronized (events) {
            while (!events.isEmpty())
                logEvent(events.poll());
        }
    }

    private void logEvent(AuditEvent event) {
        try {
            AuditDbManager.getInstance().storeEvent(event);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to log audit event: " + event, ex);
        }
    }

    public class EventListener {

        private EventListener() {
        }

        @EventBusListener
        public void auditEventPublished(AuditEvent evt) {
            try {
                events.put(evt);
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Unable to publish event: " + evt, ex);
            }
        }
    }

    public class ProviderListener {

        private ProviderListener() {
        }

        @EventBusListener
        public void auditEventPublished(AuditEvent.Provider provider) {
            try {
                AuditEvent evt = provider.getAuditEvent();
                if(evt == null)
                    throw new NullPointerException("AuditEvent.Provider '"+provider+"' provided a null AuditEvent!");
                events.put(evt);
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Unable to publish event: " + provider, ex);
            }
        }
    }
}
