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
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.jreserve.gui.data.dataobject.DataSourceObjectProvider;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.jreserve.gui.misc.utils.dataobject.ProjectObjectLookup;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.LoaderTransfer;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataFolderNode extends FilterNode {
    
    private final static String ROOT_IMG_PATH = "org/jreserve/gui/data/icons/database.png";
    private final static String IMG_PATH = "org/jreserve/gui/data/icons/folder_db.png";
    private static Image ROOT_IMG = ImageUtilities.loadImage(ROOT_IMG_PATH);
    private static Image IMG = ImageUtilities.loadImage(IMG_PATH);
    
    private final boolean isRoot;
    private final DataFolder folder;
    
    public DataFolderNode(Node original, DataObjectProvider doProvider, boolean isRoot) {
        this(original, doProvider, isRoot, new InstanceContent());
    }
    
    private DataFolderNode(Node original, DataObjectProvider doProvider, boolean isRoot, InstanceContent ic) {
        super(original, new DataFolderChildren(original, doProvider), 
              new ProxyLookup(original.getLookup(), new AbstractLookup(ic))
                );
        this.folder = getLookup().lookup(DataFolder.class);
        ic.add(doProvider);
        ic.add(ClipboardUtil.createPasteable(this));
        
        this.isRoot = isRoot;
        if(!isRoot) {
            ic.add(new FolderDeletable(this));
            ic.add(ClipboardUtil.createCopiable(folder));
            ic.add(ClipboardUtil.createCutable(folder));
        }
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
        return !isRoot;
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
        List<DataObject> objs = getPastedObjects(t, LoaderTransfer.DND_MOVE);
        if(objs.isEmpty())
            return null;
        
        List<PasteType> pts = new ArrayList<PasteType>(objs.size());
        for(DataObject obj : objs)
            pts.add(ClipboardUtil.movePasteType(folder, obj));

        return ClipboardUtil.multiPasteType(pts);
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
        if(objects == null)
            return Collections.EMPTY_LIST;
        
        List<DataObject> result = new ArrayList<DataObject>(objects.length);
        for(DataObject obj : objects) {
            if((obj instanceof DataSourceDataObject) &&
                !obj.getFolder().equals(folder)) {
                result.add(obj);
            } else if(obj instanceof DataFolder) {
                FileObject client = obj.getPrimaryFile();
                if(FileUtil.isParentOf(client, folder.getPrimaryFile()))
                    continue;
                if(folder.equals(obj))
                    continue;
                
                Project p = FileOwnerQuery.getOwner(client);
                if(p == null)
                    continue;
                ProjectObjectLookup pol = p.getLookup().lookup(ProjectObjectLookup.class);
                if(pol == null)
                    continue;
                String path = FileUtil.getRelativePath(p.getProjectDirectory(), client);
                if(pol.lookupOne(path, DataSourceObjectProvider.class) == null)
                    continue;
                result.add(obj);
            }
        }
        
        return result;
    }
    
    
    private static class FolderDeletable extends Deletable.NodeDeletable {
        
        private FolderDeletable(DataFolderNode node) {
            super(node);
        }
        
        @Override
        public void delete() throws Exception {
            DataFolder folder = node.getLookup().lookup(DataFolder.class);
            folder.delete();
        }
    }
}
