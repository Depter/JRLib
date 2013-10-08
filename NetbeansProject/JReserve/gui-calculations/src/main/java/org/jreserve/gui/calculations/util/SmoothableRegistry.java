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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.CachedLayerRegistrationLoader;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothableRegistry extends CachedLayerRegistrationLoader<SmoothableAdapter> {
    
    private final static Logger logger = Logger.getLogger(SmoothableRegistry.class.getName());
    private final static Map<String, SmoothableRegistry> registries = new HashMap<String, SmoothableRegistry>();

    public static List<SmoothableAdapter> getAdapters(String category) {
        SmoothableRegistry registry = registries.get(category);
        if(registry == null) {
            registry = new SmoothableRegistry(category);
            registries.put(category, registry);
        }
        return registry.getValues();
    }
    
    private final String category;
    
    private SmoothableRegistry(String category) {
        this.category = category;
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return String.format(SmoothableRegistrationProcessor.LAYER_PATH, category);
    }

    @Override
    protected SmoothableAdapter getValue(FileObject file) throws Exception {
        try {
            return new SmoothableAdapter(file);
        } catch (Exception ex) {
            String msg = String.format("Unable to load Smoothable from file '%s'", file.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw ex;
        }
    }
}
