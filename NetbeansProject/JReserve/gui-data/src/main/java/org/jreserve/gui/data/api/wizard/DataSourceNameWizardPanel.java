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
import javax.swing.event.ChangeListener;
import org.jreserve.gui.data.api.NamedDataSourceProvider;
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.DataSourceNameWizardPanel.NoDOP=DataProvider not present in the given project!",
    "MSG.DataSourceNameWizardPanel.NoDataType=Type not selected!",
    "MSG.DataSourceNameWizardPanel.NoName=Name is not selected!",
    "# {0} - root",
    "MSG.DataSourceNameWizardPanel.PathNotInRoot=Data sources must be within ''{0}'' folder!",
    "MSG.DataSourceNameWizardPanel.PathExists=Data source already exists!"
})
public class DataSourceNameWizardPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    private DataSourceNameVisualPanel panel;
    private boolean isValid;
    private WizardDescriptor wiz;
    private ChangeSupport cs = new ChangeSupport(this);
    
    @Override
    public Component getComponent() {
        if(panel == null) {
            panel = new DataSourceNameVisualPanel(this);
            if(wiz != null)
                initPanel();
        }
        return panel;
    }
    
    private void initPanel() {
        Project project = (Project) wiz.getProperty(AbstractDataSourceWizardIterator.PROP_PROJECT);
        panel.setProject(project);
        NamedDataSourceProvider dop = (NamedDataSourceProvider) wiz.getProperty(AbstractDataSourceWizardIterator.PROP_OBJECT_PROVIDER);
        panel.setObjectProvider(dop);
        DataFolder folder = (DataFolder) wiz.getProperty(AbstractDataSourceWizardIterator.PROP_FOLDER);
        panel.setDataFolder(folder);
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(WizardDescriptor settings) {
        this.wiz = settings;
        if(panel != null)
            initPanel();
    }

    @Override
    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty(AbstractDataSourceWizardIterator.PROP_PATH, panel.getDataSourcePath());
        settings.putProperty(AbstractDataSourceWizardIterator.PROP_DATA_TYPE, panel.getDataType());
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
    
    void panelChanged() {
        isValid = isInputValid();
        if(isValid)
            showError(null);
        cs.fireChange();
    }
    
    private void showError(String msg) {
        if(wiz != null)
            wiz.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
    }
    
    private boolean isInputValid() {
        return isObjectProviderSet() && 
               isDataTypeValid() &&
               isNameValid() &&
               isPathValid();
    }
    
    private boolean isObjectProviderSet() {
        if(panel.getObjectProvider() == null) {
            showError(Bundle.MSG_DataSourceNameWizardPanel_NoDOP());
            return false;
        }
        return true;
    }
    
    private boolean isDataTypeValid() {
        if(panel.getDataType() == null) {
            showError(Bundle.MSG_DataSourceNameWizardPanel_NoDataType());
            return false;
        }
        return true;
    }
    
    private boolean isNameValid() {
        String name = panel.getFileName();
        if(name==null || name.length()==0) {
            showError(Bundle.MSG_DataSourceNameWizardPanel_NoName());
            return false;
        }
        return true;
    }
    
    private boolean isPathValid() {
        NamedDataSourceProvider dop = panel.getObjectProvider();
        String path = panel.getDataSourcePath();
        
        FileObject root = dop.getRootFolder();
        
        path += "." + DataSourceDataObject.EXTENSION;
        FileObject fo = root.getFileObject(path);
        if(fo != null) {
            showError(Bundle.MSG_DataSourceNameWizardPanel_PathExists());
            return false;
        }
        return true;
    }
}
