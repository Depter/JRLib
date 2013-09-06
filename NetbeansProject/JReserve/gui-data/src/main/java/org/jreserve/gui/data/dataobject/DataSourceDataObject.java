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
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.SaveType;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataSourceDataObject.Loader=DataSource File"
})
//@MIMEResolver.ExtensionRegistration(
//    displayName = "#LBL.DataSourceDataObject.Loader",
//    mimeType = DataSourceDataObject.MIME_TYPE,
//    extension = {"jdst"}
//)
//@DataObject.Registration(
//    mimeType = DataSourceDataObject.MIME_TYPE,
//    iconBase = "org/jreserve/gui/data/icons/database_triangle.png",
//    displayName = "#LBL.DataSourceDataObject.Loader",
//    position = 300
//)
//@ActionReferences({
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
//        position = 100, separatorAfter = 200),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
//        position = 300),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
//        position = 400, separatorAfter = 500),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
//        position = 600),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
//        position = 700, separatorAfter = 800),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
//        position = 900, separatorAfter = 1000),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
//        position = 1100, separatorAfter = 1200),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
//        position = 1300),
//    @ActionReference(
//        path = "Loaders/"+DataSourceDataObject.MIME_TYPE+"/Actions",
//        id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
//        position = 1400)
//})
public class DataSourceDataObject extends MultiDataObject {
    private final static int LKP_VERSION = 1;
    private final static boolean IS_MULTIVIEW = true;
    public final static String MIME_TYPE = "text/jreserve-data-source-triangle";
    
    private final static String PROP_DATA_TYPE = "data.type";
    private final static String PROP_FACTORY_ID = "factory.id";
    private final static String PROP_AUDIT_ID = "audit.id";
    
    private final long auditId;
    private final DataProvider provider;
    private final InstanceContent ic = new InstanceContent();
    private final Lookup lkp;
    
    public DataSourceDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fo, loader);
        registerEditor(MIME_TYPE, IS_MULTIVIEW);
        
        Properties props = DataSourceUtil.loadProperties(fo);
        auditId = DataSourceUtil.getLong(props, PROP_AUDIT_ID, -1);
        provider = DataSourceUtil.createProvider(fo, props);
        for(MultiDataObject.Entry entry : provider.getSecondaryEntries(this))
            super.addSecondaryEntry(entry);
        
        ic.add(new DataSourceAuditable());
        ic.add(new DataSourceImpl());
        lkp = new ProxyLookup(super.getCookieSet().getLookup(), new AbstractLookup(ic));
    }
    
    public Lookup getLookup() {
        return lkp;
    }
    
    public long getAuditId() {
        return auditId;
    }
    
    @Override
    protected int associateLookup() {
        return LKP_VERSION;
    }
    
    private String getPath() {
        return Displayable.Utils.displayPath(getNodeDelegate());
    }
    
    private class DataSourceAuditable implements AuditedObject {

        @Override
        public Project getAuditedProject() {
            return FileOwnerQuery.getOwner(getPrimaryFile());
        }

        @Override
        public long getAuditId() {
            return auditId;
        }

        @Override
        public String getAuditName() {
            return getPath();
        }
    }
    
    private class DataSourceImpl implements DataSource {

        private DataProvider provider;
        private DataType dataType;
        
        @Override
        public String getName() {
            return DataSourceDataObject.this.getName();
        }

        @Override
        public String getPath() {
            return DataSourceDataObject.this.getPath();
        }

        @Override
        public DataType getDataType() {
            return dataType;
        }

        @Override
        public List<DataEntry> getEntries(DataEntryFilter filter) throws Exception {
            return provider.getEntries(filter);
        }

        @Override
        public boolean addEntries(Set<DataEntry> entries, SaveType saveType) throws Exception {
            //TODO fire event
            return provider.addEntries(entries, saveType);
        }

        @Override
        public boolean deleteEntries(Set<DataEntry> entries) throws Exception {
            //TODO fire event
            return provider.deleteEntries(entries);
        }
    } 
}