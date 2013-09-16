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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.calculations.api.CalculationModifierFactory;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.annotations.CachedLayerRegistrationLoader;
import org.jreserve.jrlib.CalculationData;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CalculationModifierFactoryRegistry extends CachedLayerRegistrationLoader<CalculationModifierFactory> {
    
    private final static Logger logger = Logger.getLogger(CalculationModifierFactoryRegistry.class.getName());
    private final static Map<String, CalculationModifierFactoryRegistry> registries = new HashMap<String, CalculationModifierFactoryRegistry>();
    
    public static <C extends CalculationData> CalculationModifierFactory<C> getFactory(Class<C> category, String rootName) {
        return getFactory(category.getName(), rootName);
    }
    
    public static CalculationModifierFactory getFactory(String category, String rootName) {
        CalculationModifierFactoryRegistry r = registries.get(category);
        if(r == null) {
            r = new CalculationModifierFactoryRegistry(category);
            registries.put(category, r);
        }
        
        r.getValues();
        CalculationModifierFactory factory = r.cache.get(rootName);
        if(factory == null) {
            String msg = "CalculationModifierFactory for root name '%s' not found within category '%s'";
            logger.log(Level.WARNING, String.format(msg, rootName, category));
        }
        return factory;
    }
    
    private final String category;
    private final Map<String, CalculationModifierFactory> cache = new HashMap<String, CalculationModifierFactory>();
            
    private CalculationModifierFactoryRegistry(String category) {
        this.category = category;
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return String.format(CalculationModifierFactoryRegistrationProcessor.LAYER_PATH, category);
    }

    @Override
    protected CalculationModifierFactory getValue(FileObject file) throws Exception {
        String root = AnnotationUtils.stringAttribute(CalculationModifierFactoryRegistrationProcessor.ROOT_NAME, file);
        CalculationModifierFactory factory = super.getInstance(file);
        if(root != null) {
            if(cache.containsKey(root)) {
                String msg = "Root name '%s' is used multiple times within category '%s'!";
                logger.log(Level.WARNING, String.format(msg, root, category));
            } else {
                cache.put(root, factory);
            }
        } else {
            String msg = "Attribute '%s' not found for file '%s'!";
            logger.log(Level.WARNING, String.format(msg, CalculationModifierFactoryRegistrationProcessor.ROOT_NAME, file.getPath()));
        }
        
        return factory;
    }
}
