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
package org.jreserve.gui.misc.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRegistrationProcessor<A extends Annotation, I> extends LayerGeneratingProcessor {
    private final static String ERR_NOT_IMPLEMENTS_INTERFACE = 
        "Class '%s' annotated with '%s', but does not implements interface '%s'!";
    private final static String ERR_NO_COSNTRUCTOR = 
        "Class '%s' annotated with '%s', but it does not have a no arg public constructor!";
    public final static String POSITION = "position";
    
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
        return roundEnv.getElementsAnnotatedWith(getAnnotationClass());
    }
    
    protected abstract Class<A> getAnnotationClass();
    
    protected void processElement(TypeElement element) throws LayerGenerationException {
        checkImplementsInterface(element);
        checkConstructor(element);
        addClass(element);
    }
    
    private void checkImplementsInterface(TypeElement element) throws LayerGenerationException {
        TypeMirror mirror = getInterfaceMirror();
        if(processingEnv.getTypeUtils().isAssignable(element.asType(), mirror))
            return;
        String msg = String.format(getNoInterfaceMsgFormat(), getClassName(element), annotationName(), interfaceName());
        throw createException(msg, element);
    }
    
    private TypeMirror getInterfaceMirror() {
        Elements utils = processingEnv.getElementUtils();
        String name = getInterfaceClass().getName();
        return utils.getTypeElement(name).asType();
    }
    
    protected abstract Class<I> getInterfaceClass();
    
    protected String getNoInterfaceMsgFormat() {
        return ERR_NOT_IMPLEMENTS_INTERFACE;
    }
    
    protected String getClassName(TypeElement element) {
        return element.getQualifiedName().toString();
    }
    
    protected String annotationName() {
        return getAnnotationClass().getName();
    }
    
    protected String interfaceName() {
        return getInterfaceClass().getName();
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
    
    protected boolean isConstructor(ExecutableElement element) {
        return isPublic(element) && element.getParameters().isEmpty();
    }
    
    protected boolean isPublic(ExecutableElement element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }
    
    private LayerGenerationException noAppropriateConstructor(TypeElement element) {
        String msg = String.format(ERR_NO_COSNTRUCTOR, 
                getClassName(element), annotationName());
        return createException(msg, element);
    }
    
    protected LayerGenerationException createException(String msg, TypeElement element) {
        return new LayerGenerationException(msg, element, processingEnv, getAnnotation(element));
    }
    
    protected String getNoConstructorMsgFormat() {
        return ERR_NO_COSNTRUCTOR;
    }
    
    protected void addClass(TypeElement element) throws LayerGenerationException {
        String name = getFileLocation(element);
        LayerBuilder.File file = layer(element).file(name);
        initAttributes(file, element);
        file.write();
    }
    
    protected abstract String getFileLocation(TypeElement element) throws LayerGenerationException;
    
    protected String getFileName(TypeElement element) {
        return getClassName(element).replaceAll("\\.", "-")+".instance";
    }
    
    protected void initAttributes(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
    }
    
    protected A getAnnotation(TypeElement element) {
        return element.getAnnotation(getAnnotationClass());
    }
}
