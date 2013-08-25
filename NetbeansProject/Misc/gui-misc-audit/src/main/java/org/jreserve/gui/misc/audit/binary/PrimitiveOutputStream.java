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

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PrimitiveOutputStream extends OutputStream {
    
    private final OutputStream delegate;
    private final byte[] buffer = new byte[8];

    PrimitiveOutputStream(OutputStream delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void write(int b) throws IOException {
        delegate.write(b);
    }
    
    public void writeString(String str) throws IOException {
        int length = str.length();
        writeInt(length);
        for(int i=0; i<length; i++)
            writeChar(str.charAt(i));
    }
    
    public void writeBoolean(boolean v) throws IOException {
        PrimitiveUtil.putBoolean(buffer, v);
        writeBuffer(PrimitiveUtil.BOOLEAN_LENGTH);
    }
    
    public void writeChar(char v) throws IOException {
        PrimitiveUtil.putChar(buffer, v);
        writeBuffer(PrimitiveUtil.CHAR_LENGTH);
    }
    
    public void writeByte(byte v) throws IOException {
        write(v);
    }
    
    public void writeShort(short v) throws IOException {
        PrimitiveUtil.putShort(buffer, v);
        writeBuffer(PrimitiveUtil.SHORT_LENGTH);
    }
    
    private void writeBuffer(int length) throws IOException {
        delegate.write(buffer, 0, length);
    }
    
    public void writeInt(int v) throws IOException {
        PrimitiveUtil.putInt(buffer, v);
        writeBuffer(PrimitiveUtil.INT_LENGTH);
    }
    
    public void writeLong(long v) throws IOException {
        PrimitiveUtil.putLong(buffer, v);
        writeBuffer(PrimitiveUtil.LONG_LENGTH);
    }
    
    public void writeFloat(float v) throws IOException {
        PrimitiveUtil.putFloat(buffer, v);
        writeBuffer(PrimitiveUtil.FLOAT_LENGTH);
    }
    
    public void writeDouble(double v) throws IOException {
        PrimitiveUtil.putDouble(buffer, v);
        writeBuffer(PrimitiveUtil.DOUBLE_LENGTH);
    }
    
    @Override
    public void close() throws IOException {
        delegate.close();
    }
    
    @Override
    public void flush() throws IOException {
        delegate.flush();
    }
}
