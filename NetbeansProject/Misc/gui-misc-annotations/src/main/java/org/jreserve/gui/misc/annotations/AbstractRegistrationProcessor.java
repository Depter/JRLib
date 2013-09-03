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
import java.util.List;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractRegistrationProcessor<A extends Annotation, I> extends LayerGeneratingProcessor {

    public final static String POSITION = "position";
    private final Class<A> annotationClass;
    private final Class<I> interfaceClass;
    
    protected AbstractRegistrationProcessor() {
        this(null, null);
    }
    
    protected AbstractRegistrationProcessor(Class<A> annotationClass, Class<I> interfaceClass) {
        this.annotationClass = annotationClass;
        this.interfaceClass = interfaceClass;
    }
    
    @Override
    protected boolean handleProcess(Set<? extends TypeElement> set, RoundEnvironment re) throws LayerGenerationException {
        if(re.processingOver())
            return false;
        processRoundEnvironment(re);
        return true;
    }

    private void processRoundEnvironment(RoundEnvironment re) throws LayerGenerationException {
        for (Element e : getElements(re))
            processElement(e);
    }
    
    private Set<? extends Element> getElements(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(getAnnotationClass());
    }
    
    protected Class<A> getAnnotationClass() {
        return annotationClass;
    }

    protected void processElement(Element element) throws LayerGenerationException {
        String[] instanceDefinition = findDefinition(element);
        String className = instanceDefinition[0];
        String methodName = instanceDefinition[1];
        createLayerRegistration(element, className, methodName);
    }

    private String[] findDefinition(Element e) throws LayerGenerationException {
        A an = getAnnotation(e);
        final TypeMirror interfaceMirror = getInterfaceMirror();
        
        if (e.getKind() == ElementKind.CLASS) {
            TypeElement clazz = (TypeElement) e;
            if (!isAssignable(clazz.asType(), interfaceMirror))
                throw new LayerGenerationException("Not assignable to " + interfaceMirror, e, processingEnv, an);
            
            if (!clazz.getModifiers().contains(Modifier.PUBLIC))
                throw new LayerGenerationException("Class must be public", e, processingEnv, an);
            
            
            if(!hasGoodConstructor(clazz))
                throw new LayerGenerationException("No appropriate constructor!", e, processingEnv, an);
                
            return new String[] {processingEnv.getElementUtils().getBinaryName(clazz).toString(), null};
        } else {
            ExecutableElement method = (ExecutableElement) e;
            if(!isAssignable(method.getReturnType(), interfaceMirror))
                throw new LayerGenerationException("Not assignable to " + interfaceMirror, e, processingEnv, an);
            if(!method.getModifiers().contains(Modifier.PUBLIC))
                throw new LayerGenerationException("Method must be public", e, processingEnv, an);
            if(!method.getModifiers().contains(Modifier.STATIC))
                throw new LayerGenerationException("Method must be static", e, processingEnv, an);
            if(!method.getEnclosingElement().getModifiers().contains(Modifier.PUBLIC))
                throw new LayerGenerationException("Class must be public", e, processingEnv, an);
            if(!goodParameters(method.getParameters()))
                throw new LayerGenerationException("No appropriate parameters!", e, processingEnv, an);
                
            return new String[] {
                processingEnv.getElementUtils().getBinaryName((TypeElement) method.getEnclosingElement()).toString(),
                method.getSimpleName().toString()};
        }
    }
    
    protected A getAnnotation(Element element) {
        return element.getAnnotation(getAnnotationClass());
    }
    
    private TypeMirror getInterfaceMirror() {
        Class clazz = getInterfaceClass();
        TypeElement te = getElementFor(clazz);
        return te.asType();
    }
    
    private TypeElement getElementFor(Class clazz) {
        String name = clazz.getCanonicalName();
        return processingEnv.getElementUtils().getTypeElement(name);
    }
    
    protected Class<I> getInterfaceClass() {
        return interfaceClass;
    }
    
    private boolean isAssignable(TypeMirror from, TypeMirror to) {
        return processingEnv.getTypeUtils().isAssignable(from, to);
    }
    
    private boolean hasGoodConstructor(TypeElement clazz ) {
        for(ExecutableElement constructor : ElementFilter.constructorsIn(clazz.getEnclosedElements()))
            if(goodConstructor(constructor))
                return true;
        return false;
    }
    
    private boolean goodConstructor(ExecutableElement constructor) {
        if(!constructor.getModifiers().contains(Modifier.PUBLIC))
            return false;
        return goodParameters(constructor.getParameters());
    }
    
    private boolean goodParameters(List<? extends VariableElement> params) {
        return params.isEmpty() ||
               (params.size()==1 && isFileObjectType(params.get(0)));
    }
    
    private boolean isFileObjectType(VariableElement e) {
        TypeElement fo = getElementFor(FileObject.class);
        return isAssignable(e.asType(), fo.asType());
    }
    
    protected void createLayerRegistration(Element element, String className, String methodName) throws LayerGenerationException {
        String name = getFileLocation(element, className, methodName);
        LayerBuilder.File file = layer(element).file(name);
        initAttributes(file, element);
        initInstanceAttributes(file, element, className, methodName);
        file.write();
    }
    
    protected String getFileLocation(Element element, String className, String methodName) throws LayerGenerationException {
        String name = getFileBaseName(element, className, methodName);
        String folder = getFolder(element);
        if(folder == null || folder.length()==0)
            return name;
        
        if('/' != folder.charAt(folder.length()-1))
            folder += "/";
        return folder + name;
    }
    
    protected String getFileBaseName(Element element, String className, String methodName) {
        String fileBaseName = className.replace('.', '-');
        if(methodName != null)
            fileBaseName += "-" + methodName;
        return fileBaseName+".instance";
    }
    
    protected abstract String getFolder(Element element) throws LayerGenerationException;
    
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
    }
    
    protected void initInstanceAttributes(LayerBuilder.File file, Element element, String className, String methodName) throws LayerGenerationException {
        if(methodName == null)
            file.instanceAttribute("instanceCreate", getInterfaceClass());
        else
            file.instanceAttribute("instanceCreate", getInterfaceClass(), getAnnotation(element), methodName);
        file.stringvalue("instanceClass", getInterfaceClass().getName());
    }
}
