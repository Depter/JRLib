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
package org.jreserve.gui.misc.utils.dataobject;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractDataObjectProvider implements DataObjectProvider {
    
    private final static Logger logger = Logger.getLogger(AbstractDataObjectProvider.class.getName());
    protected final Project project;

    protected AbstractDataObjectProvider(Project project) {
        this.project = project;
    }
    
    @Override
    public DataFolder getRootFolder() {
        FileObject folder = project.getProjectDirectory();
        folder = folder.getFileObject(getRootName());
        if(folder == null || !folder.isFolder()) {
            String msg = "Can not find root folder '%s' in project directory '%s'!";
            logger.log(Level.WARNING, String.format(msg, getRootName(), project.getProjectDirectory().getPath()));
            return null;
        }
        
        try {
            DataObject obj = DataObject.find(folder);
            if(obj instanceof DataFolder)
                return (DataFolder) obj;
            String msg = "Expected to load DataFolder from file '%s', but loaded '%s' instead!";
            logger.log(Level.WARNING, String.format(msg, folder.getPath(), obj.toString()));
            return null;
        } catch (Exception ex) {
            String msg = "Can not load DataFolder from file '%s'!";
            logger.log(Level.WARNING, String.format(msg, folder.getPath()), ex);
            return null;
        }
    }

    @Override
    public DataFolder createFolder(String path) throws IOException {
        FileObject folder = getRootFolder().getPrimaryFile();
        
        StringTokenizer tokenizer = new StringTokenizer(path, "/", false);
        while(tokenizer.hasMoreTokens()) {
            String name = tokenizer.nextToken();
            try {
                folder = FileUtil.createFolder(folder, name);
            } catch (IOException ex) {
                String msg = "Unable to create folder %s/%s!";
                logger.log(Level.SEVERE, String.format(msg, folder.getPath(), name), ex);
                throw ex;
            }
        }
        
        try {
            return (DataFolder) DataObject.find(folder);
        } catch (Exception ex) {
            String msg = "Can not load DataFolder from file '%s'!";
            logger.log(Level.WARNING, String.format(msg, folder.getPath()), ex);
            throw new IOException("", ex);
        }
    }
    
    protected abstract String getRootName();
}
