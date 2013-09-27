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
package org.jreserve.gui.plot.colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.CachedLayerRegistrationLoader;
import org.jreserve.gui.plot.ColorGenerator;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ColorGeneratorRegistry extends CachedLayerRegistrationLoader<AbstractColorGeneratorAdapter> {

    private final static Logger logger = Logger.getLogger(ColorGeneratorRegistry.class.getName());
    
    private static List<AbstractColorGeneratorAdapter> values;
    private static ColorGeneratorRegistry INSTANCE;
    
    public synchronized static AbstractColorGeneratorAdapter getAdapter(String id) {
        if(values == null)
            loadValues();
        for(AbstractColorGeneratorAdapter adapter : values)
            if(adapter.getId().equals(id))
                return adapter;
        return null;
    }
    
    public synchronized static List<AbstractColorGeneratorAdapter> getAdapters() {
        if(values == null)
            loadValues();
        return new ArrayList<AbstractColorGeneratorAdapter>(values);
    }
    
    private static void loadValues() {
        ColorGeneratorRegistry registry = new ColorGeneratorRegistry();
        values = registry.getValues();
        Collections.sort(values);
    }
    
    public synchronized static ColorGenerator getFirstColorGenerator() {
        if(values == null)
            loadValues();
        if(values.isEmpty())
            return new DefaultColorGenerator();
        return values.iterator().next().createColorGenerator();
    }
    
    private ColorGeneratorRegistry() {
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return ColorGeneratorRegistrationProcessor.PATH;
    }

    @Override
    protected AbstractColorGeneratorAdapter getValue(FileObject file) throws Exception {
        if(ListColorGeneratorRegistrationProcessor.EXTENSION.equals(file.getExt())) {
            return new ListColorGeneratorAdapter(file);
        } else {
            Object o = file.getAttributes();
            ColorGenerator.Factory delegate = super.getInstance(file, ColorGenerator.Factory.class);
            return new ColorGeneratorAdapter(file, delegate);
        }
    }
}
