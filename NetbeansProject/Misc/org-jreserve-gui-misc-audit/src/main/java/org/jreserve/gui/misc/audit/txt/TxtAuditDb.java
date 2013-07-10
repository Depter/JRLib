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
package org.jreserve.gui.misc.audit.txt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.audit.db.AuditDb;
import org.jreserve.gui.misc.audit.event.AuditEvent;
import org.jreserve.gui.misc.audit.event.AuditRecord;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TxtAuditDb.StoreException=Unable to store audit event!",
    "MSG.TxtAuditDb.LoadException=Unable to load audit events!"
})
class TxtAuditDb implements AuditDb {
    
    private final static Logger logger = Logger.getLogger(TxtAuditDb.class.getName());
    private final static int BUFFER_SIZE = 1024;
    
    private final FileObject projectDir;
    private final FileObject auditFile;
    private final FileLock lock;
    private final StringBuilder sb = new StringBuilder();
    
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    
    TxtAuditDb(FileObject projectFolder) throws IOException {
        this.projectDir = projectFolder;
        this.auditFile = FileUtil.createData(projectFolder, "Audit/db.aud");
        this.lock = auditFile.lock();
    }
    
    @Override
    public FileObject getProjectDir() {
        return projectDir;
    }

    @Override
    public FileObject getDbFile() {
        return auditFile;
    }

    @Override
    public void storeEvent(AuditEvent event) {
        try {
            openWriter();
            writeEvent(event);
        } catch (IOException ex) {
            BubbleUtil.showException(Bundle.MSG_TxtAuditDb_StoreException(), ex);
            logger.log(Level.SEVERE, "Unable to store audit event: "+event, ex);
        } finally {
            closeWriter();
        }
    }
    
    private void openWriter() throws IOException {
        OutputStream os = auditFile.getOutputStream(lock);
        writer = new ObjectOutputStream(new BufferedOutputStream(os, BUFFER_SIZE));
    }
    
    //CHANGE_DATE | LOG_DATE | COMPONENT | USER | CHANGE
    private void writeEvent(AuditEvent event) throws IOException {
        writer.writeLong(event.getChangeDate().getTime());
        writer.writeLong(new Date().getTime());
        writeString(event.getComponent());
        writeString(event.getUserName());
        writeString(event.getChange());
    }
    
    private void writeString(String str) throws IOException {
        if(str == null)
            str = "";
        writer.writeInt(str.length());
        writer.writeChars(str);
    }
    
    private void closeWriter() {
        if(writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to close writer for audit file: "+auditFile.getPath(), ex);
            }
            writer = null;
        }
    }
    
    @Override
    public List<AuditRecord> getAuditRecords() {
        List<AuditRecord> records = new ArrayList<AuditRecord>();
        try {
            openReader();
            while(true)
                records.add(readRecord());
        } catch(EOFException ex) {
            return records;
        } catch (IOException ex) {
            BubbleUtil.showException(Bundle.MSG_TxtAuditDb_LoadException(), ex);
            logger.log(Level.SEVERE, "Unable to read audit events!", ex);
        } finally {
            closeReader();
        }
        return records;
    }
    
    private AuditRecord readRecord() throws IOException {
        Date changeDate = new Date(reader.readLong());
        Date logDate = new Date(reader.readLong());
        String component = readString();
        String user = readString();
        String change = readString();
        return new AuditRecord(changeDate, logDate, component, user, change);
    }
    
    private String readString() throws IOException {
        int length = reader.readInt();
        sb.setLength(0);
        for(int i=0; i<length; i++)
            sb.append(reader.readChar());
        return sb.toString();
    }
    
    private void openReader() throws IOException {
        InputStream is = auditFile.getInputStream();
        reader = new ObjectInputStream(new BufferedInputStream(is, BUFFER_SIZE));
    }
    
    
    private void closeReader() {
        if(reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to close reader for audit file: "+auditFile.getPath(), ex);
            }
            reader = null;
        }
    }

    @Override
    public void close() {
        if(lock != null)
            lock.releaseLock();
    }
}
