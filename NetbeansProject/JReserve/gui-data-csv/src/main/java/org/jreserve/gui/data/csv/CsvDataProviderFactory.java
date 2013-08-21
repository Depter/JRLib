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
package org.jreserve.gui.data.csv;

import java.util.Map;
import org.jreserve.gui.data.spi.DataProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CsvDataProviderFactory implements DataProvider.Factory {
    
    private static CsvDataProviderFactory INSTANCE;
    
    @DataProvider.FactoryRegistration(
        id="org.jreserve.gui.data.csv.CsvDataProviderFactory"
    )
    public synchronized static DataProvider.Factory getInstance() {
        if(INSTANCE == null)
            INSTANCE = new CsvDataProviderFactory();
        return INSTANCE;
    }
    
    private CsvDataProviderFactory() {
    }
    
    @Override
    public String getId() {
        return "org.jreserve.gui.data.csv.CsvDataProviderFactory";
    }

    @Override
    public DataProvider createProvider(Map<String, String> properties) {
        return new CsvDataProvider(this);
    }
}
