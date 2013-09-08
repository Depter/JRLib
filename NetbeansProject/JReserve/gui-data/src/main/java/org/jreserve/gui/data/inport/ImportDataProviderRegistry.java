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

package org.jreserve.gui.data.inport;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
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
public class ImportDataProviderRegistry extends LayerRegistrationLoader<ImportDataProviderAdapter> {
    
    private static ImportDataProviderRegistry INSTANCE;
    
    public synchronized static List<ImportDataProviderAdapter> getAdapters() {
        if(INSTANCE == null)
            INSTANCE = new ImportDataProviderRegistry();
        return INSTANCE.getValues();
    }
    
    private final static Logger logger = Logger.getLogger(ImportDataProviderRegistry.class.getName());

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return ImportDataProviderRegistrationProcessor.LAYER_PATH;
    }

    @Override
    protected ImportDataProviderAdapter getValue(FileObject file) throws Exception {
        String name = getDisplayName(file);
        Icon icon = getIcon(file);
        ImportDataProvider provider = super.getInstance(file, ImportDataProvider.class);
        String id = getId(file, provider);
        boolean dsRequired = isDSRequired(file);
        return new ImportDataProviderAdapter(id, name, dsRequired, icon, provider);
    }
    
    private String getId(FileObject file, ImportDataProvider provider) {
        return AnnotationUtils.stringAttribute(
                ImportDataProviderRegistrationProcessor.ID, 
                file, provider.getClass().getName());
    }
    
    private String getDisplayName(FileObject file) {
        return AnnotationUtils.stringAttribute(
                ImportDataProviderRegistrationProcessor.DISPLAY_NAME, 
                file, null);
    }
    
    private Icon getIcon(FileObject file) {
        String iconBase = AnnotationUtils.stringAttribute(
                ImportDataProviderRegistrationProcessor.ICON_BASE, file, "");
        return iconBase.length() > 0?
                ImageUtilities.loadImageIcon(iconBase, false) :
                EmptyIcon.EMPTY_16;
    }
    
    private boolean isDSRequired(FileObject file) {
        return AnnotationUtils.booleanAttribute(
                ImportDataProviderRegistrationProcessor.REQUIRES_DATA_SOURCE, 
                file, true);
    }
}
