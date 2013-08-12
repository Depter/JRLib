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

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.misc.annotations.AbstractRegistrationProcessor;
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
@SupportedAnnotationTypes("org.jreserve.gui.excel.template.ExcelTemplateManager.Registration")
public class ExcelTemplateManagerRegistrationProcessor extends AbstractRegistrationProcessor<ExcelTemplateManager.Registration, ExcelTemplateManager> {
    final static String LAYER_PATH = "JReserve/Excel/TemplateManagers";
    final static String PROP_DISPLAY_NAME = "displayName";
    final static String PROP_ICON = "iconBase";
    
    @Override
    protected Class<ExcelTemplateManager.Registration> getAnnotationClass() {
        return ExcelTemplateManager.Registration.class;
    }

    @Override
    protected Class<ExcelTemplateManager> getInterfaceClass() {
        return ExcelTemplateManager.class;
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return LAYER_PATH;
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        ExcelTemplateManager.Registration an = getAnnotation(element);
        int position = an.position();
        file.position(position);
        
        String name = an.displayName();
        if(name == null || name.length()==0)
            throw new LayerGenerationException("Display name not set!", element);
        file.bundlevalue(POSITION, name);
        
        String icon = an.iconBase();
        if(icon != null && icon.length()>0) {
            layer(element).validateResource(icon, element, an, "iconBase", true);
            file.stringvalue(PROP_ICON, icon);
        }
    }
}
