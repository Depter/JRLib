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

import org.jreserve.gui.data.dataobject.node.DataSourceNode;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.swing.Icon;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.data.spi.DataProvider;
import org.jreserve.gui.data.spi.inport.SaveType;
import org.jreserve.gui.misc.audit.api.AuditableMultiview;
import org.jreserve.gui.misc.audit.api.AuditableObject;
import org.jreserve.gui.misc.utils.actions.AbstractDeletableDataObject;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.jrlib.gui.data.DataEntry;
import org.jreserve.jrlib.gui.data.DataEntryFilter;
import org.jreserve.jrlib.gui.data.DataType;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataSourceDataObject.Loader=DataSource File"
})
public class DataSourceDataObject extends MultiDataObject {
    private final static int LKP_VERSION = 1;
    private final static boolean IS_MULTIVIEW = true;
    
    public final static String MIME_TYPE = "text/jreserve-data-source-triangle";
    public final static String EXTENSION = "jds";
    
    public final static String PROP_DATA_TYPE = "data.type";
    public final static String PROP_FACTORY_ID = "factory.id";
    public final static String PROP_AUDIT_ID = "audit.id";
    
    private final long auditId;
    private final DataProvider provider;
    private final DataType dataType;
    private final InstanceContent ic = new InstanceContent();
    private final Lookup lkp;
    
    private String path;
    
    public DataSourceDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fo, loader);
        registerEditor(MIME_TYPE, IS_MULTIVIEW);
        calculatePath();
        
        Properties props = DataSourceUtil.loadProperties(fo);
        auditId = DataSourceUtil.getLong(props, PROP_AUDIT_ID, -1);
        provider = DataSourceUtil.createProvider(this, props);
        dataType = DataSourceUtil.getDataType(fo, props);
        
        for(FileObject entry : provider.getSecondryFiles())
            super.registerEntry(entry);
        
        ic.add(new DataSourceAuditable());
        ic.add(new DataSourceImpl());
        ic.add(new DataSourceDeletable());
        lkp = new ProxyLookup(super.getCookieSet().getLookup(), new AbstractLookup(ic));
    }
    
    private void calculatePath() {
        path = Displayable.Utils.displayProjectPath(getPrimaryFile());
    }
    
    @Override
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
        return path;
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataSourceNode(this);
    }

    @Override
    public String getName() {
        return getPrimaryFile().getName();
    }

    @Override
    protected void handleDelete() throws IOException {
        super.handleDelete();
        DataEventUtil.fireDeleted(this);
    }

    @Override
    protected DataObject handleCopy(DataFolder df) throws IOException {
        DataObject result = super.handleCopy(df);
        DataEventUtil.fireCreated((DataSourceDataObject) result, this);
        return result;
    }

    @Override
    protected FileObject handleRename(String name) throws IOException {
        String oldPath = path;
        FileObject result = super.handleRename(name);
        path = Displayable.Utils.displayProjectPath(result);
        DataEventUtil.fireRenamed(this, oldPath);
        return result;
    }

    @Override
    protected FileObject handleMove(DataFolder df) throws IOException {
        String oldPath = path;
        FileObject result = super.handleMove(df);
        path = Displayable.Utils.displayProjectPath(result);
        DataEventUtil.fireRenamed(this, oldPath);
        return result;
    }

    @Override
    protected DataObject handleCopyRename(DataFolder df, String name, String ext) throws IOException {
        DataObject result = super.handleCopyRename(df, name, ext);
        DataEventUtil.fireCreated((DataSourceDataObject) result);
        return result;
    }
    
    @MultiViewElement.Registration(
        displayName = "#LBL.EditorUtil.DataSourceAudit",
        mimeType = MIME_TYPE,
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "org.jreserve.gui.data.dataobject.DataSourceAudit",
        position = 1000
    )
    @NbBundle.Messages("LBL.EditorUtil.DataSourceAudit=Audit")
    public static MultiViewElement createAuditElement(Lookup lkp) {
        return new AuditableMultiview(lkp);
    }
    
    private class DataSourceAuditable extends AuditableObject {

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
            if(provider.addEntries(entries, saveType)) {
                DataEventUtil.fireDataImported(DataSourceDataObject.this);
                return true;
            }
            return false;
        }

        @Override
        public boolean deleteEntries(Set<DataEntry> entries) throws Exception {
            if(provider.deleteEntries(entries)) {
                DataEventUtil.fireDataDeleted(DataSourceDataObject.this);
                return true;
            }
            return false;
        }
    } 
    
    private class DataSourceDeletable extends AbstractDeletableDataObject {

        private DataSourceDeletable() {
            super(DataSourceDataObject.this);
        }
        
        @Override
        public void delete() throws Exception {
            DataSourceDataObject.this.delete();
        }

        @Override
        public Icon getIcon() {
            if(DataType.TRIANGLE == dataType) {
                return ImageUtilities.loadImageIcon(DataSourceNode.IMG_TRIANGLE, false);
            } else {
                return ImageUtilities.loadImageIcon(DataSourceNode.IMG_VECTOR, false);
            }
        }

        @Override
        public String getDisplayName() {
            return getPath();
        }
    
    }
}
