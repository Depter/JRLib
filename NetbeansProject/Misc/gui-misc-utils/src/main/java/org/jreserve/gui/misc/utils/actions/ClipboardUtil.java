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
package org.jreserve.gui.misc.utils.actions;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.jreserve.gui.misc.utils.tasks.TaskUtil;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.LoaderTransfer;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "MSG.ClipboardUtil.Pasting=Pasting '{0}'",
    "LBL.ClipboardUtil.Copy.Name=Paste",
    "# {0} - path",
    "MSG.ClipboardUtil.Copy.Error=Unable to copy '{0}'",
    "LBL.ClipboardUtil.Move.Name=Paste",
    "# {0} - path",
    "MSG.ClipboardUtil.Move.Error=Unable to move '{0}'"
})
public class ClipboardUtil {
    
    private final static Logger logger = Logger.getLogger(ClipboardUtil.class.getName());
    
    public static Cutable createCutable(DataObject obj) {
        return new CutableObject(obj);
    }
    
    public static Cutable createCutable(Node node) {
        return new CutableNode(node);
    }
    
    public static Copiable createCopiable(Node node) {
        return new CopiableNode(node);
    }
    
    public static Copiable createCopiable(DataObject obj) {
        return new CopiableObject(obj);
    }
    
    public static Pasteable createPasteable(Node node) {
        return new PasteableNode(node);
    }
    
    public static interface Pasteable {
        public PasteType[] getPasteTypes(Transferable t);
    }
    
    public static interface Copiable {
        public boolean canCopy();
    
        public Transferable clipboardCopy() throws IOException;
    }
    
    public static interface Cutable {
        public boolean canCut();
    
        public Transferable clipboardCut() throws IOException;
    }
    
    public static PasteType copyPasteType(DataFolder folder, DataObject client) {
        return new CopyPasteType(folder, client);
    }
    
    public static PasteType movePasteType(DataFolder folder, DataObject client) {
        return new MovePasteType(folder, client);
    }
    
    public static PasteType multiPasteType(List<PasteType> pasteTypes) {
        return new MultiPaste(pasteTypes);
    }
    
    private static class CutableNode implements Cutable {
        
        private Node node;
        
        private CutableNode(Node node) {
            if(node == null)
                throw new NullPointerException("Node can not be null!");
            this.node = node;
        }
        
        @Override
        public boolean canCut() {
            return node.canCut();
        }

        @Override
        public Transferable clipboardCut() throws IOException {
            return node.clipboardCut();
        }
    }    
    
    private static class CopiableNode implements Copiable {
        
        private Node node;
        
        private CopiableNode(Node node) {
            if(node == null)
                throw new NullPointerException("Node can not be null!");
            this.node = node;
        }
        
        @Override
        public boolean canCopy() {
            return node.canCopy();
        }

        @Override
        public Transferable clipboardCopy() throws IOException {
            return node.clipboardCopy();
        }
    }

    private static class PasteableNode implements Pasteable {
        
        private Node node;
        
        private PasteableNode(Node node) {
            if(node == null)
                throw new NullPointerException("Node can not be null!");
            this.node = node;
        }
        
        @Override
        public PasteType[] getPasteTypes(Transferable t) {
            return node.getPasteTypes(t);
        }
    }
    
    private static class CutableObject implements Cutable {
        private DataObject obj;
        
        private CutableObject(DataObject obj) {
            if(obj == null)
                throw new NullPointerException("DataObject can not be null!");
            this.obj = obj;
        }

        @Override
        public boolean canCut() {
            return obj.isMoveAllowed();
        }

        @Override
        public Transferable clipboardCut() throws IOException {
            return LoaderTransfer.transferable(obj, LoaderTransfer.CLIPBOARD_CUT);
        }
    }
    
    private static class CopiableObject implements Copiable {
        private DataObject obj;
        
        private CopiableObject(DataObject obj) {
            if(obj == null)
                throw new NullPointerException("DataObject can not be null!");
            this.obj = obj;
        }

        @Override
        public boolean canCopy() {
            return obj.isCopyAllowed();
        }

        @Override
        public Transferable clipboardCopy() throws IOException {
            return LoaderTransfer.transferable(obj, LoaderTransfer.CLIPBOARD_COPY);
        }
    }
    
    public static abstract class DataObjectPasteType extends PasteType implements Runnable {
        
        protected final DataFolder folder;
        protected final DataObject client;

        protected DataObjectPasteType(DataFolder folder, DataObject client) {
            if(folder == null)
                throw new NullPointerException("Folder can not be null!");
            this.folder = folder;
            if(client == null)
                throw new NullPointerException("Client can not be null!");
            this.client = client;
        }
        
        @Override
        public Transferable paste() throws IOException {
            if(folder == null || client == null)
                return null;
            if(canPaste(folder.getPrimaryFile(), client.getPrimaryFile()))
                doPaste();
            return ExTransferable.EMPTY;
        }
        
        protected boolean canPaste(FileObject myFolder, FileObject fo) {
            if(fo.isData())
                return true;
            return !FileUtil.isParentOf(fo, myFolder);
        }
        
        private void doPaste() {
            String msg = Bundle.MSG_ClipboardUtil_Pasting(client.getPrimaryFile().getPath());
            final ProgressHandle ph = ProgressHandleFactory.createHandle(msg);
            ph.start();
            ph.switchToIndeterminate();
            TaskUtil.execute(this, new TaskListener() {
                @Override
                public void taskFinished(Task task) {
                    ph.finish();
                }
            });
        }
    }

    public static class CopyPasteType extends DataObjectPasteType {

        public CopyPasteType(DataFolder folder, DataObject client) {
            super(folder, client);
        }
        
        public String getName() {
            return Bundle.LBL_ClipboardUtil_Copy_Name();
        }

        @Override
        public void run() {
            try {
                client.copy(folder);
            } catch (Exception ex) {
                String path = client.getPrimaryFile().getPath();
                String msg = Bundle.MSG_ClipboardUtil_Copy_Error(path);
                BubbleUtil.showException(msg, ex);
            }
        }
    }
    
    public static class MovePasteType extends DataObjectPasteType {

        public MovePasteType(DataFolder folder, DataObject client) {
            super(folder, client);
        }
        
        public String getName() {
            return Bundle.LBL_ClipboardUtil_Move_Name();
        }

        @Override
        public void run() {
            try {
                //TODO check move
                client.move(folder);
            } catch (Exception ex) {
                String path = client.getPrimaryFile().getPath();
                String msg = Bundle.MSG_ClipboardUtil_Move_Error(path);
                BubbleUtil.showException(msg, ex);
            }
        }
    }
    
    public static class MultiPaste extends PasteType {
        
        private List<PasteType> pastes;

        public MultiPaste(List<PasteType> pastes) {
            this.pastes = pastes;
        }
        
        @Override
        public Transferable paste() throws IOException {
            Transferable t = null;
            for(PasteType pt : pastes) {
                try {
                    t = pt.paste();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Unable to complete paste: "+pt, ex);
                }
            }
            return t;
        }
    
    }
    private ClipboardUtil() {}
}
