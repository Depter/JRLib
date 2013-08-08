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

package org.jreserve.gui.trianglewidget.model.registration;

import java.util.List;
import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.annotations.LayerRegistrationLoader;
import org.jreserve.gui.trianglewidget.model.TriangleModel;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleModelRegistry extends LayerRegistrationLoader<TriangleModelAdapter> {
    
    private static TriangleModelRegistry INSTANCE;
    
    public synchronized static List<TriangleModelAdapter> getAdapters() {
        if(INSTANCE == null)
            INSTANCE = new TriangleModelRegistry();
        return INSTANCE.getValues();
    }
    
    private final static Logger logger = Logger.getLogger(TriangleModelRegistry.class.getName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return TriangleModelRegistrationProcessor.LAYER_PATH;
    }

    @Override
    protected TriangleModelAdapter getValue(FileObject file) throws Exception {
        String name = getDisplayName(file);
        String iconBase = getIconBase(file);
        TriangleModel model = super.getInstance(file, TriangleModel.class);
        String id = getId(file, model);
        return new TriangleModelAdapter(id, name, iconBase, model);
    }
    
    private String getId(FileObject file, TriangleModel model) {
        return AnnotationUtils.stringAttribute(
                TriangleModelRegistrationProcessor.ID, 
                file, model.getClass().getName());
    }
    
    private String getDisplayName(FileObject file) {
        return AnnotationUtils.stringAttribute(
                TriangleModelRegistrationProcessor.DISPLAY_NAME, 
                file, null);
    }
    
    private String getIconBase(FileObject file) {
        return AnnotationUtils.stringAttribute(TriangleModelRegistrationProcessor.ICON_BASE, file, "");
    }
}
