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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PrimitiveUtil {
    final static int BOOLEAN_LENGTH = 1;
    final static int SHORT_LENGTH = 2;
    final static int INT_LENGTH = 4;
    final static int LONG_LENGTH = 8;
    final static int FLOAT_LENGTH = 4;
    final static int DOUBLE_LENGTH = 8;
    final static int CHAR_LENGTH = 2;
    
    /*
     * Methods for unpacking primitive values from byte arrays starting at
     * given offsets.
     */

    static boolean getBoolean(byte[] b) {
        return b[0] != 0;
    }

    static char getChar(byte[] b) {
        return (char) (
                ((b[1] & 0xFF) << 0) +
                ((b[0]) << 8)
         );
    }

    static short getShort(byte[] b) {
        return (short) (((b[1] & 0xFF) << 0) +
                        ((b[0]) << 8));
    }

    static int getInt(byte[] b) {
        return ((b[3] & 0xFF) << 0) +
               ((b[2] & 0xFF) << 8) +
               ((b[1] & 0xFF) << 16) +
               ((b[0]) << 24);
    }

    static float getFloat(byte[] b) {
        int i = ((b[3] & 0xFF) << 0) +
                ((b[2] & 0xFF) << 8) +
                ((b[1] & 0xFF) << 16) +
                ((b[0]) << 24);
        return Float.intBitsToFloat(i);
    }

    static long getLong(byte[] b) {
        return ((b[7] & 0xFFL) << 0) +
               ((b[6] & 0xFFL) << 8) +
               ((b[5] & 0xFFL) << 16) +
               ((b[4] & 0xFFL) << 24) +
               ((b[3] & 0xFFL) << 32) +
               ((b[2] & 0xFFL) << 40) +
               ((b[1] & 0xFFL) << 48) +
               (((long) b[0]) << 56);
    }

    static double getDouble(byte[] b) {
        long j = ((b[7] & 0xFFL) << 0) +
                 ((b[6] & 0xFFL) << 8) +
                 ((b[5] & 0xFFL) << 16) +
                 ((b[4] & 0xFFL) << 24) +
                 ((b[3] & 0xFFL) << 32) +
                 ((b[2] & 0xFFL) << 40) +
                 ((b[1] & 0xFFL) << 48) +
                 (((long) b[0]) << 56);
        return Double.longBitsToDouble(j);
    }

    /*
     * Methods for packing primitive values into byte arrays starting at given
     * offsets.
     */

    static void putBoolean(byte[] b, boolean val) {
        b[0] = (byte) (val ? 1 : 0);
    }

    static void putChar(byte[] b, char val) {
        b[1] = (byte) (val >>> 0);
        b[0] = (byte) (val >>> 8);
    }

    static void putShort(byte[] b, short val) {
        b[1] = (byte) (val >>> 0);
        b[0] = (byte) (val >>> 8);
    }

    static void putInt(byte[] b, int val) {
        b[3] = (byte) (val >>> 0);
        b[2] = (byte) (val >>> 8);
        b[1] = (byte) (val >>> 16);
        b[0] = (byte) (val >>> 24);
    }

    static void putFloat(byte[] b, float val) {
        int i = Float.floatToIntBits(val);
        b[3] = (byte) (i >>> 0);
        b[2] = (byte) (i >>> 8);
        b[1] = (byte) (i >>> 16);
        b[0] = (byte) (i >>> 24);
    }

    static void putLong(byte[] b, long val) {
        b[7] = (byte) (val >>> 0);
        b[6] = (byte) (val >>> 8);
        b[5] = (byte) (val >>> 16);
        b[4] = (byte) (val >>> 24);
        b[3] = (byte) (val >>> 32);
        b[2] = (byte) (val >>> 40);
        b[1] = (byte) (val >>> 48);
        b[0] = (byte) (val >>> 56);
    }

    static void putDouble(byte[] b, double val) {
        long j = Double.doubleToLongBits(val);
        b[7] = (byte) (j >>> 0);
        b[6] = (byte) (j >>> 8);
        b[5] = (byte) (j >>> 16);
        b[4] = (byte) (j >>> 24);
        b[3] = (byte) (j >>> 32);
        b[2] = (byte) (j >>> 40);
        b[1] = (byte) (j >>> 48);
        b[0] = (byte) (j >>> 56);
    }
}
