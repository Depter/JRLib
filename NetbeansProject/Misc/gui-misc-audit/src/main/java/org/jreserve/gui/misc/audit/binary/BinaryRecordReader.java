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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jreserve.gui.misc.audit.event.AuditRecord;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BinaryRecordReader extends AbstractBinaryReader<List<AuditRecord>>{

    BinaryRecordReader(File file) {
        super(file);
    }

    @Override
    List<AuditRecord> read() throws IOException {
        if(file.length() == 0)
            return Collections.EMPTY_LIST;
        return super.read(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<AuditRecord> readStream() throws IOException {
        List<AuditRecord> records = new ArrayList<AuditRecord>();
        while(true) {
            try {
                records.add(readRecord());
            } catch (EOFException ex) {
                return records;
            }
        }
    }
    
    private AuditRecord readRecord() throws IOException {
        Date changeDate = new Date(is.readLong());
        Date logDate = new Date(is.readLong());
        long objId = is.readLong();
        String objName = is.readString();
        String user = is.readString();
        String machine = is.readString();
        String change = is.readString();
        return new AuditRecord(changeDate, logDate, objId, objName, user, machine, change);
    }

}
