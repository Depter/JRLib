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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.DataCategory;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.WeakListeners;

public final class CreateDataSourceWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator<WizardDescriptor> {

    final static String PROP_SOURCE_WIZARD = "source.type.iterator";
    final static String PROP_DATA_CATEGORY = "dataCategory";
    
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
        
        this.wizardDesc = wizard;
        wizard.putProperty(DataSourceWizard.PROP_SOURCE_PROPERTIES, new HashMap<String, String>());
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
            while(panels.size() > 1)
                panels.remove(1);
        }
    }
    
    private void reclaulcateState() {
        initPanels();
        String[] steps = initSteps();
        index = 0;
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
        if(index == 0)
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
    public Set instantiate(ProgressHandle handle) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set instantiate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    private class SourceIteratorListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
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

    // Example of invoking this wizard:
    // @ActionID(category="...", id="...")
    // @ActionRegistration(displayName="...")
    // @ActionReference(path="Menu/...")
    // public static ActionListener run() {
    //     return new ActionListener() {
    //         @Override public void actionPerformed(ActionEvent e) {
    //             WizardDescriptor wiz = new WizardDescriptor(new CreateDataSourceWizardWizardIterator());
    //             // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
    //             // {1} will be replaced by WizardDescriptor.Iterator.name()
    //             wiz.setTitleFormat(new MessageFormat("{0} ({1})"));
    //             wiz.setTitle("...dialog title...");
    //             if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
    //                 ...do something...
    //             }
    //         }
    //     };
    // }
    
//    private int index;
//    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;
//
//    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
//        if (panels == null) {
//            panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
//            panels.add(new CreateDataSourceWizardWizardPanel1());
//            String[] steps = new String[panels.size()];
//            for (int i = 0; i < panels.size(); i++) {
//                Component c = panels.get(i).getComponent();
//                // Default step name to component name of panel.
//                steps[i] = c.getName();
//                if (c instanceof JComponent) { // assume Swing components
//                    JComponent jc = (JComponent) c;
//                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
//                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
//                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
//                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
//                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
//                }
//            }
//        }
//        return panels;
//    }
//
//    @Override
//    public WizardDescriptor.Panel<WizardDescriptor> current() {
//        return getPanels().get(index);
//    }
//
//    @Override
//    public String name() {
//        return index + 1 + ". from " + getPanels().size();
//    }
//
//    @Override
//    public boolean hasNext() {
//        return index < getPanels().size() - 1;
//    }
//
//    @Override
//    public boolean hasPrevious() {
//        return index > 0;
//    }
//
//    @Override
//    public void nextPanel() {
//        if (!hasNext()) {
//            throw new NoSuchElementException();
//        }
//        index++;
//    }
//
//    @Override
//    public void previousPanel() {
//        if (!hasPrevious()) {
//            throw new NoSuchElementException();
//        }
//        index--;
//    }
//
//    // If nothing unusual changes in the middle of the wizard, simply:
//    @Override
//    public void addChangeListener(ChangeListener l) {
//    }
//
//    @Override
//    public void removeChangeListener(ChangeListener l) {
//    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed
}
