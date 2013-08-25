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
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PrimitiveInputStream extends InputStream {

    private final byte[] buffer = new byte[8];
    private final InputStream delegate;

    PrimitiveInputStream(InputStream delegate) {
        this.delegate = delegate;
    }
    
    public String readString() throws IOException {
        int length = readInt();
        char[] chars = new char[length];
        for(int i=0; i<length; i++)
            chars[i] = readChar();
        return new String(chars);
    }
    
    public char readChar() throws IOException {
        read(PrimitiveUtil.CHAR_LENGTH);
        return PrimitiveUtil.getChar(buffer);
    }
    
    private void read(int length) throws IOException {
        for(int i=0; i<length; i++) {
            int b = delegate.read();
            if(b == -1)
                throw new EOFException();
            buffer[i] = (byte)b;
        }
    }
    
    public boolean readBoolean() throws IOException {
        read(PrimitiveUtil.BOOLEAN_LENGTH);
        return PrimitiveUtil.getBoolean(buffer);
    }
    
    public byte readByte() throws IOException {
        return (byte) read();
    }
    
    public short readShort() throws IOException {
        read(PrimitiveUtil.SHORT_LENGTH);
        return PrimitiveUtil.getShort(buffer);
    }
    
    public int readInt() throws IOException {
        read(PrimitiveUtil.INT_LENGTH);
        return PrimitiveUtil.getInt(buffer);
    }
    
    public long readLong() throws IOException {
        read(PrimitiveUtil.LONG_LENGTH);
        return PrimitiveUtil.getLong(buffer);
    }
    
    public float readFloat() throws IOException {
        read(PrimitiveUtil.FLOAT_LENGTH);
        return PrimitiveUtil.getFloat(buffer);
    }
    
    public double readDouble() throws IOException {
        read(PrimitiveUtil.DOUBLE_LENGTH);
        return PrimitiveUtil.getDouble(buffer);
    }
    
    @Override
    public int read() throws IOException {
        int v = delegate.read();
        if(v == -1)
            throw new EOFException();
        return v;
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
