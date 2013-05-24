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
package org.jreserve.grscript.gui.script.registration;

import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.jreserve.grscript.gui.script.functions.FunctionProviderAdapter;
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
@SupportedAnnotationTypes("org.jreserve.grscript.gui.script.functions.FunctionProviderAdapter.Registration")
public class FunctionProviderAdapterRegistrationProcessor extends LayerGeneratingProcessor {
    public final static String ENTITY_DIRECTORY = "Scripts/FunctionProviders/";
    public final static String POSITION = "priority";
    
    private final static String ERR_NOT_IMPLEMENTS_INTERFACE = 
        "Class '%s' annotated with '%s', but does not implements interface '%s'!";
    private final static String ERR_NO_COSNTRUCTOR = 
        "Class '%s' annotated with '%s', but it does not have a no arg public constructor!";
    
    private final static Class<FunctionProviderAdapter.Registration> ANNOTATION = FunctionProviderAdapter.Registration.class;
    private final static Class<?> INTERFACE = FunctionProviderAdapter.class;
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> set, RoundEnvironment re) throws LayerGenerationException {
        if(re.processingOver())
            return false;
        processRoundEnvironment(re);
        return true;
    }

    private void processRoundEnvironment(RoundEnvironment re) throws LayerGenerationException {
        for (Element e : getElements(re))
            processElement((TypeElement) e);
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(ANNOTATION);
    }
    
    private void processElement(TypeElement element) throws LayerGenerationException {
        checkImplementsInterface(element);
        checkConstructor(element);
        addClass(element);
    }
    
    private void checkImplementsInterface(TypeElement element) throws LayerGenerationException {
        TypeMirror mirror = getInterfaceMirror();
        if(processingEnv.getTypeUtils().isAssignable(element.asType(), mirror))
            return;
        String msg = String.format(ERR_NOT_IMPLEMENTS_INTERFACE, getClassName(element), ANNOTATION.getName(), INTERFACE.getName());
        throw new LayerGenerationException(msg);
    }
    
    private TypeMirror getInterfaceMirror() {
        Elements utils = processingEnv.getElementUtils();
        String name = INTERFACE.getName();
        return utils.getTypeElement(name).asType();
    }
    
    private String getClassName(TypeElement element) {
        return element.getQualifiedName().toString();
    }
    
    private void checkConstructor(TypeElement element) throws LayerGenerationException {
        for(Element e : element.getEnclosedElements())
            if(isConstructor(e))
                return;
        throw noAppropriateConstructor(element);
    }
    
    private boolean isConstructor(Element element) {
        if(ElementKind.CONSTRUCTOR != element.getKind())
            return false;
        ExecutableElement constructor = (ExecutableElement) element;
        return isConstructor(constructor);
    }
    
    private boolean isConstructor(ExecutableElement element) {
        if(!element.getParameters().isEmpty())
            return false;
        return element.getModifiers().contains(Modifier.PUBLIC);
    }
    
    private LayerGenerationException noAppropriateConstructor(TypeElement element) {
        String msg = String.format(ERR_NO_COSNTRUCTOR, 
                getClassName(element), ANNOTATION.getName());
        return new LayerGenerationException(msg);
    }
    
    private void addClass(TypeElement element) throws LayerGenerationException {
        String name = getFileLocation(element);
        LayerBuilder.File file = layer(element).file(name);
        file.intvalue(POSITION, getPriority(element));
        file.write();
    }
    
    private String getFileLocation(TypeElement element) {
        return ENTITY_DIRECTORY + getPath(element) + getFileName(element);
    }
    
    private String getPath(TypeElement element) {
        String path = element.getAnnotation(ANNOTATION).path();
        if(path == null || path.length()==0) 
            return "";
        return path.endsWith("/")? path : path+"/";
    }
    
    private String getFileName(TypeElement element) {
        return getClassName(element).replaceAll("\\.", "-")+".instance";
    }
    
    private int getPriority(TypeElement element) throws LayerGenerationException {
        return element.getAnnotation(ANNOTATION).position();
    }
}
