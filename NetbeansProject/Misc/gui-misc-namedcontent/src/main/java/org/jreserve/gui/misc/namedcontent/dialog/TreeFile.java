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

package org.jreserve.gui.misc.namedcontent.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import org.jreserve.gui.misc.namedcontent.NamedContent;
import org.jreserve.gui.misc.namedcontent.NamedContentChooserController;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TreeFile implements TreeItem {

    private NamedContent file;
    private List<TreeFile> children = new ArrayList<TreeFile>();

    TreeFile(NamedContent file) {
        this.file = file;
        
        for(NamedContent child : file.getContents())
            children.add(new TreeFile(child));
    }
   
    @Override
    public List<? extends TreeItem> getChildren() {
        return children;
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public String getName() {
        return file.getDisplayName();
    }

    @Override
    public Icon getIcon() {
        return file.getIcon();
    }
    
    @Override
    public boolean containsAcceptable(NamedContentChooserController controller) {
        if(controller.acceptsContent(file))
            return true;
        for(TreeFile child : children)
            if(child.containsAcceptable(controller))
                return true;
        return false;
    }

    @Override
    public void cleanUp(NamedContentChooserController controller) {
        Iterator<TreeFile> it = children.iterator();
        while(it.hasNext()) {
            TreeFile child = it.next();
            child.cleanUp(controller);
            if(child.getChildCount() == 0 && !child.containsAcceptable(controller))
                it.remove();
        }
    }
    
    NamedContent getContent() {
        return file;
    }
}
