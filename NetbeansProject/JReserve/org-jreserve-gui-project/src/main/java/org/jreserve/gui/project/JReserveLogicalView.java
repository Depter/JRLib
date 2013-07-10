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

import java.awt.Image;
import java.util.List;
import javax.swing.Action;
import org.jreserve.gui.misc.eventbus.EventBusListener;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.project.api.ProjectEvent;
import org.jreserve.gui.project.api.ProjectEvent.ProjectPropertyChangedEvent;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class JReserveLogicalView implements LogicalViewProvider {

    private final static String LAYER_PATH = "Projects/"+JReserveProjectFactory.LAYER_NAME+"/Nodes";    //NOI18
    private final static String ACTION_PATH = "Node/JReserveProject/Actions";
    
    private final Project project;

    JReserveLogicalView(Project project) {
        this.project = project;
    }
    
    @Override
    public Node createLogicalView() {
        FileObject dir = project.getProjectDirectory();
        DataFolder folder = DataFolder.findFolder(dir);
        return new JReserveProjectRootNode(project, folder.getNodeDelegate());
    }

    @Override
    public Node findPath(Node node, Object o) {
        //TODO implement
        return null;
    }
    
    private static class JReserveProjectRootNode extends FilterNode {
        
        private JReserveProjectRootNode(Project project, Node node) {
            super(node,
                  NodeFactorySupport.createCompositeChildren(project, LAYER_PATH),
                  new ProxyLookup(project.getLookup(), node.getLookup())
            );
            setDisplayName(ProjectUtils.getInformation(project).getDisplayName());
            EventBusManager.getDefault().subscribe(this);
        }

        @Override
        public Image getIcon(int type) {
            return JReserveProjectFactory.getProjectIcon().getImage();
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public Action[] getActions(boolean arg0) {
            List<? extends Action> actions = Utilities.actionsForPath(ACTION_PATH);
            int size = actions.size();
            return actions.toArray(new Action[size]);
//            return new Action[]{
//                CommonProjectActions.newFileAction(),
//                CommonProjectActions.copyProjectAction(),
//                CommonProjectActions.deleteProjectAction(),
//                CommonProjectActions.customizeProjectAction(),
//                CommonProjectActions.closeProjectAction()
//            };
        }
        
        @EventBusListener(forceEDT=true)
        public void projectPropertyChange(ProjectPropertyChangedEvent event) {
            if(isNamePropertyChange(event))
                setDisplayName(event.getNewValue());
        }
        
        private boolean isNamePropertyChange(ProjectPropertyChangedEvent event) {
            return ProjectBaseCustomizerPanel.BASE_CONFIG.equals(event.getConfigName()) &&
                   ProjectBaseCustomizerPanel.NAME_PROP.equals(event.getProperty());
        }
    }
    
}
