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
package org.jreserve.grscript.gui.script.function.explorer.nodes;

import java.awt.Image;
import org.jreserve.grscript.gui.script.functions.FunctionFolder;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionFolderNode extends AbstractNode {

    final static String IMG_PATH = "org/jreserve/grscript/gui/script/function/explorer/nodes/";
    private final static Image OPENED_IMG = ImageUtilities.loadImage(IMG_PATH+"folderOpen.gif");
    private final static Image CLOSED_IMG = ImageUtilities.loadImage(IMG_PATH+"folder.gif");
    
    public FunctionFolderNode(FunctionFolder folder) {
        super(Children.create(new FunctionFolderChildren(folder), true), Lookups.singleton(folder));
        setDisplayName(folder.getName());
    }

    @Override
    public Image getIcon(int type) {
        return CLOSED_IMG;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return OPENED_IMG;
    }
}
