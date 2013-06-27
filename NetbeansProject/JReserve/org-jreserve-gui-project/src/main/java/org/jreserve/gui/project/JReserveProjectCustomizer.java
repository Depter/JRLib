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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - projectName",
    "LBL.JReserveProjectCustomizer.Dialog.Title={0} properties"
})
class JReserveProjectCustomizer implements CustomizerProvider {

    private final static String LAYER_PATH = "Projects/" + JReserveProjectFactory.LAYER_NAME + "/Customizer";
    private final Project project;

    JReserveProjectCustomizer(Project project) {
        this.project = project;
    }

    @Override
    public void showCustomizer() {
        Dialog dialog = ProjectCustomizer.createCustomizerDialog(
                LAYER_PATH, //Load from path
                project.getLookup(), //The lookup, to pass to configurations
                "",
                new OKOptionListener(), //Callback, when ok pressed
                null);                  //Help context
        String name = ProjectUtils.getInformation(project).getDisplayName();
        dialog.setTitle(Bundle.LBL_JReserveProjectCustomizer_Dialog_Title(name));
        dialog.setVisible(true);
    }

    private class OKOptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO check for saving project and save the project.
        }
    }
}
