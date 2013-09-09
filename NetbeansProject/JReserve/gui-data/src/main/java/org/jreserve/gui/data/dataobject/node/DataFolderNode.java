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
import org.openide.util.datatransfer.ExTransferable;
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
    public Transferable clipboardCopy() throws IOException {
        ExTransferable t = ExTransferable.create(super.clipboardCopy());
        DataObject obj = getLookup().lookup(DataFolder.class);
        if(obj != null)
            t.put(DataSourceTransfer.createCopy(obj));
        return t;
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        ExTransferable t = ExTransferable.create(super.clipboardCut());
        DataObject obj = getLookup().lookup(DataFolder.class);
        if(obj != null)
            t.put(DataSourceTransfer.createMove(obj));
        return t;
    }

    @Override
    public Transferable drag() throws IOException {
        ExTransferable t = ExTransferable.create(super.drag());
        DataObject obj = getLookup().lookup(DataFolder.class);
        if(obj != null)
            t.put(DataSourceTransfer.createMove(obj));
        return t;
    }

    //FROM org.netbeans.spi.java.project.support.ui.PackageRootNode
    //    public @Override void createPasteTypes(Transferable t, List<PasteType> list) {
    //        if (t.isDataFlavorSupported(ExTransferable.multiFlavor)) {
    //            try {
    //                MultiTransferObject mto = (MultiTransferObject) t.getTransferData(ExTransferable.multiFlavor);
    //                List<PackageViewChildren.PackageNode> l = new ArrayList<PackageViewChildren.PackageNode>();
    //                boolean isPackageFlavor = false;
    //                boolean hasTheSameRoot = false;
    //                int op = -1;
    //                for (int i=0; i < mto.getCount(); i++) {
    //                    Transferable pt = mto.getTransferableAt(i);
    //                    DataFlavor[] flavors = mto.getTransferDataFlavors(i);
    //                    for (int j=0; j< flavors.length; j++) {
    //                        if (PackageViewChildren.SUBTYPE.equals(flavors[j].getSubType ()) &&
    //                                PackageViewChildren.PRIMARY_TYPE.equals(flavors[j].getPrimaryType ())) {
    //                            if (op == -1) {
    //                                op = Integer.valueOf (flavors[j].getParameter (PackageViewChildren.MASK)).intValue ();
    //                            }
    //                            PackageViewChildren.PackageNode pkgNode = (PackageViewChildren.PackageNode) pt.getTransferData(flavors[j]);
    //                            if ( !((PackageViewChildren)getChildren()).getRoot().equals( pkgNode.getRoot() ) ) {
    //                                l.add(pkgNode);
    //                            }
    //                            else {
    //                                hasTheSameRoot = true;
    //                            }
    //                            isPackageFlavor = true;
    //                        }
    //                    }
    //                }
    //                if (isPackageFlavor && !hasTheSameRoot) {
    //                    list.add(new PackageViewChildren.PackagePasteType(this.group.getRootFolder(),
    //                            l.toArray(new PackageViewChildren.PackageNode[l.size()]),
    //                            op));
    //                }
    //                else if (!isPackageFlavor) {
    //                    list.addAll( Arrays.asList( getDataFolderNodeDelegate().getPasteTypes( t ) ) );
    //                }
    //            } catch (UnsupportedFlavorException e) {
    //                Exceptions.printStackTrace(e);
    //            } catch (IOException e) {
    //                Exceptions.printStackTrace(e);
    //            }
    //        }
    //        else {
    //            DataFlavor[] flavors = t.getTransferDataFlavors();
    //            FileObject root = this.group.getRootFolder();
    //            boolean isPackageFlavor = false;
    //            if (root!= null  && root.canWrite()) {
    //                for (DataFlavor flavor : flavors) {
    //                    if (PackageViewChildren.SUBTYPE.equals(flavor.getSubType ()) &&
    //                            PackageViewChildren.PRIMARY_TYPE.equals(flavor.getPrimaryType ())) {
    //                        isPackageFlavor = true;
    //                        try {
    //                            int op = Integer.parseInt(flavor.getParameter(PackageViewChildren.MASK));
    //                            PackageViewChildren.PackageNode pkgNode = (PackageViewChildren.PackageNode) t.getTransferData(flavor);
    //                            if ( !((PackageViewChildren)getChildren()).getRoot().equals( pkgNode.getRoot() ) ) {
    //                                list.add(new PackageViewChildren.PackagePasteType (root, new PackageViewChildren.PackageNode[] {pkgNode}, op));
    //                            }
    //                        } catch (IOException ioe) {
    //                            Exceptions.printStackTrace(ioe);
    //                        }
    //                        catch (UnsupportedFlavorException ufe) {
    //                            Exceptions.printStackTrace(ufe);
    //                        }
    //                    }
    //                }
    //            }
    //            if (!isPackageFlavor) {
    //                list.addAll( Arrays.asList( getDataFolderNodeDelegate().getPasteTypes( t ) ) );
    //            }
    //        }
    //    }
    
    
    @Override
    public PasteType getDropType(Transferable t, int action, int index) {
        return null;
    }
    
    
    @Override
    public PasteType[] getPasteTypes(Transferable t) {
        return new PasteType[0];
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
    
    private abstract class DataSourcePasteType extends PasteType {
        
        protected final DataFolder folder;
        protected final DataObject client;

        public DataSourcePasteType(DataObject client) {
            folder = getLookup().lookup(DataFolder.class);
            this.client = client;
        }
        
        @Override
        public Transferable paste() throws IOException {
            if(folder == null || client == null)
                return null;
            if(canPaste(folder.getPrimaryFile(), client.getPrimaryFile()))
                operate();
            return null;
        }
        
        private boolean canPaste(FileObject myFolder, FileObject fo) {
            if(fo.isData())
                return true;
            return !FileUtil.isParentOf(fo, myFolder);
        }
        
        protected abstract void operate() throws IOException;
    }
    
    private class CopyPasteType extends DataSourcePasteType {

        private CopyPasteType(DataObject client) {
            super(client);
        }

        @Override
        protected void operate() throws IOException {
            //TODO check copy
            client.copy(folder);
        }
    }
    
    private class MovePasteType extends DataSourcePasteType {

        private MovePasteType(DataObject client) {
            super(client);
        }

        @Override
        protected void operate() throws IOException {
            //TODO check move
            client.move(folder);
        }
    }
}
