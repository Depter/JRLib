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
package org.jreserve.gui.calculations.util;

import java.util.List;
import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.CachedLayerRegistrationLoader;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultColorRegistry extends CachedLayerRegistrationLoader<DefaultColorAdapter> {
    
    private final static Logger logger = Logger.getLogger(DefaultColorRegistry.class.getName());

    private static DefaultColorRegistry INSTANCE;
    
    public synchronized static DefaultColorAdapter getColor(String id) {
        for(DefaultColorAdapter adapter : getColors())
            if(id.equals(adapter.getId()))
                return adapter;
        return null;
    }
    
    public synchronized static List<DefaultColorAdapter> getColors() {
        if(INSTANCE == null)
            INSTANCE = new DefaultColorRegistry();
        return INSTANCE.getValues();
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return DefaultColorRegistrationProcessor.FOLDER;
    }

    @Override
    protected DefaultColorAdapter getValue(FileObject file) throws Exception {
        return new DefaultColorAdapter(file);
    }

}