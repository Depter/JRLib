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
package org.jreserve.gui.misc.utils.dataobject;

import org.jreserve.gui.misc.utils.dataobject.selectdialog.SelectDataObjectDialog;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.DataObjectChooser.Ok=Ok",
    "LBL.DataObjectChooser.Cancel=Cancel",
    "LBL.DataObjectChooser.Folder=Select Folder"
})
public class DataObjectChooser {
    
    public static DataFolder[] selectFolders(DataObjectProvider root) {
        return selectFolders(root.getRootFolder());
    }
    
    public static DataFolder[] selectFolders(DataFolder... roots) {
        DataObject[] sel = select(new FolderController(roots));
        if(sel == null)
            return null;
        DataFolder[] result = new DataFolder[sel.length];
        for(int i=0; i<sel.length; i++)
            result[i] = (DataFolder) sel[i];
        return result;
    }
    
    public static DataFolder selectOneFolder(DataObjectProvider root) {
        return selectOneFolder(root.getRootFolder());
    }
    
    public static DataFolder selectOneFolder(DataFolder... roots) {
        return (DataFolder) selectOne(new FolderController(roots));
    }
    
    public static DataObject selectOne(Controller controller) {
        DataObject[] result = SelectDataObjectDialog.select(controller, false);
        if(result==null || result.length==0)
            return null;
        return result[0];
    }
    
    public static DataObject[] select(Controller controller) {
        return SelectDataObjectDialog.select(controller, true);
    }
    
    private DataObjectChooser() {}
    
    public static interface Controller {
        public DataObject[] getRoots();
        
        public String getTitle();
        
        public String getOkText();
        
        public String getCancelText();
        
        public boolean showDataObject(DataObject obj);
        
        public boolean canSelectObject(DataObject obj);
    }
    
    public static class DefaultController implements Controller {
        
        private DataObject[] roots;
        private String title;
        private String okText = Bundle.LBL_DataObjectChooser_Ok();
        private String cancelText = Bundle.LBL_DataObjectChooser_Cancel();
        
        public DefaultController(String title, DataObject... roots) {
            this.roots = roots;
            this.title = (title==null)? "" : title;
        }
        
        public DataObject[] getRoots() {
            return roots;
        }
        
        @Override
        public String getTitle() {
            return title;
        }
        
        public void setOkText(String text) {
            okText = text!=null? text : Bundle.LBL_DataObjectChooser_Ok();
        }
        
        @Override
        public String getOkText() {
            return okText;
        }
        
        public void setCancelText(String text) {
            cancelText = text!=null? text : Bundle.LBL_DataObjectChooser_Cancel();
        }

        @Override
        public String getCancelText() {
            return cancelText;
        }

        @Override
        public boolean showDataObject(DataObject obj) {
            return obj instanceof DataFolder;
        }

        @Override
        public boolean canSelectObject(DataObject obj) {
            return true;
        }
    }

    public static class FolderController extends DefaultController {

        public FolderController(DataObject... roots) {
            super(Bundle.LBL_DataObjectChooser_Folder(), roots);
        }

        @Override
        public boolean canSelectObject(DataObject obj) {
            return obj instanceof DataFolder;
        }
        
    }
}
