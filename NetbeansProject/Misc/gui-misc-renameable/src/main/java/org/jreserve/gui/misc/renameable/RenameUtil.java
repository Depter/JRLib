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
package org.jreserve.gui.misc.renameable;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import org.jreserve.gui.misc.renameable.dialog.MoveDialog;
import org.jreserve.gui.misc.renameable.dialog.RenameDialog;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RenameUtil {
    
    public static void rename(Renameable renameable) {
        RenameDialog.rename(renameable);
    }
    
    public static void move(DataFolder folder, List<DataObject> objects) {
        MoveDialog.move(folder, objects);
    }
    
    public static PasteType movePasteType(DataFolder folder, List<DataObject> sources) {
        if(sources.isEmpty())
            return null;
            
        FileObject pf = folder.getPrimaryFile();
        for(DataObject obj : sources)
            if(!canPaste(pf, obj.getPrimaryFile()))
                return null;
        
        return new DataObjectPasteType(folder, sources);
    }
        
    private static boolean canPaste(FileObject myFolder, FileObject fo) {
        if(fo.isData())
            return true;
        return !FileUtil.isParentOf(fo, myFolder);
    }
    
    private RenameUtil() {}

    private static class DataObjectPasteType extends PasteType {
        
        private final DataFolder folder;
        private final List<DataObject> sources;

        protected DataObjectPasteType(DataFolder folder, List<DataObject> sources) {
            this.folder = folder;
            this.sources = sources;
        }
        
        @Override
        public Transferable paste() throws IOException {
            MoveDialog.move(folder, sources);
            return ExTransferable.EMPTY;
        }
    }
}
