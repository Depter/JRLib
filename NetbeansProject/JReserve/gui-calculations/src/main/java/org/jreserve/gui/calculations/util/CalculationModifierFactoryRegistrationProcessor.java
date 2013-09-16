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
package org.jreserve.gui.calculations.util;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.jreserve.gui.calculations.api.CalculationModifierFactory;
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
@SupportedAnnotationTypes("org.jreserve.gui.calculations.api.CalculationModifierFactory.Registration")
public class CalculationModifierFactoryRegistrationProcessor extends AbstractRegistrationProcessor<CalculationModifierFactory.Registration, CalculationModifierFactory> {

    final static String LAYER_PATH = "JReserve/CalculationModifierFactory/%s/";
    final static String ROOT_NAME = "rootName";

    public CalculationModifierFactoryRegistrationProcessor() {
        super(CalculationModifierFactory.Registration.class, CalculationModifierFactory.class);
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        CalculationModifierFactory.Registration an = getAnnotation(element);
        String category = an.category();
        if(category == null || category.length() == 0)
            throw new LayerGenerationException("Category not set!", element, processingEnv, an, "category");
        return String.format(LAYER_PATH, category);
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        CalculationModifierFactory.Registration an = getAnnotation(element);
        
        String rootName = an.rootName();
        if(rootName==null || rootName.length() == 0)
            throw new LayerGenerationException("RootName not set!", element, processingEnv, an, "rootName");
        file.bundlevalue(ROOT_NAME, rootName);
        
        file.position(an.position());
    }
}
