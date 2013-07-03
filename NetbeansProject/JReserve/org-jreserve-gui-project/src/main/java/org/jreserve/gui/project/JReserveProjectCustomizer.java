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
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.jreserve.gui.project.config.ConfigFactory;
import org.jreserve.gui.project.config.ProjectConfiguration;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
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
    
    private final static Logger logger = Logger.getLogger(JReserveProjectCustomizer.class.getName());
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
                null,
                new OkListener(),
                new StoreListener(), //Callback, when ok pressed
                null);                  //Help context
        String name = ProjectUtils.getInformation(project).getDisplayName();
        dialog.setTitle(Bundle.LBL_JReserveProjectCustomizer_Dialog_Title(name));
        dialog.setVisible(true);
    }
    
    private class OkListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {}
    }
    
    private class StoreListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = getConfigFile();
            ProjectConfiguration config = getConfiguration();
            save(config, file);
        }
        
        private File getConfigFile() {
            File folder = FileUtil.toFile(project.getProjectDirectory());
            return new File(folder, JReserveProjectFactory.CONFIG_FILE);
        }
        
        private ProjectConfiguration getConfiguration() {
            return project.getLookup()
                    .lookup(JReserveProjectInformation.class)
                    .getConfiguration();
        }
        
        private void save(ProjectConfiguration configuration, File file) {
            try {
                ConfigFactory.writeConfig(configuration, file);
                logger.log(Level.INFO, "JResereProject configuration saved to: {0}", file.getAbsolutePath());
            } catch(JAXBException ex) {
                String msg = String.format("Unable to save JReserveProject configuration to: %s", file.getAbsolutePath());
                logger.log(Level.SEVERE, msg, ex);
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
