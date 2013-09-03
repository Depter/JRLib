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
package org.jreserve.gui.project.api.util;

import javax.lang.model.element.Element;
import org.jreserve.gui.misc.annotations.AbstractRegistrationProcessor;
import org.jreserve.gui.project.api.DataObjectProvider;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGenerationException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProjectFolderProviderRegistrationProcessor extends AbstractRegistrationProcessor<DataObjectProvider.Registration, DataObjectProvider> {
    final static String LAYER_PATH = "Projects/%s/DataObjectProviders/";
    final static String PROP_ID = "id";
    final static String PROP_DISPLAY_NAME = "displayName";
    final static String PROP_ICON_BASE = "iconBase";
    
    private final static String ERR_NO_PROJECT_NAME = 
        "Project type is invalid!";
    
    public ProjectFolderProviderRegistrationProcessor() {
        super(DataObjectProvider.Registration.class, DataObjectProvider.class);
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return String.format(LAYER_PATH, getProjectName(element));
    }
    
    private String getProjectName(Element element) throws LayerGenerationException {
        DataObjectProvider.Registration annotation = getAnnotation(element);
        String project = annotation.projectType();
        if(project == null || project.trim().length()==0)
            throw new LayerGenerationException(ERR_NO_PROJECT_NAME, element);
        return project;
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        DataObjectProvider.Registration an = getAnnotation(element);
        
        String id = an.id();
        if(id == null || id.length()==0)
            throw new LayerGenerationException("id not set!", element, processingEnv, an, "id");
        file.stringvalue(PROP_ID, id);
        
        String name = an.displayName();
        if(name==null || name.length()==0)
            throw new LayerGenerationException("displayName not set!", element, processingEnv, an, "displayName");
        file.bundlevalue(PROP_DISPLAY_NAME, name, an, "displayName");
        
        String icon = an.iconBase();
        if(icon != null && icon.length()>0)
            file.stringvalue(PROP_ICON_BASE, icon);
        file.position(an.position());
    }
}
