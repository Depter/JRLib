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
package org.jreserve.gui.data.csv.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PreviewReader implements Runnable {
    
    private final static int READ_CONT = 5;
    private final static int BUFFER_SIZE = 1024;
    
    private final File file;
    private BufferedReader reader = null;
    private volatile String[] lines;
    
    PreviewReader(File file) {
        this.file = file;
    }
    
    @Override
    public void run() {
        lines = readLines();
    }
    
    String[] readLines() {
        try {
            reader = new BufferedReader(new FileReader(file), BUFFER_SIZE);
            String[] result = new String[READ_CONT];
            int lineCount = 0;
            String line;
            while((line = reader.readLine()) != null && lineCount < READ_CONT)
                result[lineCount++] = line;
            return result;
        } catch(Exception ex) {
            return new String[0];
        } finally {
            if(reader != null)
                try {reader.close();} catch (IOException ex) {}
            reader = null;
        }
    }
}
