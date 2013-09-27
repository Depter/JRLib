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

import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.jreserve.gui.plot.ColorGenerator.ListRegistration;
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
@SupportedAnnotationTypes("org.jreserve.gui.plot.ColorGenerator.ListRegistration")
public class ListColorGeneratorRegistrationProcessor extends LayerGeneratingProcessor {
    
    private final static String HEX_REGEXP = "[0-9A-Fa-f]+";
    final static String COLORS = "colors";
    final static char COLOR_SEPARATOR = ';';
    final static String EXTENSION = "colors";
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if(roundEnv.processingOver())
            return false;
        processRoundEnvironment(roundEnv);
        return true;
    }

    private void processRoundEnvironment(RoundEnvironment re) throws LayerGenerationException {
        for (Element e : getElements(re))
            processElement(e);
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(getAnnotationClass());
    }
    
    private Class<ListRegistration> getAnnotationClass() {
        return ListRegistration.class;
    }
    
    private void processElement(Element element) throws LayerGenerationException {
        ListRegistration an = element.getAnnotation(getAnnotationClass());
        
        String id = an.id();
        if(id == null || id.length() == 0)
            throw new LayerGenerationException("ID not set!", element, processingEnv, an, "id"); //NOI18
        
        String name = getFileLocation(id);
        LayerBuilder.File file = layer(element).file(name);
        
        file.stringvalue(ColorGeneratorRegistrationProcessor.ID, id);
        
        String displayName = an.displayName();
        if(displayName == null || displayName.length() == 0)
            throw new LayerGenerationException("DisplayName not set!", element, processingEnv, an, "displayName"); //NOI18
        file.bundlevalue(ColorGeneratorRegistrationProcessor.DISPLAY_NAME, displayName);
        
        file.position(an.position());
        
        String[] colors = an.colors();
        if(colors==null || colors.length==0)
            throw new LayerGenerationException("Colors not set!", element, processingEnv, an, "colors"); //NOI18
        
        StringBuilder sb = new StringBuilder();
        for(String color : colors) {
            if(ColorUtil.isHexColor(color)) {
                if(sb.length() > 0) sb.append(COLOR_SEPARATOR);
                sb.append(color);
            } else {
                throw new LayerGenerationException("Color '"+color+"' is not a hexadecimal color!", element, processingEnv, an, "colors"); //NOI18
            }
        }
        file.stringvalue(COLORS, sb.toString());
        
        file.write();
    }
    
    private String getFileLocation(String id) {
        id = id.replace('.', '-');
        id += "."+EXTENSION;
        return ColorGeneratorRegistrationProcessor.PATH + id;
    }
}
