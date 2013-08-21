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
public class PreviewReader implements Runnable {
    
    private final static int BUFFER_SIZE = 1024;
    
    private final File file;
    private final int readCount;
    private BufferedReader reader = null;
    private volatile String[] lines;
    
    public PreviewReader(File file, int readCount) {
        this.file = file;
        this.readCount = readCount;
    }
    
    @Override
    public void run() {
        lines = readLines();
    }
    
    public String[] readLines() {
        try {
            reader = new BufferedReader(new FileReader(file), BUFFER_SIZE);
            String[] result = new String[readCount];
            int lineCount = 0;
            String line;
            while((line = reader.readLine()) != null && lineCount < readCount)
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
