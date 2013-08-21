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

package org.jreserve.gui.data.csv.input.triangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.data.csv.input.AbstractCsvReader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CsvTriangleReader extends AbstractCsvReader<double[][], AbstractCsvReader.CsvFormatData>{
    
    private List<double[]> rows = new ArrayList<double[]>();
    
    CsvTriangleReader(AbstractCsvReader.CsvFormatData config) {
        super(config);
    }

    @Override
    protected double[][] getResult() throws IOException {
        int size = rows.size();
        double[][] result = new double[size][];
        for(int i=0; i<size; i++)
            result[i] = rows.get(i);
        return result;
    }
    
    @Override
    protected void readRow(String[] cells) throws IOException {
        int size = cells.length;
        double[] result = new double[size];
        
        for(int i=0; i<size; i++) {
            try {
                result[i] = getDouble(cells[i]);
            } catch (Exception ex) {
                throw valueException(ex, cells[i], i);
            }
        }
        
        rows.add(result);
    }
    
    private double getDouble(String str) {
        str = str.replace(config.getDecimalSep(), '.');
        return Double.parseDouble(str);
    }
    
    private IOException valueException(Exception ex, String cell, int column) {
        String msg = "Unable to parse value from '%s' at line '%d', column '%d'!";
        int index = column + (config.hasRowHeader()? 2 : 1);
        msg = String.format(msg, cell, lineIndex+1, index);
        return new IOException(msg, ex);
    }
}
