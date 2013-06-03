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
package org.jreserve.gui.misc.expandable.registration;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import org.jreserve.gui.misc.annotations.AbstractRegistrationProcessor;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.jreserve.gui.misc.expandable.ExpandableElement.Registration")
public class ExpandableElementRegistrationProcessor extends AbstractRegistrationProcessor<ExpandableElement.Registration, ExpandableElement> {

    private final static Class<ExpandableElement.Registration> ANNOTATION = ExpandableElement.Registration.class;
    private final static Class<ExpandableElement> INTERFACE = ExpandableElement.class;
    private final static String ERR_NO_MIME = 
        "Class '%s' annotated with '%s', but it does not define a mime type!";
    private final static String ERR_NO_NAME = 
        "Class '%s' annotated with '%s', but it does not define a display name!";
    
    final static String PATH = "Editors/%s/ExpandableView/";
    public final static String NAME = "name";
    public final static String ICON = "iconBase";
    public final static String PREFFERED_ID = "prefferedID";
    public final static String CLASS = "class";
    
    @Override
    protected Class<ExpandableElement.Registration> getAnnotationClass() {
        return ANNOTATION;
    }

    @Override
    protected Class<ExpandableElement> getInterfaceClass() {
        return INTERFACE;
    }
    
    @Override
    protected boolean isConstructor(ExecutableElement element) {
        if(!isPublic(element))
            return false;
        List<? extends VariableElement> parameters = element.getParameters();
        return parameters.isEmpty() || 
               (parameters.size()==1 && isLookup(parameters.get(0)));
    }
    
    private boolean isLookup(VariableElement param) {
        Types types = processingEnv.getTypeUtils();
        TypeMirror lookup = getLookupMirror();
        return types.isAssignable(param.asType(), lookup);
    }
    
    private TypeMirror getLookupMirror() {
        Elements utils = processingEnv.getElementUtils();
        String name = Lookup.class.getName();
        return utils.getTypeElement(name).asType();
    }

    @Override
    protected String getFileLocation(TypeElement element) throws LayerGenerationException {
        String path = String.format(PATH, getMimeType(element));
        return path + getFileName(element);
    }
    
    private String getMimeType(TypeElement element) throws LayerGenerationException {
        String mime = getAnnotation(element).mimeType();
        if(mime!=null && mime.trim().length()>0)
            return mime;
        String msg = String.format(ERR_NO_MIME, getClassName(element), annotationName(), interfaceName());
        throw new LayerGenerationException(msg);
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        file.intvalue(POSITION, getAnnotation(element).position());
        file.stringvalue(CLASS, getClassName(element));
        initName(file, element);
        initIcon(file, element);
        initPrefferedID(file, element);
    }
    
    private void initName(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        String name = getDisplayName(element);
        if(name.startsWith("#")) {
            file.bundlevalue(NAME, name);
        } else {
            file.stringvalue(NAME, name);
        }
    }
    
    private String getDisplayName(TypeElement element) throws LayerGenerationException {
        String name = getAnnotation(element).displayName();
        if(name!=null && name.trim().length()>0)
            return name;
        String msg = String.format(ERR_NO_NAME, getClassName(element), annotationName(), interfaceName());
        throw new LayerGenerationException(msg);
    }
    
    private void initIcon(LayerBuilder.File file, TypeElement element) throws LayerGenerationException {
        String icon = getAnnotation(element).iconBase();
        if(icon != null && icon.trim().length()>0)
            file.stringvalue(ICON, icon);
    }
    
    private void initPrefferedID(LayerBuilder.File file, TypeElement element) {
        String id = getAnnotation(element).prefferedID();
        if(id == null || id.trim().length()==0)
            id = getClassName(element);
        file.stringvalue(PREFFERED_ID, id);
    }
    
}
