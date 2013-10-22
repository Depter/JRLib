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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    
    protected final DataObject obj;
    
    public NamedDataObjectContent(DataObject obj) {
        this.obj = obj;
    }
    
    @Override
    public List<NamedContent> getContents() {
        List<NamedContent> result = new ArrayList<NamedContent>();
        if(obj instanceof DataFolder)
            for(DataObject child : ((DataFolder) obj).getChildren())
                result.addAll(getContent(child));
        return result;
    }
    
    private Collection<? extends NamedContent> getContent(DataObject obj) {
        Collection<? extends NamedContent> ncs = obj.getLookup().lookupAll(NamedContent.class);
        if(ncs.isEmpty())
            return Collections.singleton(new NamedDataObjectContent(obj));
        return ncs;
    }
//    
//    @Override
//    public Icon getIcon() {
//        Displayable displayable = getDisplayable();
//        if(displayable != null)
//            return displayable.getIcon();
//        
//        if(obj instanceof DataFolder)
//            return CommonIcons.folder();
//        
//        Image img = obj.getNodeDelegate().getIcon(BeanInfo.ICON_COLOR_16x16);
//        return ImageUtilities.image2Icon(img);
//    }

    @Override
    public String getName() {
        Displayable displayable = getLookup().lookup(Displayable.class);
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
