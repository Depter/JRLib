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

package org.jreserve.gui.misc.namedcontent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractNamedContentProvider implements NamedContentProvider {
    
    private final static Logger logger = Logger.getLogger(AbstractNamedContentProvider.class.getName());
    
    private final String rootPath;
    private FileObject projectDir;
    private List<NamedContent> contents;
    
    protected AbstractNamedContentProvider(Project project, String rootPath) {
        this.projectDir = project.getProjectDirectory();
        this.rootPath = rootPath;
    }
    
    @Override
    public synchronized FileObject getRoot() {
        if(projectDir == null || !projectDir.isValid())
            return null;
        return projectDir.getFileObject(rootPath);
    }
    
    @Override
    public synchronized List<NamedContent> getContents() {
        if(contents == null)
            loadContents();
        return new ArrayList<NamedContent>(contents);
    }

    private void loadContents() {
        contents = new ArrayList<NamedContent>();
        if(projectDir == null) {
            logger.log(Level.WARNING, "Unable to load NamedContents! Project directory is null!");
            return;
        }
        
        if(!projectDir.isValid()) {
            logger.log(Level.WARNING, "Unable to load NamedContents! Project directory is invalid!");
            return;
        }
        
        FileObject dataFolder = projectDir.getFileObject(rootPath);
        if(dataFolder == null) {
            String msg = "Unable to load NamedContents! Path '%s' not found in directory '%s'!";
            logger.log(Level.WARNING, String.format(msg, rootPath, projectDir.getPath()));
            return;
        }
        
        try {
            DataObject obj = DataObject.find(dataFolder);
            if(obj instanceof DataFolder) {
                for(DataObject child : ((DataFolder)obj).getChildren())
                    contents.add(new NamedDataObjectContent(child));
            } else {
                contents.add(new NamedDataObjectContent(obj));
            }
        } catch(DataObjectNotFoundException ex) {
            String msg = "Unable to load NamedContents! Path '%s' is not a DataObject!";
            logger.log(Level.WARNING, String.format(msg, dataFolder.getPath()));
        } catch(Exception ex) {
            String msg = "Unexpected exception while loading contents from path '%s'!";
            logger.log(Level.WARNING, String.format(msg, dataFolder.getPath()), ex);
        }
    }
}
