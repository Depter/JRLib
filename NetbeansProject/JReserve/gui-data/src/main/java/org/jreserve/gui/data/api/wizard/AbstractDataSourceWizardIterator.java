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
package org.jreserve.gui.data.api.wizard;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - current",
    "# {1} - total",
    "LBL.AbstractDataSourceWizardIterator.Name={0} of {1}"
})
public abstract class AbstractDataSourceWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {

    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel> panels;
    private int index;
    private int panelCount;
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        panels = new ArrayList<WizardDescriptor.Panel>(createPanels());
        panelCount = panels.size();
        
        //TODO add properties
        
        calculateState();
    }
    
    protected List<WizardDescriptor.Panel> createPanels() {
        return Collections.singletonList((WizardDescriptor.Panel) new DataSourceNameWizardPanel());
    }
    
    private void calculateState() {
        String[] steps = initSteps();
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
        index = 0;
    }
    
    private String[] initSteps() {
        String[] steps = new String[panelCount];
        for(int i=0; i<panelCount; i++) {
            Component c = panels.get(i).getComponent();
            steps[i] = c.getName();
            initPanel(i, steps, c);
        }
        return steps;
    }
    
    private void initPanel(int i, String[] steps, Component c) {
        if(c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(i));
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
        }
    }    
    
    @Override
    public void uninitialize(WizardDescriptor wizard) {
        this.wizard = null;
        this.panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panels.get(index);
    }

    @Override
    public String name() {
        return Bundle.LBL_AbstractDataSourceWizardIterator_Name(index+1, panels.size());
    }

    @Override
    public boolean hasNext() {
        return index < (panels.size()-1);
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if(!hasNext())
            throw new NoSuchElementException();
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(++index));
    }

    @Override
    public void previousPanel() {
        if(!hasPrevious())
            throw new NoSuchElementException();
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(--index));
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public Set instantiate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set instantiate(ProgressHandle handle) throws IOException {
        //TODO get parameters
        //Create primary file
        
        //long auditId = AuditDbManager.getInstance().getNextObjectId(getDataManager().getProject())
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected abstract void createSecondaryFiles(FileObject primaryFile) throws IOException;
}
