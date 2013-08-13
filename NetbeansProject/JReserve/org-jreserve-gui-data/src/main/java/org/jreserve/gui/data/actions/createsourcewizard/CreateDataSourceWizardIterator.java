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
package org.jreserve.gui.data.actions.createsourcewizard;

import org.jreserve.gui.data.spi.DataSourceWizard;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.jreserve.gui.data.spi.DataProvider;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.WeakListeners;

public final class CreateDataSourceWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(CreateDataSourceWizardIterator.class.getName());
    
    final static String PROP_SOURCE_WIZARD = "source.type.iterator";
    final static String PROP_DATA_CATEGORY = "data.Category";
    final static String PROP_DATA_NAME = "data.Name";
    final static String PROP_DATA_TYPE = "data.Type";
    
    private DataCategory dataCategory;
    private ChangeSupport cs = new ChangeSupport(this);
    private SourceTypeListener stListener = new SourceTypeListener();
    private ChangeListener sourceItListener = new SourceIteratorListener();
    private List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>();
    
    private int panelCount;
    private int index;
    private WizardDescriptor wizardDesc;
    
    private DataSourceWizard sourceWizard;
    
    public CreateDataSourceWizardIterator(DataCategory dataCategory) {
        this.dataCategory = dataCategory;
    }
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        panels.add(new CreateDataSourceWizardPanel1());
        panels.add(new CreateDataSourceWizardPanel2());
        
        this.wizardDesc = wizard;
        wizard.putProperty(PROP_DATA_CATEGORY, dataCategory);

        wizard.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
        wizard.putProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
        
        wizardDesc.addPropertyChangeListener(WeakListeners.propertyChange(stListener, wizard));
        
        reclaulcateState();
    }
    
    private void typeChanged() {
        releaseOldIterator();
        sourceWizard = (DataSourceWizard) wizardDesc.getProperty(PROP_SOURCE_WIZARD);
        reclaulcateState();
    }
    
    private void releaseOldIterator() {
        if(sourceWizard != null) {
            sourceWizard.removeChangeListener(sourceItListener);
            while(panels.size() > 2)
                panels.remove(2);
        }
    }
    
    private void reclaulcateState() {
        initPanels();
        String[] steps = initSteps();
        if(index > 1)
            index = 1;
        wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
    }
    
    private void initPanels() {
        if(sourceWizard != null)
            panels.addAll(sourceWizard.getPanels());
        panelCount = panels.size();
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
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panels.get(index);
    }

    @Override
    public String name() {
        if(index < 2)
            return (index+1) + " of ...";
        return (index+1) + " of " + panels.size();
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
        wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(++index));
    }

    @Override
    public void previousPanel() {
        if(!hasPrevious())
            throw new NoSuchElementException();
        wizardDesc.putProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(--index));
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        if(sourceWizard != null)
            sourceWizard.removeChangeListener(sourceItListener);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }

    @Override
    public Set instantiate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Set instantiate(ProgressHandle handle) throws IOException {
        handle.start();
        handle.switchToIndeterminate();
        try {
            return Collections.singleton(buildDataSource());
        } catch(IOException ex) {
            String msg = "Unable to create new DataSource!";
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        } finally {
            handle.finish();
        }
    }
    
    private DataSource buildDataSource() throws IOException {
        DataType dataType = (DataType) wizardDesc.getProperty(PROP_DATA_TYPE);
        if(dataType == null)
            throw new IllegalStateException("DataType not set (property name 'CreateDataSourceWizardIterator.PROP_DATA_TYPE')!");
        
        String name = (String) wizardDesc.getProperty(PROP_DATA_NAME);
        if(name == null)
            throw new IllegalStateException("Name not set (property name 'CreateDataSourceWizardIterator.PROP_DATA_NAME')!");
            
        DataCategory parent = (DataCategory) wizardDesc.getProperty(PROP_DATA_CATEGORY);
        if(parent == null)
            throw new IllegalStateException("DataCategory not set (property name 'CreateDataSourceWizardIterator.PROP_DATA_CATEGORY')!");
            
        DataSourceWizard sw = (DataSourceWizard) wizardDesc.getProperty(PROP_SOURCE_WIZARD);
        if(sw == null)
            throw new IllegalStateException("DataSourceWizard not set (property name 'CreateDataSourceWizardIterator.PROP_SOURCE_WIZARD')!");
        
        DataProvider provider = sw.createDataProvider(wizardDesc);
        if(provider == null)
            throw new IllegalStateException(String.format("DataSourceWizard '%s' returned null for provider!", sw));
        
        return parent.getDataManager().createDataSource(parent, name, dataType, provider);
    }
    
    private class SourceIteratorListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            reclaulcateState();
            cs.fireChange();
        }
    }
    
    private class SourceTypeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(PROP_SOURCE_WIZARD.equals(evt.getPropertyName())) {
                typeChanged();
            }
        }
    }
}
