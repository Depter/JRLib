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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractBinaryReader<T> {
    
    private final static Logger logger = Logger.getLogger(AbstractBinaryReader.class.getName());
    private final static int BUFFER_SIZE = 1024;
    
    protected final File file;
    protected PrimitiveInputStream is;

    protected AbstractBinaryReader(File file) {
        this.file = file;
    }
    
    T read() throws IOException {
        try {
            openStream();
            return readStream();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to read from: "+file.getAbsolutePath(), ex);
            throw ex;
        } finally {
            closeStream();
        }
    }
    
    private void openStream() throws IOException {
        InputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
        is = new PrimitiveInputStream(bis);
    }
    
    protected abstract T readStream() throws IOException;
    
    private void closeStream() {
        if(is != null) {
            try{
                is.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to close stream for file: "+file.getAbsolutePath(), ex);
            }
            is = null;
        }
    }
}
