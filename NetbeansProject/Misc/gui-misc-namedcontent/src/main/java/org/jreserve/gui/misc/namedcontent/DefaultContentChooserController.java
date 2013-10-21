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
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultContentChooserController implements NamedContentChooserController {
    
    private NamedContentProvider root;
    private Class clazz;
    private String title;

    public DefaultContentChooserController(NamedContentProvider root, Class clazz, String title) {
        this.root = root;
        this.clazz = clazz;
        this.title = title;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public NamedContentProvider getRoot() {
        return root;
    }

    @Override
    public boolean showsContent(NamedContent content) {
        Lookup lkp = content.getLookup();
        return lkp.lookup(DataFolder.class) != null ||
               lkp.lookup(clazz) != null;
    }

    @Override
    public boolean acceptsContent(NamedContent content) {
        return content.getLookup().lookup(clazz) != null;
    }
    
    @Override
    public boolean acceptsFolder() {
        return false;
    }
}
