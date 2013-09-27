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

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.jreserve.gui.misc.annotations.AbstractRegistrationProcessor;
import org.jreserve.gui.plot.ColorGenerator;
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
@SupportedAnnotationTypes("org.jreserve.gui.plot.ColorGenerator.Registration")
public class ColorGeneratorRegistrationProcessor extends AbstractRegistrationProcessor<ColorGenerator.Registration, ColorGenerator.Factory> {
    
    final static String PATH = "JReserve/ColorGenerators/"; //NOI18
    final static String ID = "id"; //NOI18
    final static String DISPLAY_NAME = "displayName"; //NOI18
    
    public ColorGeneratorRegistrationProcessor() {
        super(ColorGenerator.Registration.class, ColorGenerator.Factory.class);
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return PATH;
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        ColorGenerator.Registration an = getAnnotation(element);
        
        String id = an.id();
        if(id == null || id.length()==0)
            throw new LayerGenerationException("ID not set!", element, processingEnv, an, "id"); //NOI18
        file.stringvalue(ID, id);
        
        String displayName = an.displayName();
        if(displayName == null || displayName.length() == 0)
            throw new LayerGenerationException("DisplayName not set!", element, processingEnv, an, "displayName"); //NOI18
        file.bundlevalue(DISPLAY_NAME, displayName);
        
        file.position(an.position());
    }
}
