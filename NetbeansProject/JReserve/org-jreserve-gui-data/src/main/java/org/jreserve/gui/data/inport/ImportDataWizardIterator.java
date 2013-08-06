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
package org.jreserve.gui.data.inport;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataEntry;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.SaveType;
import org.jreserve.gui.data.spi.ImportDataProvider;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.WeakListeners;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ImportDataWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {
    
    private final static Logger logger = Logger.getLogger(ImportDataWizardIterator.class.getName());
    
    final static String PROP_INIT_DATA_ITEM = "init.data.item"; //Panel 0
    
    private DataItem dataItem;
    private WizardDescriptor wizardDesc;
    private ImportWizardListener stListener = new ImportWizardListener();
    private ImportDataProvider importWizard;
    private SourceIteratorListener sourceItListener = new SourceIteratorListener();
    private ChangeSupport cs = new ChangeSupport(this);
    private List<WizardDescriptor.Panel> panels = new ArrayList<WizardDescriptor.Panel>();
    private int panelCount;
    private int index;
    
    public ImportDataWizardIterator(DataItem dataItem) {
        this.dataItem = dataItem;
    }
    
    @Override
    public void initialize(WizardDescriptor wizard) {
        panels.add(new ImportDataWizardPanel1());
        panels.add(new ImportDataWizardPanelLast());
        
        this.wizardDesc = wizard;
        wizard.putProperty(PROP_INIT_DATA_ITEM, dataItem);

        wizard.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
        wizard.putProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
        wizard.putProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);
        
        wizardDesc.addPropertyChangeListener(WeakListeners.propertyChange(stListener, wizard));
        
        importWizardChanged();
    }
    
    private void importWizardChanged() {
        releaseOldWizard();
        importWizard = (ImportDataProvider) wizardDesc.getProperty(ImportDataProvider.PROP_IMPORT_WIZARD);
        reclaulcateState();
    }
    
    private void releaseOldWizard() {
        if(importWizard != null) {
            importWizard.removeChangeListener(sourceItListener);
            while(panels.size() > 2)
                panels.remove(1);
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
        if(importWizard != null)
            panels.addAll(1, importWizard.getPanels());
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
        if(index < 1)
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
        if(importWizard != null)
            importWizard.removeChangeListener(sourceItListener);
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
        DataSource ds = (DataSource) wizardDesc.getProperty(ImportDataProvider.PROP_DATA_SOURCE);
        if(ds == null)
            throw new IOException("DataSource not set!");
        SaveType st = (SaveType) wizardDesc.getProperty(ImportDataProvider.PROP_SAVE_TYPE);
        if(st == null)
            throw new IOException("SaveType not set!");
        List<DataEntry> entries = (List<DataEntry>) wizardDesc.getProperty(ImportDataProvider.PROP_IMPORT_DATA);
        if(entries == null)
            return Collections.EMPTY_SET;
        
        try {
            ds.addEntries(new TreeSet<DataEntry>(entries), st);
        } catch (Exception ex) {
            String msg = String.format("Unable to save data to '%s'!", ds.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
        
        return Collections.EMPTY_SET;
    }
    
    private class SourceIteratorListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            reclaulcateState();
            cs.fireChange();
        }
    }
    
    private class ImportWizardListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(ImportDataProvider.PROP_IMPORT_WIZARD.equals(evt.getPropertyName())) {
                importWizardChanged();
            }
        }
    }    
}
