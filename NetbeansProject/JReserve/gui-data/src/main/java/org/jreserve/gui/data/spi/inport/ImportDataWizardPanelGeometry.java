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
package org.jreserve.gui.data.spi.inport;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.inport.ImportDataVisualPanelGeometry;
import org.jreserve.gui.data.spi.inport.ImportDataProvider;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ImportDataWizardPanelGeometry implements WizardDescriptor.Panel<WizardDescriptor> {
    
    public final static String PROP_TRIANGLE_ARRAY = "triangle.array";
    public final static String PROP_TRIANGLE_GEOMETRY = "triangle.geometry";
    
    private final ChangeSupport cs = new ChangeSupport(this);
    private ImportDataVisualPanelGeometry component;
    private WizardDescriptor wiz;
    private boolean valid = true;
    
    @Override
    public Component getComponent() {
        if(component == null) {
            component = new ImportDataVisualPanelGeometry();
            if(wiz != null)
                initComponent();
        }
        return component;
    }
    
    private void initComponent() {
        double[][] values = (double[][]) wiz.getProperty(PROP_TRIANGLE_ARRAY);
        component.setTriangleValues(values);
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(component != null)
            initComponent();
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(ImportDataProvider.PROP_IMPORT_DATA, component.createEntries());
        settings.putProperty(PROP_TRIANGLE_GEOMETRY, component.createGeometry());
        if(component != null)
            component.storePreferences();
    }

    @Override
    public boolean isValid() {
        return valid;
    }
    
    @Override
    public void addChangeListener(ChangeListener l) {
        cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        cs.removeChangeListener(l);
    }
}
