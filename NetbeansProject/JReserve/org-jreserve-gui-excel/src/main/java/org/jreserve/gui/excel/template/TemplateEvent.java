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

package org.jreserve.gui.excel.template;

import org.jreserve.gui.misc.eventbus.EventBusManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TemplateEvent {
    
    public static void publishCreated(ExcelTemplate template) {
        publish(new TemplateCreatedEvent(template));
    }
    
    private static void publish(TemplateEvent evt) {
        EventBusManager.getDefault().publish(evt);
    }
    
    public static void publishDeleted(ExcelTemplateManager manager, ExcelTemplate template) {
        publish(new TemplateDeletedEvent(manager, template));
    }
    
    private final ExcelTemplateManager manager;
    private final ExcelTemplate template;

    private TemplateEvent(ExcelTemplateManager manager, ExcelTemplate template) {
        this.manager = manager;
        this.template = template;
    }

    public ExcelTemplateManager getManager() {
        return manager;
    }

    public ExcelTemplate getTemplate() {
        return template;
    }
    
    public static class TemplateCreatedEvent extends TemplateEvent {
        private TemplateCreatedEvent(ExcelTemplate template) {
            super(template.getManager(), template);
        }
    }
    
    public static class TemplateDeletedEvent extends TemplateEvent {
        private final String name;
        
        private TemplateDeletedEvent(ExcelTemplateManager manager, ExcelTemplate template) {
            super(manager, template);
            this.name = template.getName();
        }
        
        public String getName() {
            return name;
        }
    }
}
