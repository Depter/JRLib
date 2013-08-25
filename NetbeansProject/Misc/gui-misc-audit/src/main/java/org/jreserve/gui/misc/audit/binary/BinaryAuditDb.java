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

package org.jreserve.gui.misc.audit.binary;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.audit.db.AuditDb;
import org.jreserve.gui.misc.audit.event.AuditEvent;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;


/**
 *
 * @author Peter Decsi
 * @version 1.0
 */ 
@Messages({
    "MSG.BinaryAuditDb.StoreException=Unable to store audit event!",
    "MSG.BinaryAuditDb.LoadException=Unable to load audit events!"
})
class BinaryAuditDb implements AuditDb {
    
    private final static Logger logger = Logger.getLogger(BinaryAuditDb.class.getName());
    private final static String DB_PATH = "Audit/db.aud";
    private final static String ID_PATH = "Audit/id.aud";
    
    private final File dbFile;
    private final File idFile;
    
    BinaryAuditDb(Project project) throws IOException {
        FileObject projectDir = project.getProjectDirectory();
        dbFile = createFile(projectDir, DB_PATH);
        idFile = createFile(projectDir, ID_PATH);
    }
    
    private File createFile(FileObject folder, String path) throws IOException {
        FileObject file = FileUtil.createData(folder, path);
        return FileUtil.toFile(file);
    }

    @Override
    public void storeEvent(AuditEvent event) {
        try {
            new BinaryRecordWriter(dbFile).write(Collections.singletonList(event));
        } catch (Exception ex) {
            BubbleUtil.showException(Bundle.MSG_BinaryAuditDb_StoreException(), ex);
            logger.log(Level.SEVERE, "Unable to read audit events!", ex);
        }
    }

    @Override
    public List<AuditRecord> getAuditRecords() {
        try {
            return new BinaryRecordReader(dbFile).read();
        } catch (IOException ex) {
            BubbleUtil.showException(Bundle.MSG_BinaryAuditDb_LoadException(), ex);
            logger.log(Level.SEVERE, "Unable to read audit events!", ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public long getNextObjectId() {
        try {
            long id = new BinaryIdReader(idFile).read();
            new BinaryIdWriter(idFile).write(id++);
            return id;
        } catch (IOException ex) {
            return -1;
        }
    }

    @Override
    public void close() {
    }
}
