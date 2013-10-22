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

package org.jreserve.gui.calculations.vector.wizard;

import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.api.NamedDataSourceProvider;
import org.jreserve.gui.misc.namedcontent.ProjectContentProvider;
import org.jreserve.gui.misc.utils.wizard.AbstractWizardPanel;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.CreateVectorWizardPanel2.DOP.NotFound=Project does not contain data storages!",
    "MSG.CreateVectorWizardPanel2.DataSource.Empty=Field 'Storage' is empty!",
    "MSG.CreateVectorWizardPanel2.DataSource.NotFound=Data storage not found!",
    "MSG.CreateVectorWizardPanel2.Date.EndBeforeStart=End date is before start date!"
})
class CreateVectorWizardPanel2 extends AbstractWizardPanel<CreateVectorVisualPanel2>{

    @Override
    protected CreateVectorVisualPanel2 createComponent() {
        return new CreateVectorVisualPanel2(this);
    }

    @Override
    protected void initComponent() {
        Object p = wiz.getProperty(CreateVectorWizardIterator.PROP_PROJECT);
        if(p instanceof Project)
            panel.setProject((Project) p);
        panel.loadSettings();
    }
    
    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty(CreateVectorWizardIterator.PROP_GEOMETRY, createGeometry());
        wiz.putProperty(CreateVectorWizardIterator.PROP_DATA_SOURCE, getDataSource());
        panel.storeSettings();
    }
    
    private TriangleGeometry createGeometry() {
        MonthDate start = panel.getStartDate();
        MonthDate end = panel.getEndDate();
        int al = panel.getAccidentLength();
        return new TriangleGeometry(start, end, al, 1);
    }
    
    private DataSource getDataSource() {
        ProjectContentProvider pol = panel.getProjectObjectLookup();
        String path = panel.getDataSourcePath();
        return pol.getContent(path, DataSource.class);
    }

    @Override
    protected boolean isInputValid() {
        return isStorageValid() && isGeometryValid();
    }
    
    private boolean isStorageValid() {
        NamedDataSourceProvider dop = panel.getDataSourceProvider();
        if(dop == null) {
            showError(Bundle.MSG_CreateVectorWizardPanel2_DOP_NotFound());
            return false;
        }
        
        String path = panel.getDataSourcePath();
        if(path == null || path.length() == 0) {
            showError(Bundle.MSG_CreateVectorWizardPanel2_DataSource_Empty());
            return false;
        }
        
        ProjectContentProvider pol = panel.getProjectObjectLookup();
        if(pol == null || pol.getContent(path, DataSource.class) == null) {
            showError(Bundle.MSG_CreateVectorWizardPanel2_DataSource_NotFound());
            return false;
        }
        
        return true;
    }
    
    private boolean isGeometryValid() {
        MonthDate start = panel.getStartDate();
        MonthDate end = panel.getEndDate();
        if(start.after(end)) {
            showError(Bundle.MSG_CreateVectorWizardPanel2_Date_EndBeforeStart());
            return false;
        }
        
        return true;
    }
}
