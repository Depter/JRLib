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

import org.jreserve.gui.data.api.DataManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataRootNode.Name=Data"
})
public class DataRootNode extends AbstractNode {
    
    private final static String IMG_PATH = "org/jreserve/gui/data/icons/database.png";  //NOI18
    
    private static Lookup createLookup(DataManager manager) {
        return new ProxyLookup(
                manager.getProject().getLookup(),
                Lookups.singleton(manager.getCategory(null))
        );
    }
    
    public DataRootNode(DataManager manager) {
        super(Children.LEAF, createLookup(manager));
        setDisplayName(Bundle.LBL_DataRootNode_Name());
        setIconBaseWithExtension(IMG_PATH);
    }
    
    
}
