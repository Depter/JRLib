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
package org.jreserve.gui.data.actions.createsourcewizard;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jreserve.gui.data.spi.DataSourceWizard;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.misc.annotations.CachedLayerRegistrationLoader;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataSourceWizardRegistry extends CachedLayerRegistrationLoader<DataSourceWizardAdapter>{
    
    private static DataSourceWizardRegistry INSTANCE;
    
    synchronized static List<DataSourceWizardAdapter> getAdapters() {
        if(INSTANCE == null)
            INSTANCE = new DataSourceWizardRegistry();
        return INSTANCE.getValues();
    }
    
    private final static Logger logger = Logger.getLogger(DataSourceWizardRegistry.class.getName());
    private final static Icon NO_ICON = new EmptyIcon();
    
    private DataSourceWizardRegistry() {
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return DataSourceWizardRegistrationProcessor.LAYER_PATH;
    }

    @Override
    protected DataSourceWizardAdapter getValue(FileObject file) throws Exception {
        String name = getDisplayName(file);
        Icon icon = getIcon(file);
        DataSourceWizard wizard = super.getInstance(file, DataSourceWizard.class);
        return new DataSourceWizardAdapter(name, icon, wizard);
    }
    
    private String getDisplayName(FileObject file) {
        return AnnotationUtils.stringAttribute(
                DataSourceWizardRegistrationProcessor.DISPLAY_NAME, 
                file, null);
    }
    
    private Icon getIcon(FileObject file) {
        String iconBase = AnnotationUtils.stringAttribute(
                DataSourceWizardRegistrationProcessor.ICON_BASE, file, "");
        return iconBase.length() > 0?
                ImageUtilities.loadImageIcon(iconBase, false) :
                NO_ICON;
    }
}
