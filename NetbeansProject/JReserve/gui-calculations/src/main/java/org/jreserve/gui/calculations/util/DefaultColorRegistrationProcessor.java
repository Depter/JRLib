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

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.jreserve.gui.calculations.api.DefaultColor;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes({
    "org.jreserve.gui.calculations.api.DefaultColor.Registration",
    "org.jreserve.gui.calculations.api.DefaultColor.Registrations"
})
public class DefaultColorRegistrationProcessor extends LayerGeneratingProcessor {
    
    final static String FOLDER = "JReserve/CalculationColors/";
    final static String ID = "id";
    final static String DISPLAY_NAME = "displayName";
    final static String COLOR = "color";
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment re) throws LayerGenerationException {
        if(re.processingOver())
            return false;
        processRoundEnvironment(re);
        return true;
    }

    private void processRoundEnvironment(RoundEnvironment re) throws LayerGenerationException {
        for(Element e : getElements(re, DefaultColor.Registrations.class)) {
            DefaultColor.Registrations a = e.getAnnotation(DefaultColor.Registrations.class);
            for(DefaultColor.Registration r : a.value())
                processElement(e, r);
        }
        
        for (Element e : getElements(re, DefaultColor.Registration.class)) {
            DefaultColor.Registration a = e.getAnnotation(DefaultColor.Registration.class);
            processElement(e, a);
        }
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv, Class<? extends Annotation> clazz) {
        return roundEnv.getElementsAnnotatedWith(clazz);
    }

    private void processElement(Element element, DefaultColor.Registration a) throws LayerGenerationException {
        String id = a.id();
        if(id == null || id.length() == 0) {
            String msg = "Id is not set!";
            throw new LayerGenerationException(msg, element, processingEnv, a, "id");
        }
        
        String name = getFileBaseName(id);
        LayerBuilder.File file = layer(element).file(FOLDER + name);
        
        file.stringvalue(ID, id);
        file.bundlevalue(DISPLAY_NAME, a.displayName(), a, "displayName");
        
        String color = a.color();
        if(color.startsWith("#")) {
            file.bundlevalue(COLOR, color);
        } else {
            file.stringvalue(COLOR, color);
        }
        
        file.position(a.position());
        
        file.write();
    }
    
    private String getFileBaseName(String id) {
        String fileBaseName = id.replace('.', '-');
        return fileBaseName+".color";
    }
}
