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

package org.jreserve.gui.data.binaryprovider;

import java.util.Map;
import org.jreserve.gui.data.spi.DataProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BinaryDataProviderFactory implements DataProvider.Factory {
    
    private final static String ID = BinaryDataProviderFactory.class.getName();
    private static BinaryDataProviderFactory INSTANCE;
    
    public synchronized static DataProvider.Factory getInstance() {
        if(INSTANCE == null)
            INSTANCE = new BinaryDataProviderFactory();
        return INSTANCE;
    }
    
    private BinaryDataProviderFactory() {
    }
    
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public DataProvider createProvider(Map<String, String> properties) {
        return new BinaryDataProvider(this);
    }
}
