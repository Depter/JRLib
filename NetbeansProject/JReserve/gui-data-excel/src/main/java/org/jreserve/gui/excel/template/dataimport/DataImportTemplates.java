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
package org.jreserve.gui.excel.template.dataimport;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.excel.template.TemplateEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataImportTemplates implements ExcelTemplateManager<DataImportTemplate> {
    
    private final static Logger logger = Logger.getLogger(DataImportTemplates.class.getName());
    private final static String PATH = "Excel/Templates/DataImport";    //NOI18
    
    private static DataImportTemplates INSTANCE;
    
    public synchronized static DataImportTemplates getDefault() {
        if(INSTANCE == null)
            INSTANCE = new DataImportTemplates();
        return INSTANCE;
    }
    
    private final FileObject root;
    private Lookup lkp;
    private List<DataImportTemplate> templates;
    
    private DataImportTemplates() {
        root = initRoot();
    }
    
    private FileObject initRoot() {
        FileObject folder = FileUtil.getConfigFile(PATH);
        if(folder == null) {
            folder = FileUtil.getConfigRoot();
            for(String name : PATH.split("/"))
                folder = folder==null? null : createFolder(folder, name);
        }
        return folder;
    }
    
    private FileObject createFolder(FileObject parent, String name) {
        FileObject child = parent.getFileObject(name, null);
        if(child != null && child.isFolder())
            return child;
        
        try {
            return parent.createFolder(name);
        } catch (IOException ex) {
            String msg = String.format("Unable to create folder '%s' is '%s'!", name, parent.getPath());
            logger.log(Level.SEVERE, msg, ex);
            return null;
        }
    }

    @Override
    public synchronized List<DataImportTemplate> getTemplates() {
        if(templates == null)
            templates = (root==null)? 
                    Collections.EMPTY_LIST : 
                    TemplateLoader.loadTemplates(this, root);
        return templates;
    }
    
    @Override
    public Lookup getLookup() {
        if(lkp == null)
            lkp = Lookups.fixed(this, new DataImportTemplateBuilder(this));
        return lkp;
    }
    
    public synchronized DataImportTemplate createTemplate(String name, List<DataImportTemplateItem> items) throws IOException {
        checkNewName(name);
        FileObject file = getTemplateFile(name);
        DataImportTemplate template = new DataImportTemplate(file, this, items);
        TemplateLoader.save(template);
        templates.add(template);
        Collections.sort(templates);
        logger.log(Level.FINE, "Create DataImpotTemplate ''{0}''.", name);
        TemplateEvent.publishCreated(template);
        return template;
    }
    
    private void checkNewName(String name) {
        if(name == null)
            throw new NullPointerException("Name is null!");
        if(name.length() == 0)
            throw new IllegalArgumentException("Name is empty string!");
        for(DataImportTemplate template : getTemplates())
            if(name.equalsIgnoreCase(template.getName()))
                throw new IllegalArgumentException("Name '"+name+"' already exists!");
    }
    
    private FileObject getTemplateFile(String name) throws IOException {
        if(root == null)
            throw new IOException("RootFolder not found!");
        return root.createData(name, "xml");
    }
    
    @Override
    public synchronized void deleteTemplate(final DataImportTemplate template) {
        synchronized(template) {
            if(template == null)
                throw new NullPointerException("Template is null!");
            if(this != template.getManager())
                throw new IllegalArgumentException("Template belongs to another manager!");
        
            String name = template.getName();
            try {
                templates.remove(template);
                template.getFile().delete();
                logger.log(Level.INFO, "Deleted template ''{0}''.", name);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Unable to delete template: "+name, ex);
            }
        }
        TemplateEvent.publishDeleted(this, template);
    }
    
    public synchronized void renameTemplate(DataImportTemplate template, String newName) throws IOException {
        String oldName = null;
        try {
            oldName = template.getName();
            checkTempalte(template);
            checkNewName(newName);
            template.renameFile(newName);
            TemplateEvent.publishRenamed(template, oldName);
        } catch (Exception ex) {
            String msg = String.format("Unable to rename temaplate '%s' to '%s'!", oldName, newName);
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    private void checkTempalte(DataImportTemplate template) {
        if(template == null)
            throw new NullPointerException("Template is null!");
        if(this != template.getManager())
            throw new IllegalArgumentException("Template belongs to another manager!");
    }
    
}
