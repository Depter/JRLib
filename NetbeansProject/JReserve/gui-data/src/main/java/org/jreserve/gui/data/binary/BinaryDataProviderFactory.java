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

package org.jreserve.gui.data.binary;

import java.util.Map;
import org.jreserve.gui.data.spi.DataProvider;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class BinaryDataProviderFactory implements DataProvider.Factory {
    
    final static String ID = BinaryDataProviderFactory.class.getName();
    private final static BinaryDataProviderFactory INSTANCE = new BinaryDataProviderFactory();
    
    public static BinaryDataProviderFactory getInstance() {
        return INSTANCE;
    }
    
    private BinaryDataProviderFactory() {
    }
    
    @Override
    public DataProvider createProvider(DataObject obj, Map<String, String> properties) {
        return new BinaryDataProvider(obj);
    }
}
