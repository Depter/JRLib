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
package org.jreserve.gui.data.spi.util;

import java.util.List;
import java.util.logging.Logger;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.util.DataProviderFactoryRegistry.FactoryEntry;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
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
public class DataProviderFactoryRegistry extends CachedLayerRegistrationLoader<FactoryEntry>{
    
    private final static Logger logger = Logger.getLogger(DataProviderFactoryRegistry.class.getName());
    
    private static DataProviderFactoryRegistry INSTANCE;
    
    public synchronized static DataProvider.Factory getFactory(String id) {
        for(FactoryEntry entry : getEntries())
            if(entry.id.equals(id))
                return entry.factory;
        BubbleUtil.showError(Bundle.MSG_DataProviderFactoryRegistry_NoFactory(id));
        logger.warning(String.format("Unable to find DataProvider.Factory for id '%s'! EmptyDataProvider will be used instead", id));
        return EmptyDataProvider.FACTORY;
    }
    
    private synchronized static List<FactoryEntry> getEntries() {
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
    protected FactoryEntry getValue(FileObject file) throws Exception {
        String id = AnnotationUtils.stringAttribute(DataProviderRegistrationProcessor.ID, file);
        DataProvider.Factory factory = getInstance(file, DataProvider.Factory.class);
        return new FactoryEntry(id, factory);
    }
    
    public static class FactoryEntry {
        private final String id;
        private final DataProvider.Factory factory;

        public FactoryEntry(String id, DataProvider.Factory factory) throws Exception {
            this.factory = factory;
            this.id = id;
        }
    }
}
