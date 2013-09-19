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

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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

    final static String PATH = "Editors/%s/ExpandableView/";
    private final static String ERR_NO_MIME = 
        "Mime type not defined!";
    public final static String DISPLAY_NAME = "displayName";
    public final static String ICON = "iconBase";
    public final static String PREFFERED_ID = "prefferedID";
    public final static String CLASS = "class";
    public final static String BACKGROUND = "background";
    public final static String FOREGROUND = "foreground";

    public ExpandableElementRegistrationProcessor() {
        super(ExpandableElement.Registration.class, ExpandableElement.class);
        super.addConstructorParams(new Class[]{Lookup.class});
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        return String.format(PATH, getMimeType(element));
    }
    
    private String getMimeType(Element element) throws LayerGenerationException {
        String mime = getAnnotation(element).mimeType();
        if(mime!=null && mime.trim().length()>0)
            return mime;
        throw new LayerGenerationException(ERR_NO_MIME, element);
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        ExpandableElement.Registration an = getAnnotation(element);
        
        file.stringvalue(CLASS, getClassName(element));
        
        file.position(an.position());
        
        String str  = an.displayName();
        if(str==null || str.length()==0)
            throw new LayerGenerationException("Display name is not set!", element);
        file.bundlevalue(DISPLAY_NAME, an.displayName(), an, "displayName");
        
        str = an.prefferedID();
        if(str==null || str.length()==0)
            throw new LayerGenerationException("Preffered id is not set!", element);
        file.stringvalue(PREFFERED_ID, str);
        
        str = an.iconBase();
        if(str != null && str.length() > 0)
            file.stringvalue(ICON, str);
        
        str = an.background();
        if(str.startsWith("#")) {
            file.bundlevalue(BACKGROUND, str);
        } else {
            if(validHex(str)) {
                file.stringvalue(BACKGROUND, str);
            } else {
                throw new LayerGenerationException("Invalid background!", element);
            }
        }
        
        str = an.foreground();
        if(str.startsWith("#")) {
            file.bundlevalue(FOREGROUND, str);
        } else {
            if(validHex(str)) {
                file.stringvalue(FOREGROUND, str);
            } else {
                throw new LayerGenerationException("Invalid foreground!", element);
            }
        }
    }
    
    private String getClassName(Element e) {
        TypeElement te = (TypeElement) e;
        return processingEnv.getElementUtils().getBinaryName(te).toString();
    }
    
    private boolean validHex(String color) {
        return color != null && 
               color.length() == 6 && 
               color.matches("[0-9A-Fa-f]+");
    }
}
