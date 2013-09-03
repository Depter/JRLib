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

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.jreserve.gui.misc.annotations.AbstractRegistrationProcessor;
import org.jreserve.gui.project.api.ProjectFileProvider;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.jreserve.gui.project.api.ProjectFileProvider.Registration")
public class ProjectFileProviderRegistrationProcessor extends AbstractRegistrationProcessor<ProjectFileProvider.Registration, ProjectFileProvider> {
    final static String LAYER_PATH = "Projects/%s/CreateFileProviders/";

    private final static String ERR_NO_PROJECT_NAME = 
        "Project name is invalid!";
    

    @Override
    protected Class<ProjectFileProvider.Registration> getAnnotationClass() {
        return ProjectFileProvider.Registration.class;
    }

    @Override
    protected Class<ProjectFileProvider> getInterfaceClass() {
        return ProjectFileProvider.class;
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return String.format(LAYER_PATH, getProjectName(element));
    }
    
    private String getProjectName(Element element) throws LayerGenerationException {
        ProjectFileProvider.Registration annotation = getAnnotation(element);
        String project = annotation.project();
        if(project == null || project.trim().length()==0)
            throw new LayerGenerationException(ERR_NO_PROJECT_NAME, element);
        return project;
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        int position = getAnnotation(element).position();
        file.position(position);
    }
}
