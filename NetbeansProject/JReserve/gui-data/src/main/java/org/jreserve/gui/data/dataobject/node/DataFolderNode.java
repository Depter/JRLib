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
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.jreserve.gui.misc.utils.dataobject.DataObjectProvider;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
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
