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

package org.jreserve.gui.calculations.nodes;

import java.awt.Image;
import org.jreserve.gui.calculations.CalculationRootFileProvider;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CalculationFolderNode.DummyName=Calculations"
})
class CalculationFolderNode extends AbstractNode {
    
    @StaticResource private final static String CALCULATION_IMG = "org/jreserve/gui/calculations/icons/calculator.png";
    
    private static DataFolder getFolder(Project project) {
        FileObject dir = project.getProjectDirectory();
        dir = dir.getFileObject(CalculationRootFileProvider.CALCULATIONS_FOLDER);
        if(dir == null)
            return null;
        try {
            return (DataFolder) DataObject.find(dir);
        } catch (Exception ex) {
            return null;
        }
    }
    
    private boolean isRoot;
    private DataFolder folder;
    
    public CalculationFolderNode(Project project) {
        this(getFolder(project), true);
    }

    public CalculationFolderNode(DataFolder folder, boolean isRoot) {
        super(
            Children.create(new CalculationsFolderNodeChildren(folder), true),
            folder==null? Lookup.EMPTY : Lookups.proxy(folder)
        );
        
        this.isRoot = isRoot;
        this.folder = folder;
        setDisplayName(folder==null? Bundle.LBL_CalculationFolderNode_DummyName() : folder.getName());
    }

    @Override
    public Image getIcon(int type) {
        if(isRoot)
            return ImageUtilities.loadImage(CALCULATION_IMG);
        return super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
}
