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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import org.jreserve.gui.misc.utils.notifications.BubbleUtil;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.TemplateLoader.LoadError=Unable to load template!",
    "# {0} - name",
    "MSG.TemplateLoader.SaveError=Unable to save template ''{0}''!"
})
class TemplateLoader {
    
    private final static Logger logger = Logger.getLogger(TemplateLoader.class.getName());
    
    private static JAXBContext JAXB_CTX;
    
    static void save(DataImportTemplate template) {
        FileObject file = template.getFile();
        try {
            TemplateContainer container = new TemplateContainer(template.getItems());
            Marshaller m = getContext().createMarshaller();
            m.marshal(container, FileUtil.toFile(file));
        } catch (Exception ex) {
            String msg = "Unable to save template to file: "+(file==null? null : file.getPath());
            logger.log(Level.SEVERE, msg, ex);
            BubbleUtil.showException(Bundle.MSG_TemplateLoader_SaveError(template.getName()), ex);
        }
    }
    
    static List<DataImportTemplate> loadTemplates(DataImportTemplates manager, FileObject root) {
        Enumeration<? extends FileObject> files = root.getData(false);
        List<DataImportTemplate> result = new ArrayList<DataImportTemplate>();
        
        while(files.hasMoreElements()) {
            FileObject file = files.nextElement();
            if(isTemplate(file))
                result.add(new DataImportTemplate(file, manager, loadItems(file)));
        }
        
        return result;
    }
    
    private static boolean isTemplate(FileObject file) {
        String ext = file.getExt();
        return ext!=null && ext.toLowerCase().equals("xml");
    }
    
    private static List<DataImportTemplateItem> loadItems(FileObject file) {
        try {
            Unmarshaller um = getContext().createUnmarshaller();
            TemplateContainer templates = (TemplateContainer) um.unmarshal(FileUtil.toFile(file));
            return templates.getItems();
        } catch (Exception ex) {
            String path = file==null? null : file.getPath();
            String msg = String.format("Unable to load temaple from file '%s'!", path);
            logger.log(Level.SEVERE, msg, ex);
            return new ArrayList<DataImportTemplateItem>();
        }
    }
    
    private static JAXBContext getContext() throws Exception {
        if(JAXB_CTX == null)
            JAXB_CTX = JAXBContext.newInstance(TemplateContainer.class);
        return JAXB_CTX;
    }
    
    private TemplateLoader() {}
    
    @XmlRootElement(name="dataImportTemplate")
    static class TemplateContainer {
        
        @XmlElement
        private List<DataImportTemplateItem> items;

        public TemplateContainer() {
        }

        public TemplateContainer(List<DataImportTemplateItem> items) {
            this.items = items;
        }

        public List<DataImportTemplateItem> getItems() {
            if(items == null)
                items = new ArrayList<DataImportTemplateItem>();
            return items;
        }
    }
}
