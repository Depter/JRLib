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

package org.jreserve.gui.data.dataobject;

import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataSourceDataObject.Loader=DataSource File"
})
@MIMEResolver.ExtensionRegistration(
    displayName = "#LBL.DataSourceDataObject.Loader",
    mimeType = DataSourceDataObject.MIME_TYPE,
    extension = {"jdst"}
)
@DataObject.Registration(
    mimeType = DataSourceDataObject.MIME_TYPE,
    iconBase = "org/jreserve/gui/data/icons/database_triangle.png",
    displayName = "#LBL.DataSourceDataObject.Loader",
    position = 300
)
@ActionReferences({
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
        position = 100, separatorAfter = 200),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
        position = 300),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
        position = 400, separatorAfter = 500),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
        position = 600),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
        position = 700, separatorAfter = 800),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
        position = 900, separatorAfter = 1000),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
        position = 1100, separatorAfter = 1200),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
        position = 1300),
    @ActionReference(
        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
        position = 1400)
})
public class DataSourceDataObject extends MultiDataObject {
    private final static int LKP_VERSION = 1;
    private final static boolean IS_MULTIVIEW = true;
    public final static String MIME_TYPE = "text/jreserve-data-source-triangle";
    
    private final static String PROP_DATA_TYPE = "data.type";
    private final static String PROP_FACTORY_ID = "factory.id";
    private final static String PROP_AUDIT_ID = "audit.id";
    
    private final long auditId;
    private final Project project;
    
    public DataSourceDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fo, loader);
        registerEditor(MIME_TYPE, IS_MULTIVIEW);
        
        Properties props = DataSourceUtil.loadProperties(fo);
        auditId = DataSourceUtil.getLong(props, PROP_AUDIT_ID, -1);
        project = FileOwnerQuery.getOwner(fo);
        
    }
    
    public Project getProject() {
        return project;
    }
    
    public long getAuditId() {
        return auditId;
    }
    
    @Override
    protected int associateLookup() {
        return LKP_VERSION;
    }
}
