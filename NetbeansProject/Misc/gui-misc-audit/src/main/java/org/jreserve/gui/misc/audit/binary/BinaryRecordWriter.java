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
import java.util.List;
import org.jreserve.gui.misc.audit.db.AuditDbManager;
import org.jreserve.gui.misc.audit.event.AuditEvent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class BinaryRecordWriter extends AbstractBinaryWriter<List<AuditEvent>>{

    BinaryRecordWriter(File file) {
        super(file, true);
    }

    @Override
    protected void writeToStream(List<AuditEvent> events) throws IOException {
        for(AuditEvent event : events)
            writeEvent(event);
    }

    private void writeEvent(AuditEvent event) throws IOException {
        os.writeLong(event.getChangeDate().getTime());
        os.writeLong(System.currentTimeMillis());
        os.writeLong(event.getComponentId());
        os.writeString(event.getComponent());
        os.writeString(event.getUserName());
        os.writeString(AuditDbManager.getMachineName());
        os.writeString(event.getChange());
    }
}
