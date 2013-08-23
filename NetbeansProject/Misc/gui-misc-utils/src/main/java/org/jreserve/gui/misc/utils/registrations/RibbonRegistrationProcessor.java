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
package org.jreserve.gui.misc.utils.registrations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Completions;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.swing.JSeparator;
import org.jreserve.gui.misc.utils.actions.RibbonRegistration;
import org.jreserve.gui.misc.utils.actions.RibbonRegistrations;
import org.openide.awt.ActionID;
import org.openide.filesystems.annotations.LayerBuilder.File;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RibbonRegistrationProcessor extends LayerGeneratingProcessor {
    private static final String[] DEFAULT_COMPLETIONS = { 
        "AppMenu", "AppMenuFooter", "TaskBar", "HelpButton", "TaskPanes" }; // NOI18N
    private Processor COMPLETIONS;
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> hash = new HashSet<String>();
        hash.add(RibbonRegistration.class.getCanonicalName());
        hash.add(RibbonRegistrations.class.getCanonicalName());
        return hash;
    }
    
    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        if (annotation.getAnnotationType().asElement().getSimpleName().toString().contains(RibbonRegistration.class.getSimpleName())) {
            if (member.getSimpleName().contentEquals("path")) { // NOI18N
                if (userText == null)
                    userText = "";
                
                if (userText.startsWith("\""))
                    userText = userText.substring(1);
                
                Set<Completion> res = new HashSet<Completion>();
                for (String c : DEFAULT_COMPLETIONS)
                    if (c.startsWith(userText))
                        res.add(Completions.of("\"" + c + '/', NbBundle.getMessage(RibbonRegistrationProcessor.class, "HINT_" + c)));
                
                if (!res.isEmpty())
                    return res;
                
                if (COMPLETIONS == null) {
                    String pathCompletions = System.getProperty(RibbonRegistration.class.getName() + ".completion");
                    if (pathCompletions != null) {
                        ClassLoader l = Lookup.getDefault().lookup(ClassLoader.class);
                        if (l == null) {
                            l = Thread.currentThread().getContextClassLoader();
                        }
                        if (l == null) {
                            l = RibbonRegistration.class.getClassLoader();
                        }
                        try {
                            COMPLETIONS = (Processor)Class.forName(pathCompletions, true, l).newInstance();
                        } catch (Exception ex) {
                            Exceptions.printStackTrace(ex);
                            // no completions, OK
                            COMPLETIONS = this;
                        }
                    } else {
                        return res;
                    }
                }
                if (COMPLETIONS != null && COMPLETIONS != this) {
                    COMPLETIONS.init(processingEnv);
                    for (Completion completion : COMPLETIONS.getCompletions(element, annotation, member, userText)) {
                        res.add(completion);
                    }
                }
                return res;
            }
        }
        return Collections.emptyList();
    }

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        for (Element e : roundEnv.getElementsAnnotatedWith(ActionID.class)) {
            ActionID aid = e.getAnnotation(ActionID.class);
            RibbonRegistration rReg = e.getAnnotation(RibbonRegistration.class);
            RibbonRegistrations rRegs = e.getAnnotation(RibbonRegistrations.class);
            if(rReg==null && rRegs==null)
                continue;
            if (aid == null) {
                throw new LayerGenerationException("@RibbonRegistration can only be used together with @ActionID annotation", e, processingEnv, rReg==null? rRegs : rReg);
            }
            if (aid.id() == null) {
                continue;
            }
            
            if(rReg != null)
                processRegistration(e, rReg, aid);
            if (rRegs != null) {
                for (RibbonRegistration reg : rRegs.value()) {
                    processRegistration(e, reg, aid);
                }
            }
            
        }
        return true;
    }    

    private void processRegistration(Element e, RibbonRegistration ref, ActionID aid) throws LayerGenerationException {
        String name = aid.id().replace('.', '-');
        
        File f = layer(e).file(ref.path() + "/" + name + ".shadow");
        f.stringvalue("originalFile", "Actions/" + aid.category() + "/" + name + ".instance");
        f.position(ref.position());
        
        String str = ref.menuText();
        if(str!=null && str.length()>0)
            f.bundlevalue("menuText", str);
        
        str = ref.description();
        if(str!=null && str.length()>0)
            f.bundlevalue("description", str);
        
        str = ref.iconBase();
        if(str!=null && str.length()>0)
            f.stringvalue("iconBase", str);
        
        str = ref.tooltipBody();
        if(str!=null && str.length()>0)
            f.bundlevalue("tooltipBody", str);
        
        str = ref.tooltipTitle();
        if(str!=null && str.length()>0)
            f.bundlevalue("tooltipTitle", str);
        
        str = ref.tooltipIcon();
        if(str!=null && str.length()>0)
            f.stringvalue("tooltipIcon", str);
        
        str = ref.tooltipFooter();
        if(str!=null && str.length()>0)
            f.bundlevalue("tooltipFooter", str);
        
        str = ref.tooltipFooterIcon();
        if(str!=null && str.length()>0)
            f.stringvalue("tooltipFooterIcon", str);
        
        switch(ref.priority()) {
            case TOP: f.stringvalue("priority", "top"); break;
            case MEDIUM: f.stringvalue("priority", "medium"); break;
            case LOW: f.stringvalue("priority", "low"); break;
        }
        
        f.write();
        
        if (ref.separatorAfter() != Integer.MAX_VALUE) {
            if (ref.position() == Integer.MAX_VALUE || ref.position() >= ref.separatorAfter()) {
                throw new LayerGenerationException("separatorAfter() must be greater than position()", e, processingEnv, ref);
            }
            File after = layer(e).file(ref.path() + "/" + name + "-separatorAfter.instance");
            after.newvalue("instanceCreate", JSeparator.class.getName());
            after.position(ref.separatorAfter());
            after.write();
        }
        
        if (ref.separatorBefore() != Integer.MAX_VALUE) {
            if (ref.position() == Integer.MAX_VALUE || ref.position() <= ref.separatorBefore()) {
                throw new LayerGenerationException("separatorBefore() must be lower than position()", e, processingEnv, ref);
            }
            File before = layer(e).file(ref.path() + "/" + name + "-separatorBefore.instance");
            before.newvalue("instanceCreate", JSeparator.class.getName());
            before.position(ref.separatorBefore());
            before.write();
        }
    }
}
