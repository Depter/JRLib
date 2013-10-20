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

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NamedDataObjectContent implements NamedContent {
    
    private final DataObject obj;
    private final Displayable displayable;
    
    public NamedDataObjectContent(DataObject obj) {
        this.obj = obj;
        displayable = obj.getLookup().lookup(Displayable.class);
    }
    
    @Override
    public List<NamedContent> getContents() {
        List<NamedContent> result = new ArrayList<NamedContent>();
        if(obj instanceof DataFolder)
            for(DataObject child : ((DataFolder) obj).getChildren())
                result.addAll(child.getLookup().lookupAll(NamedContent.class));
        return result;
    }
    
    @Override
    public Icon getIcon() {
        return displayable==null? 
                CommonIcons.folder() : 
                displayable.getIcon();
    }

    @Override
    public String getDisplayName() {
        return displayable==null?
                obj.getName() :
                displayable.getDisplayName();
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }
    
    @Override
    public String toString() {
        return String.format("NamedDataObjectContent [%s]", 
                obj.getPrimaryFile().getPath());
    }
}
