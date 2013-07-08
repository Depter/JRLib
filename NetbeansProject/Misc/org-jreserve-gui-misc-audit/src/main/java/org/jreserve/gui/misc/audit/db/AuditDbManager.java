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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.audit.event.AuditEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AuditDbManager {
    
    private final static Logger logger = Logger.getLogger(AuditDbManager.class.getName());
    private final static String AUDIT_PATH = "Audit";
    
    public static AuditDbManager getInstance() {
        return null;
    }
    
    private final Map<String, AuditDb> dbs = new HashMap<String, AuditDb>();
    
    private AuditDbManager() {
    }
    
    public AuditDb getAuditDb(FileObject projectFolder) {
        String path = projectFolder.getPath();
        AuditDb db = dbs.get(projectFolder.getPath());
        if(db == null) {
            db = createAuitDb(projectFolder);
            dbs.put(path, db);
        }
        return db;
    }
    
    private AuditDb createAuitDb(FileObject projectFolder) {
        try {
            
            FileObject auditDir = FileUtil.createFolder(projectFolder, AUDIT_PATH);
            
            return null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to create audit db within project: "+projectFolder.getPath(), ex);
            return new DummyDb(projectFolder);
        }
    }
    
    private static class DummyDb implements AuditDb {

        private final FileObject projectDir;
        
        private DummyDb(FileObject projectDir) {
            this.projectDir = projectDir;
        }
        
        @Override
        public FileObject getProjectDir() {
            return projectDir;
        }

        @Override
        public FileObject getDbFile() {
            return null;
        }

        @Override
        public void storeEvent(AuditEvent event) {
            String msg = "Unable to log AuditEvent, because audit db was not ceated!\n\t"+event;
            logger.log(Level.SEVERE, msg);
        }
    }
}
