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
import java.util.List;
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
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
    
    public DataFolderNode(Node original, DataObjectProvider doProvider, boolean isRoot) {
        this(original, doProvider, isRoot, new InstanceContent());
    }
    
    private DataFolderNode(Node original, DataObjectProvider doProvider, boolean isRoot, InstanceContent ic) {
        super(original, new DataFolderChildren(original, doProvider), 
              new ProxyLookup(original.getLookup(), new AbstractLookup(ic))
                );
        this.isRoot = isRoot;
        ic.add(doProvider);
        if(!isRoot)
            ic.add(new FolderDeletable(this));
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
    public boolean canCopy() {
        return !isRoot;
    }

    @Override
    public boolean canCut() {
        return !isRoot;
    }
    
    @Override
    public PasteType[] getPasteTypes(Transferable t) {
        final List<Node> pastables = getPastableNodes(t, NodeTransfer.COPY);
        if(pastables.isEmpty())
            return new PasteType[0];
        
        PasteType paste = new PasteType() {
            @Override
            public Transferable paste() throws IOException {
                Node[] nn = new Node[pastables.size()];
                for(int i=0; i<nn.length; i++)
                    nn[i] = pastables.get(i).cloneNode();
                getChildren().add(nn);
                return null;
            }
        };
        
        return new PasteType[]{paste};
    }
    
    private List<Node> getPastableNodes(Transferable t, int action) {
        List<Node> result = new ArrayList<Node>();
        Node[] nodes = NodeTransfer.nodes(t, action);
        if(nodes == null)
            return result;
        
        for(Node node : nodes)
            if(canPasteNode(node))
                result.add(node);
        return result;
    }
    
    private boolean canPasteNode(Node node) {
        DataObject obj = node.getLookup().lookup(DataObject.class);
        if(obj instanceof DataSourceDataObject) {
            return true;
        } else if(obj instanceof DataFolder) {
            DataObjectProvider dop = getLookup().lookup(DataObjectProvider.class);
            FileObject root = dop.getRootFolder().getPrimaryFile();
            FileObject dir = ((DataFolder)obj).getPrimaryFile();
            return FileUtil.isParentOf(root, dir);
        } else {
            return false;
        }
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
