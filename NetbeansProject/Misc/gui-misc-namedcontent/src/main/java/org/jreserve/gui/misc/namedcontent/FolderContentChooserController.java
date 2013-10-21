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

package org.jreserve.gui.misc.namedcontent;

import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.FolderContentChooserController.Title=Select Folder"
})
public class FolderContentChooserController implements NamedContentChooserController {

    private NamedContentProvider root;

    public FolderContentChooserController(NamedContentProvider root) {
        this.root = root;
    }
    
    @Override
    public String getTitle() {
        return Bundle.LBL_FolderContentChooserController_Title();
    }
    
    @Override
    public NamedContentProvider getRoot() {
        return root;
    }

    @Override
    public boolean showsContent(NamedContent content) {
        return content.getLookup().lookup(DataFolder.class) != null;
    }

    @Override
    public boolean acceptsContent(NamedContent content) {
        return content.getLookup().lookup(DataFolder.class) != null;
    }

    @Override
    public boolean acceptsFolder() {
        return true;
    }
}
