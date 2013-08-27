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
package org.jreserve.gui.misc.audit.db;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.audit.event.AuditEvent;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AuditDbManager {

    private final static Logger logger = Logger.getLogger(AuditDbManager.class.getName());
    
    
    protected final static String AUDIT_PATH = "Audit";
    private static AuditDbManager INSTANCE;
    private static String MACHINE_NAME = null;
    
    public synchronized static AuditDbManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = Lookup.getDefault().lookup(AuditDbManager.class);
            if(INSTANCE == null) {
                INSTANCE = new DummyDbManager();
                logger.warning("No ServiceProvider found for AuditDbManager service! Dummy implementation will be used.");
            }
        }
        return INSTANCE;
    }
    
    public synchronized static String getMachineName() {
        if(MACHINE_NAME == null) {
            MACHINE_NAME = "Unknown";
            try {
                MACHINE_NAME = InetAddress.getLocalHost().getHostName();
                logger.log(Level.CONFIG, "Machine name set to: {0}", MACHINE_NAME);
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Unable to determine machine name!", ex);
            }
        }
        return MACHINE_NAME;
    }

    private final Map<Project, AuditDb> dbs = new HashMap<Project, AuditDb>();
    private boolean closed = false;
    
    private synchronized AuditDb getAuditDb(Project project) {
        if(project == null)
            throw new NullPointerException("Project is null!");
        if(closed)
            throw new IllegalStateException("AuditDbManager is closed!");
        
        AuditDb db = dbs.get(project);
        if(db == null) {
            try {
                db = createAuitDb(project);
            } catch (Exception ex) {
                FileObject projectDir = project.getProjectDirectory();
                logger.log(Level.SEVERE, "Unable to create audit db within project: "+projectDir.getPath(), ex);
                db = new DummyDb();
            }
            dbs.put(project, db);
        }
        return db;
    }
    
    protected abstract AuditDb createAuitDb(Project project) throws Exception;
    
    public List<AuditRecord> getAuditRecords(Project project) {
        AuditDb db = getAuditDb(project);
        synchronized(db) {
            return db.getAuditRecords();
        }
    }
    
    public void storeEvent(AuditEvent event) {
        AuditDb db = getAuditDb(event.getAuditedProject());
        synchronized(db) {
            db.storeEvent(event);
        }
    }
    
    public long getNextObjectId(Project project) {
        AuditDb db = getAuditDb(project);
        synchronized(db) {
            return db.getNextObjectId();
        }
    }
    
    public synchronized void close() {
        if(!closed) {
            for(AuditDb db : dbs.values())
                db.close();
            closed = true;
        }
    }
    
    private final static class DummyDb implements AuditDb {
        
        private final static Logger logger = Logger.getLogger(DummyDb.class.getName());
        
        private DummyDb() {
        }

        @Override
        public void storeEvent(AuditEvent event) {
            String msg = "Unable to log AuditEvent, because audit db was not ceated!\n\t"+event;
            logger.log(Level.SEVERE, msg);
        }

        @Override
        public List<AuditRecord> getAuditRecords() {
            return Collections.EMPTY_LIST;
        }
        
        @Override
        public void close() {
        }

        @Override
        public long getNextObjectId() {
            return -1;
        }
    }
    
    private final static class DummyDbManager extends AuditDbManager {

        @Override
        protected AuditDb createAuitDb(Project project) throws Exception {
            throw new UnsupportedOperationException("DummyDbManager is used!");
        }
    }
}
