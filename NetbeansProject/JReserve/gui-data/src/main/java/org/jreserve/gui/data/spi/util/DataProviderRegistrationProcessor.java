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
package org.jreserve.gui.data.spi.util;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.jreserve.gui.data.spi.DataProvider;
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
@SupportedAnnotationTypes("org.jreserve.gui.data.spi.DataProvider.FactoryRegistration")
public class DataProviderRegistrationProcessor extends AbstractRegistrationProcessor<DataProvider.FactoryRegistration, DataProvider.Factory> {

    final static String LAYER_PATH = "JReserve/DataStore/DataProviders/";
    final static String ID = "id";
    
    @Override
    protected Class<DataProvider.FactoryRegistration> getAnnotationClass() {
        return DataProvider.FactoryRegistration.class;
    }

    @Override
    protected Class<DataProvider.Factory> getInterfaceClass() {
        return DataProvider.Factory.class;
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return LAYER_PATH;
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        DataProvider.FactoryRegistration an = getAnnotation(element);
        
        String id = an.id();
        if(id == null || id.length()==0)
            throw new LayerGenerationException("ID not set!", element);
        file.stringvalue(ID, id);
    }
}
