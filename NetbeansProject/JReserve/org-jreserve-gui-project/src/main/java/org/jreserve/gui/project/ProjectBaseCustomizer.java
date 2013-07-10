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
package org.jreserve.gui.project;

import javax.swing.JComponent;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.ProjectBaseCustomizer.Category=Project"
})
public class ProjectBaseCustomizer implements ProjectCustomizer.CompositeCategoryProvider {
    
    private String name;
    private ProjectBaseCustomizerPanel panel;
    
    private ProjectBaseCustomizer(String name) {
        this.name = name;
    }
    
    @Override
    public ProjectCustomizer.Category createCategory(Lookup lkp) {
        return ProjectCustomizer.Category.create(name, name, null);
    }

    @Override
    public JComponent createComponent(ProjectCustomizer.Category category, Lookup lookup) {
        if(panel == null)
            panel = new ProjectBaseCustomizerPanel(category, lookup);
        return panel;
    }
    
    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = JReserveProjectFactory.LAYER_NAME,
            position = 100
    )
    public static ProjectBaseCustomizer createCustomizer() {
        return new ProjectBaseCustomizer(Bundle.LBL_ProjectBaseCustomizer_Category());
    }
}
