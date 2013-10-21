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
package org.jreserve.gui.data.dataobject.node;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import org.jreserve.gui.data.api.NamedDataSourceProvider;
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.jreserve.gui.misc.renameable.RenameUtil;
import org.jreserve.gui.misc.renameable.Renameable;
import org.jreserve.gui.misc.utils.actions.deletable.DataObjectDeletable;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.LoaderTransfer;
import org.openide.nodes.FilterNode;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ActionReferences({
    @ActionReference(
        path = DataFolderNode.ACTION_PATH,
        id = @ActionID(category = "Project", id = "org.netbeans.modules.project.ui.NewFile$WithSubMenu"),
        position = 100, separatorAfter = 150),
    @ActionReference(
        path = DataFolderNode.ACTION_PATH,
        id = @ActionID(category = "File", id = "org.jreserve.gui.misc.renameable.action.RenameAction"),
        position = 300),
    @ActionReference(
        path = DataFolderNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.CopyAction"),
        position = 400),
    @ActionReference(
        path = DataFolderNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.CutAction"),
        position = 500),
    @ActionReference(
        path = DataFolderNode.ACTION_PATH,
        id = @ActionID(category = "Edit", id = "org.jreserve.gui.misc.utils.actions.PasteAction"),
        position = 600, separatorAfter = 650),
    @ActionReference(
        path = DataFolderNode.ACTION_PATH,
        id = @ActionID(category = "File", id = "org.jreserve.gui.misc.utils.notifications.actions.DeleteAction"), 
        position = 700)
})
class DataFolderNode extends FilterNode {
    
    public final static String ACTION_PATH = "Node/DataFolderNode/Actions";  //NOI18
    private final static String ROOT_IMG_PATH = "org/jreserve/gui/data/icons/database.png"; //NOI18
    private final static String IMG_PATH = "org/jreserve/gui/data/icons/folder_db.png"; //NOI18
    private static Image ROOT_IMG = ImageUtilities.loadImage(ROOT_IMG_PATH);
    private static Image IMG = ImageUtilities.loadImage(IMG_PATH);
    
    private final boolean isRoot;
    private final DataFolder folder;
    
    public DataFolderNode(DataFolder folder, NamedDataSourceProvider doProvider, boolean isRoot) {
        this(folder, doProvider, isRoot, new InstanceContent());
    }
    
    private DataFolderNode(DataFolder folder, NamedDataSourceProvider doProvider, boolean isRoot, InstanceContent ic) {
        super(folder.getNodeDelegate(), 
              Children.create(new DFChildren(folder, doProvider), true),
              new ProxyLookup(folder.getLookup(), new AbstractLookup(ic))
        );
        this.folder = getLookup().lookup(DataFolder.class);
        ic.add(doProvider);
        ic.add(ClipboardUtil.createPasteable(this));
            
        this.isRoot = isRoot;
        if(!isRoot) {
            ic.add(new FolderDeletable(folder));
            ic.add(ClipboardUtil.createCopiable(folder));
            ic.add(ClipboardUtil.createCutable(folder));
            ic.add(new RenameableFolder());
        }
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Image getIcon(int type) {
        return isRoot? ROOT_IMG : IMG;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Transferable drag() throws IOException {
        return LoaderTransfer.transferable(folder, LoaderTransfer.DND_MOVE);
    }

    @Override
    public PasteType getDropType(Transferable t, int action, int index) {
        List<DataObject> objs = getPastedObjects(t, LoaderTransfer.CLIPBOARD_CUT);
        return RenameUtil.movePasteType(folder, objs);
    }
    
    @Override
    public PasteType[] getPasteTypes(Transferable t) {
        List<PasteType> pts = new ArrayList<PasteType>();
        for(DataObject obj : getPastedObjects(t, LoaderTransfer.CLIPBOARD_COPY))
            pts.add(ClipboardUtil.copyPasteType(folder, obj));
        
        for(DataObject obj : getPastedObjects(t, LoaderTransfer.CLIPBOARD_CUT))
            pts.add(ClipboardUtil.movePasteType(folder, obj));
        
        return pts.toArray(new PasteType[pts.size()]);
    }
    
    private List<DataObject> getPastedObjects(Transferable t, int action) {
        DataObject[] objects = LoaderTransfer.getDataObjects(t, action);
        if(objects == null || objects.length == 0)
            return Collections.EMPTY_LIST;
        
        List<DataObject> result = new ArrayList<DataObject>(objects.length);
        for(DataObject obj : objects) {
            if((obj instanceof DataSourceDataObject) && canPaste((DataSourceDataObject)obj)) {
                result.add(obj);
            } else if((obj instanceof DataFolder) && canPaste((DataFolder) obj)) {
                result.add(obj);
            }
        }
        
        return result;
    }
    
    private boolean canPaste(DataSourceDataObject obj) {
        if(obj.getFolder().equals(folder))
            return false;
        FileObject file = obj.getPrimaryFile();
        if(folder.getPrimaryFile().getFileObject(file.getNameExt()) != null)
            return false;
        
        return true;
    }
    
    private boolean canPaste(DataFolder obj) {
        FileObject client = obj.getPrimaryFile();
        //Can not paste parent into child
        if(FileUtil.isParentOf(client, folder.getPrimaryFile()))
            return false;
        //Can not paste into itself
        if(folder.equals(obj))
            return false;
        //Name already exists
        if(folder.getPrimaryFile().getFileObject(client.getName()) != null)
            return false;
        
        //Check if it is a folder containing DataSources.
        Project p = FileOwnerQuery.getOwner(client);
        if(p == null)
            return false;
        NamedDataSourceProvider dsop = p.getLookup().lookup(NamedDataSourceProvider.class);
        if(dsop == null)
            return false;
        FileObject dataRoot = dsop.getRoot();
        if(dataRoot == null)
            return false;
        
        return dataRoot.equals(client) || FileUtil.isParentOf(dataRoot, client);
    }
        
    private static class FolderDeletable extends DataObjectDeletable {
        
        private FolderDeletable(DataFolder folder) {
            super(folder);
        }

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(IMG_PATH, false);
        }

        @Override
        public String getDisplayName() {
            return Displayable.Utils.displayProjectPath(getDeletedObject().getPrimaryFile());
        }        
    }
    
    private class RenameableFolder implements Renameable {
        @Override
        public DataObject getObject() {
            return folder;
        }

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(IMG_PATH, false);
        }

        @Override
        public String getDisplayName() {
            return Displayable.Utils.displayProjectPath(folder.getPrimaryFile());
        }
    }
}
