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
package org.jreserve.gui.data.nodes;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
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
    "MSG.DataItemFlavor.Move=Moving: {0}",
    "# {0} - path",
    "MSG.DataItemFlavor.Move.Error=Mave of {0}!"
})
class DataItemFlavor<T extends DataItem> extends DataFlavor {
    
    private final static DataFlavor DATA_CATEGORY_FLAVOR = new DataCategoryFlavor();
    private final static DataFlavor DATA_SOURCE_FLAVOR = new DataSourceFlavor();
    
    static Transferable createTransferable(DataItem item) {
        if(item.getParent() == null)
            return null;
        return new DataItemTransferable(item);
    }
    
    private static DataFlavor getFlavor(DataItem item) {
        return (item instanceof DataCategory)?
                DATA_CATEGORY_FLAVOR :
                DATA_SOURCE_FLAVOR;
    }
    
    private DataItemFlavor(Class<T> clazz, String name) {
        super(clazz, name);
    }
    
    static class DataCategoryFlavor extends DataItemFlavor {
        private DataCategoryFlavor() {
            super(DataCategory.class, "DataCategory");
        }
    }
    
    static class DataSourceFlavor extends DataItemFlavor {
        private DataSourceFlavor() {
            super(DataSource.class, "DataSource");
        }
    }
    
    static PasteType getDropType(DataCategory target, Transferable t) {
        DataItem item = getTransferableItem(t);
        return (item != null)? 
                new DataItemPasteType(target, item) : 
                null;
    }
        
    private static DataItem getTransferableItem(Transferable t) {
        try {
            if(t.isDataFlavorSupported(DATA_SOURCE_FLAVOR))
                return (DataItem) t.getTransferData(DATA_SOURCE_FLAVOR);
            else if(t.isDataFlavorSupported(DATA_CATEGORY_FLAVOR))
                return (DataItem) t.getTransferData(DATA_CATEGORY_FLAVOR);
            else
                return null;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
    
    private static class DataItemSingle extends ExTransferable.Single {
        
        private DataItem item;
        
        public DataItemSingle(DataItem item) {
            super(getFlavor(item));
            this.item = item;
        }
        
        @Override
        protected Object getData() throws IOException, UnsupportedFlavorException {
            return item;
        }
    }
    
    private static class DataItemPasteType extends PasteType implements Runnable {
        
        private final static RequestProcessor RP = RequestProcessor.getDefault();
        
        private final DataCategory target;
        private final DataItem item;

        DataItemPasteType(DataCategory target, DataItem item) {
            this.target = target;
            this.item = item;
        }
        
        @Override
        public Transferable paste() throws IOException {
            final RequestProcessor.Task task = RP.create(this);
            final ProgressHandle ph = ProgressHandleFactory.createHandle("Move: "+item.getPath());
            task.addTaskListener(new TaskListener() {
                @Override
                public void taskFinished(Task task) {
                    ph.finish();
                }
            });
            ph.start();
            task.schedule(0);
            return null;
        }
        
        @Override
        public void run() {
            try {
                target.getDataManager().moveDataItem(target, item);
            } catch (Exception ex) {
                BubbleUtil.showException(Bundle.MSG_DataItemFlavor_Move_Error(item.getPath()), ex);
            }
        }
    }
    
    private static class DataItemTransferable implements Transferable {
        
        private final DataItem item;
        private final DataFlavor flavor;
        private final DataFlavor[] flavors;
        
        private DataItemTransferable(DataItem item) {
            this.item = item;
            this.flavor = (item instanceof DataCategory)?
                    DATA_CATEGORY_FLAVOR : DATA_SOURCE_FLAVOR;
            this.flavors = new DataFlavor[]{flavor};
        }
        
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return this.flavor == flavor;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if(isDataFlavorSupported(flavor))
                return item;
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
