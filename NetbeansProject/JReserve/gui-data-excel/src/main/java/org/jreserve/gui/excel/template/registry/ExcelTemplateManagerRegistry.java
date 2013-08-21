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
package org.jreserve.gui.excel.template.registry;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.annotations.LayerRegistrationLoader;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcelTemplateManagerRegistry extends LayerRegistrationLoader<ExcelTemplateManagerAdapter> {
    
    public synchronized static List<ExcelTemplateManagerAdapter> getInstances() {
        ExcelTemplateManagerRegistry registry = new ExcelTemplateManagerRegistry();
        return registry.getValues();
    }
    
    private final static Logger logger = Logger.getLogger(ExcelTemplateManagerRegistry.class.getName());

    private ExcelTemplateManagerRegistry() {
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return ExcelTemplateManagerRegistrationProcessor.LAYER_PATH;
    }

    @Override
    protected ExcelTemplateManagerAdapter getValue(FileObject file) throws Exception {
        String name = getDisplayName(file);
        Icon icon = getIcon(file);
        ExcelTemplateManager delegate = super.getInstance(file, ExcelTemplateManager.class);
        return new ExcelTemplateManagerAdapter(delegate, name, icon);
    }    
    
    private String getDisplayName(FileObject file) {
        return AnnotationUtils.stringAttribute(
                ExcelTemplateManagerRegistrationProcessor.PROP_DISPLAY_NAME, 
                file, file.getName());
    }
    
    private Icon getIcon(FileObject file) {
        String base = AnnotationUtils.stringAttribute(
                ExcelTemplateManagerRegistrationProcessor.PROP_ICON, file);
        if(base == null || base.length()==0)
            return EmptyIcon.EMPTY_16;
        return ImageUtilities.loadImageIcon(base, false);
    }
}