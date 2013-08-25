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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractBinaryWriter<T> {
    
    private final static Logger logger = Logger.getLogger(AbstractBinaryWriter.class.getName());
    private final static int BUFFER_SIZE = 1024;
    
    protected final File file;
    protected final boolean append;
    protected PrimitiveOutputStream os;

    protected AbstractBinaryWriter(File file, boolean append) {
        this.file = file;
        this.append = append;
    }

    void write(T value) throws IOException {
        try {
            openStream();
            writeToStream(value);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to write to: "+file.getAbsolutePath(), ex);
            throw ex;
        } finally {
            closeStream();
        }
    }

    private void openStream() throws IOException {
        OutputStream fos = new FileOutputStream(file, append);
        BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
        os = new PrimitiveOutputStream(bos);
    }

    protected abstract void writeToStream(T value) throws IOException;
    
    private void closeStream() {
        if(os != null) {
            try{
                os.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to close stream for file: "+file.getAbsolutePath(), ex);
            }
            os = null;
        }
    }
}
