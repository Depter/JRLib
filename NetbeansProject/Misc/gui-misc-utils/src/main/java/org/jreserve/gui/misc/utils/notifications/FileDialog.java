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
package org.jreserve.gui.misc.utils.notifications;

import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.filechooser.FileFilter;
import org.openide.filesystems.FileChooserBuilder;


/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FileDialog {
    
    private final static EmptyBadgeProvider BADGE_PROVIDER = new EmptyBadgeProvider();
    
    public static File[] openDirectories(FileFilter selectedFilter, String title) {
        return openDirectories(null, selectedFilter, title);
    }
    
    public static File[] openDirectories(Class clazz, FileFilter selectedFilter) {
        return openDirectories(clazz, selectedFilter, null);
    }
    
    public static File[] openDirectories(Class clazz, FileFilter selectedFilter, String title) {
        List<FileFilter> filters = getFilterList(selectedFilter);
        return openDirectories(clazz, filters, selectedFilter, title);
    }
    
    public static File[] openDirectories(Class clazz, List<FileFilter> extraFilters, FileFilter selectedFilter, String title) {
        return initFcb(clazz, title, extraFilters, selectedFilter)
                .setDirectoriesOnly(true).showMultiOpenDialog();
    }
    
    public static File openDirectory(FileFilter selectedFilter, String title) {
        return openDirectory(null, selectedFilter, title);
    }
    
    public static File openDirectory(Class clazz, FileFilter selectedFilter) {
        return openDirectory(clazz, selectedFilter, null);
    }
    
    public static File openDirectory(Class clazz, FileFilter selectedFilter, String title) {
        List<FileFilter> filters = getFilterList(selectedFilter);
        return openDirectory(clazz, filters, selectedFilter, title);
    }
    
    public static File openDirectory(Class clazz, List<FileFilter> extraFilters, FileFilter selectedFilter, String title) {
        return initFcb(clazz, title, extraFilters, selectedFilter)
                .setDirectoriesOnly(true).showOpenDialog();
    }
    
    public static File[] openFiles(FileFilter selectedFilter, String title) {
        return openFiles(null, selectedFilter, title);
    }
    
    public static File[] openFiles(Class clazz, FileFilter selectedFilter) {
        return openFiles(clazz, selectedFilter, null);
    }
    
    public static File[] openFiles(Class clazz, FileFilter selectedFilter, String title) {
        List<FileFilter> filters = getFilterList(selectedFilter);
        return openFiles(clazz, filters, selectedFilter, title);
    }
    
    public static File[] openFiles(Class clazz, List<FileFilter> extraFilters, FileFilter selectedFilter, String title) {
        return initFcb(clazz, title, extraFilters, selectedFilter)
                .setFilesOnly(true).showMultiOpenDialog();
    }
    
    public static File openFile(FileFilter selectedFilter, String title) {
        return openFile(null, selectedFilter, title);
    }
    
    public static File openFile(Class clazz, FileFilter selectedFilter) {
        return openFile(clazz, selectedFilter, null);
    }
    
    public static File openFile(Class clazz, FileFilter selectedFilter, String title) {
        List<FileFilter> filters = getFilterList(selectedFilter);
        return openFile(clazz, filters, selectedFilter, title);
    }
    
    private static List<FileFilter> getFilterList(FileFilter filter) {
        if(filter == null)
            return Collections.EMPTY_LIST;
        else
            return Collections.singletonList(filter);
    }
    
    public static File openFile(Class clazz, List<FileFilter> extraFilters, FileFilter selectedFilter, String title) {
        return initFcb(clazz, title, extraFilters, selectedFilter)
                .setFilesOnly(true).showOpenDialog();
    }
    
    private static FileChooserBuilder initFcb(Class clazz, String title, List<FileFilter> extraFilters, FileFilter selectedFilter) {
        FileChooserBuilder fcb = new FileChooserBuilder(clazz==null? FileDialog.class : clazz);
        fcb.setBadgeProvider(BADGE_PROVIDER);
        
        if(title != null)
            fcb.setTitle(title);
        
        if(extraFilters != null)
            for(FileFilter filter : extraFilters)
                fcb.addFileFilter(filter);
        
        if(selectedFilter != null)
            fcb.setFileFilter(selectedFilter);
        
        return fcb;
    }
    
    private FileDialog() {
    }
    
    private final static class EmptyBadgeProvider implements FileChooserBuilder.BadgeProvider {

        @Override
        public Icon getBadge(File file) {
            return null;
        }

        @Override
        public int getXOffset() {
            return 0;
        }

        @Override
        public int getYOffset() {
            return 0;
        }
    }
}
