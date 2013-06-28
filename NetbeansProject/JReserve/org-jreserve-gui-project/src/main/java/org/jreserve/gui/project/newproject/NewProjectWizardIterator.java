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
package org.jreserve.gui.project.newproject;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBException;
import org.jreserve.gui.project.config.ConfigFactory;
import org.jreserve.gui.project.config.ProjectConfiguration;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@TemplateRegistration(
    folder = "Project/JReserve", 
    displayName = "#LBL.DefaultProject.Name", 
    description = "NewProjectDescription.html", 
    iconBase = "org/jreserve/gui/project/project.png"
)
@Messages({
    "LBL.DefaultProject.Name=Empty Project",
    "LBL_CreateProjectStep=Name and Location"
})
public class NewProjectWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {

    final static String PROP_PROJECT_FOLDER = "projectFolder";
    final static String PROP_PROJECT_NAME = "projectName";
    
    private WizardDescriptor wizard;
    private WizardDescriptor.Panel[] panels;
    private int panelIndex;
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;

        panelIndex = 0;
        panels = createPanels();
        String[] steps = createSteps();
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if(steps[i] == null)
                steps[i] = c.getName();
            
            if(c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, new Integer(i));
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            }
        }
    }
    
    private WizardDescriptor.Panel[] createPanels() {
        return new WizardDescriptor.Panel[]{
            new NewProjectWizardPanel()
        };
    }

    private String[] createSteps() {
        return new String[]{
            Bundle.LBL_CreateProjectStep()
        };
    }

    @Override
    public void uninitialize(WizardDescriptor wd) {
        wizard.putProperty(PROP_PROJECT_NAME, null);
        wizard.putProperty(PROP_PROJECT_FOLDER, null);
        wizard = null;
        panels = null;
    }

    @Override
    public String name() {
         return MessageFormat.format("{0} of {1}",
                new Object[]{new Integer(panelIndex + 1), new Integer(panels.length)});
   }

    @Override
    public boolean hasNext() {
        return panelIndex < panels.length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return panelIndex > 0;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panels[panelIndex];
    }

    @Override
    public void nextPanel() {
        if (hasNext()) {
            panelIndex++;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void previousPanel() {
        if(hasPrevious()) {
            panelIndex--;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }

    @Override
    public Set instantiate(ProgressHandle ph) throws IOException {
        ph.switchToIndeterminate();
        ph.start();
        
        File file = getProjectFile();
        if(!file.mkdir())
            throw new IOException("Unable to create folder: "+file.getAbsolutePath());
        ProjectConfiguration config = new ProjectConfiguration();
        config.setName(file.getName());
        try {
            ConfigFactory.writeConfig(config, file);
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }

        ph.finish();
        throw new UnsupportedOperationException("Not supported yet1."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private File getProjectFile() {
        String name = (String) wizard.getProperty(PROP_PROJECT_NAME);
        String folder = (String) wizard.getProperty(PROP_PROJECT_FOLDER);
        return FileUtil.normalizeFile(new File(folder + File.separator + name));
    }

    @Override
    public Set instantiate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet2."); //To change body of generated methods, choose Tools | Templates.
    }
}
