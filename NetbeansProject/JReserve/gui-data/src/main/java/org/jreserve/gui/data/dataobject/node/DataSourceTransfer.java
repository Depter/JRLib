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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.data.dataobject.DataSourceDataObject;
import org.openide.loaders.DataObject;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataSourceTransfer extends ExTransferable.Single {
    private final static int COPY = DnDConstants.ACTION_COPY;
    private final static int MOVE = DnDConstants.ACTION_MOVE;
    private final static String ACTION_PARAM = "action";
    private final static String MIME_TYPE = 
            DataSourceDataObject.MIME_TYPE+
            ";class="+DataSourceDataObject.class.getName()+
            ";"+ACTION_PARAM+"=%d";
    
    private final static Logger logger = Logger.getLogger(DataSourceTransfer.class.getName());
    
    static ExTransferable.Single createCopy(DataObject obj) {
        return new DataSourceTransfer(obj, getFlavor(COPY));
    }
    
    private static DataFlavor getFlavor(int action) {
        String mimeType = String.format(MIME_TYPE, action);
        return new DataFlavor(mimeType, null);
    }
    
    static ExTransferable.Single createMove(DataObject obj) {
        return new DataSourceTransfer(obj, getFlavor(MOVE));
    }
    
    static DataObject getCopiedObject(Transferable t) {
        return getSourceObjects(t, COPY);
    }
    
    private static DataObject getSourceObjects(Transferable t, int action) {
        DataFlavor flavor = getFlavor(action);
        if(isSupported(t, flavor, action)) {
            try {
                return (DataObject) t.getTransferData(flavor);
            } catch (ClassCastException ex) {
                logger.log(Level.WARNING, "Unable to obtain DataObject from transferable!", ex);
            } catch (UnsupportedFlavorException ex) {
                logger.log(Level.WARNING, "Unable to obtain DataObject from transferable!", ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to obtain DataObject from transferable!", ex);
            }
        }
        return null;
    }
    
    private static boolean isSupported(Transferable t, DataFlavor flavor, int action) {
        for(DataFlavor f : t.getTransferDataFlavors())
            if(flavor.equals(f) && action == Integer.parseInt(f.getParameter(ACTION_PARAM)))
                return true;
        return false;
    }
    
    static DataObject getMovedObject(Transferable t) {
        return getSourceObjects(t, MOVE);
    }
    
    private final DataObject obj;

    private DataSourceTransfer(DataObject obj, DataFlavor flavor) {
        super(flavor);
        this.obj = obj;
    }
    
    @Override
    protected DataObject getData() throws IOException, UnsupportedFlavorException {
        return obj;
    }
}
