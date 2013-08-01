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
import org.openide.util.Exceptions;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.PasteType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataItemFlavor<T extends DataItem> extends DataFlavor {
    
    private final static DataFlavor DATA_CATEGORY_FLAVOR = new DataCategoryFlavor();
    private final static DataFlavor DATA_SOURCE_FLAVOR = new DataSourceFlavor();
    
    static Transferable createTransferable(Transferable t, DataItem item) {
        if(item.getParent() == null)
            return t;
        
        ExTransferable added = ExTransferable.create(t);
        added.put(new DataItemSingle(item));
        return added;
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
    
    private static class DataItemPasteType extends PasteType {
        
        private final DataCategory target;
        private final DataItem item;

        DataItemPasteType(DataCategory target, DataItem item) {
            this.target = target;
            this.item = item;
        }
        
        @Override
        public Transferable paste() throws IOException {
            target.getDataManager().moveDataItem(target, item);
            return null;
        }
    
//        private void checkMove(DataItem target, DataItem item) {
//            if(target.getDataManager() != item.getDataManager()) {
//            }
//               !isChildOf(item, target);
//        }
//    
//        private static boolean isChildOf(DataItem parent, DataItem child) {
//            while(child != null) {
//                if(parent == child)
//                    return true;
//                child = child.getParent();
//            }
//            return false;
//        }
    }
}
