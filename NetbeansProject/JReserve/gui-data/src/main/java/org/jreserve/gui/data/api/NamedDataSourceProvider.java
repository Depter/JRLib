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

package org.jreserve.gui.data.api;

import javax.swing.Icon;
import org.jreserve.gui.data.dataobject.DataRootFileProvider;
import org.jreserve.gui.misc.namedcontent.AbstractNamedContentProvider;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectServiceProvider;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectServiceProvider(
    service = NamedDataSourceProvider.class,
    projectType = "org-jreserve-project-jreserve"
)
@Messages({
    "LBL.NamedDataSourceProvider.Name=Data"
})
public class NamedDataSourceProvider extends AbstractNamedContentProvider {
    
    @StaticResource private final static String IMG_PATH = "org/jreserve/gui/data/icons/database.png";
    private final static Icon ICON = ImageUtilities.loadImageIcon(IMG_PATH, false);
    
    public NamedDataSourceProvider(Project project) {
        super(project, DataRootFileProvider.DATA_FOLDER);
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_NamedDataSourceProvider_Name();
    }
}
