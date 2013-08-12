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

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.jreserve.gui.data.spi.DataSourceWizard;
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
@SupportedAnnotationTypes("org.jreserve.gui.data.spi.DataSourceWizard.Registration")
public class DataSourceWizardRegistrationProcessor extends AbstractRegistrationProcessor<DataSourceWizard.Registration, DataSourceWizard> {
    final static String LAYER_PATH = "JReserve/DataStore/DataSourceWizards/";
    final static String DISPLAY_NAME = "displayName";
    final static String ICON_BASE = "iconBase";

    @Override
    protected Class<DataSourceWizard.Registration> getAnnotationClass() {
        return DataSourceWizard.Registration.class;
    }

    @Override
    protected Class<DataSourceWizard> getInterfaceClass() {
        return DataSourceWizard.class;
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return LAYER_PATH;
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        DataSourceWizard.Registration an = getAnnotation(element);
        file.position(an.position());
        file.bundlevalue(DISPLAY_NAME, an.displayName(), an, "displayName");
        
        if(an.iconBase().length() > 0) {
            layer(element).validateResource(an.iconBase(), element, an, "iconBase", true);
            file.stringvalue(ICON_BASE, an.iconBase());
        }
    }
}
