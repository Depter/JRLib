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
package org.jreserve.gui.data.api.impl;

import java.util.List;
import java.util.logging.Logger;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.misc.annotations.CachedLayerRegistrationLoader;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - id",
    "MSG.DataProviderFactoryRegistry.NoFactory=Data provider for id ''{0}'' not found!"
})
class DataProviderFactoryRegistry extends CachedLayerRegistrationLoader<DataProvider.Factory>{
    
    private final static Logger logger = Logger.getLogger(DataProviderFactoryRegistry.class.getName());
    
    private static DataProviderFactoryRegistry INSTANCE;
    
    synchronized static DataProvider.Factory getFactory(String id) {
        for(DataProvider.Factory factory : getFactories())
            if(factory.getId().equals(id))
                return factory;
        BubbleUtil.showError(Bundle.MSG_DataProviderFactoryRegistry_NoFactory(id));
        logger.warning(String.format("Unable to find DataProvider.Factory for id '%s'! EmptyDataProvider will be used instead", id));
        return EmptyDataProvider.FACTORY;
    }
    
    private synchronized static List<DataProvider.Factory> getFactories() {
        if(INSTANCE == null)
            INSTANCE = new DataProviderFactoryRegistry();
        return INSTANCE.getValues();
    }
    
    private DataProviderFactoryRegistry() {
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return DataProviderRegistrationProcessor.LAYER_PATH;
    }

    @Override
    protected DataProvider.Factory getValue(FileObject file) throws Exception {
        return super.getInstance(file);
    }
}
