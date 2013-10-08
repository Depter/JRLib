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
import org.jreserve.gui.calculations.api.smoothing.Smoothable;
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
@SupportedAnnotationTypes("org.jreserve.gui.calculations.api.smoothing.Smoothable.Registration")
public class SmoothableRegistrationProcessor extends AbstractRegistrationProcessor<Smoothable.Registration, Smoothable> {
    final static String LAYER_PATH = "JReserve/Smoothable/%s/";
    final static String ICON_BASE = "iconBase";
    final static String DISPLAY_NAME = "displayName";
    final static String SEPARATOR_BEFORE = "separatorBefore";
    final static String SEPARATOR_AFTER = "separatorAfter";

    public SmoothableRegistrationProcessor() {
        super(Smoothable.Registration.class, Smoothable.class);
    }

    @Override
    protected String getFolder(Element element) throws LayerGenerationException {
        Smoothable.Registration an = getAnnotation(element);
        String category = an.category();
        if(category == null || category.length() == 0)
            throw new LayerGenerationException("Category not set!", element, processingEnv, an, "category");
        return String.format(LAYER_PATH, category);
    }
    
    @Override
    protected void initAttributes(LayerBuilder.File file, Element element) throws LayerGenerationException {
        Smoothable.Registration an = getAnnotation(element);
        
        String icon = an.iconBase();
        if(icon != null && icon.length() > 0)
            file.stringvalue(ICON_BASE, icon);
        
        String name = an.displayName();
        if(name == null || name.length() == 0)
            throw new LayerGenerationException("DisplayName not set!", element, processingEnv, an, "displayName");
        file.bundlevalue(DISPLAY_NAME, name, an, "displayName");
        
        file.boolvalue(SEPARATOR_BEFORE, an.separatorBefore());
        file.boolvalue(SEPARATOR_AFTER, an.separatorAfter());
        
        file.position(an.position());
    }
}
