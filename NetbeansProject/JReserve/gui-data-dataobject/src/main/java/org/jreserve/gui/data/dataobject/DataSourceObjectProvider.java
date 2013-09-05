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
package org.jreserve.gui.data.dataobject;

import java.io.IOException;
import javax.swing.Icon;
import org.jreserve.gui.misc.utils.dataobject.AbstractDataObjectProvider;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectServiceProvider;
import org.openide.loaders.DataFolder;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectServiceProvider(
    service = DataSourceObjectProvider.class,
    projectType = "org-jreserve-project-jreserve"
)
@Messages({
    "LBL.DataSourceObjectProvider.Name=Data2"
})
public class DataSourceObjectProvider extends AbstractDataObjectProvider {
    
    private final static String IMG = "org/jreserve/gui/data/icons/database.png";
    private final static String ROOT_NAME = "Data2";
    
    public DataSourceObjectProvider(Project project) {
        super(project);
    }

    @Override
    protected String getRootName() {
        return ROOT_NAME;
    }

    @Override
    public DataFolder createFolder(String path) throws IOException {
        return super.createFolder(path);
    }

    @Override
    public Icon getIcon() {
        return ImageUtilities.loadImageIcon(IMG, false);
    }

    @Override
    public String getDisplayName() {
        return Bundle.LBL_DataSourceObjectProvider_Name();
    }
}