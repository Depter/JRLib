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
package org.jreserve.grscript.gui.classpath.explorer.nodes;

import java.awt.Image;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CpItemNode extends AbstractNode {
    
    final static String IMG_PATH = "org/jreserve/grscript/gui/classpath/explorer/nodes/";
    private final static Image JAR_IMG = ImageUtilities.loadImage(IMG_PATH+"jar.png");
    private final static Image CLASS_IMG = ImageUtilities.loadImage(IMG_PATH+"class.png");

    private ClassPathItem item;
    
    public CpItemNode(ClassPathItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
        initDisplayName();
        super.setShortDescription(item.getPath());
    }

    private void initDisplayName() {
        String path = item.getPath();
        setShortDescription(path);
        setDisplayName(getName(path));
    }
    
    private String getName(String path) {
        return new java.io.File(path).getName();
    }

    @Override
    public Image getIcon(int type) {
        switch(item.getType()) {
            case JAR: return JAR_IMG;
            case CLASS: return CLASS_IMG;
            default: return super.getIcon(type);
        }
    }
}
