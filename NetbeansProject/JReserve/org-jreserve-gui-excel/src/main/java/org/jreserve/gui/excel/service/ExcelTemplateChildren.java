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
package org.jreserve.gui.excel.service;

import java.util.List;
import org.jreserve.gui.excel.template.ExcelTemplate;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ExcelTemplateChildren extends ChildFactory<ExcelTemplate> {

    private final ExcelTemplateManager manager;

    ExcelTemplateChildren(ExcelTemplateManager manager) {
        this.manager = manager;
    }

    @Override
    protected boolean createKeys(List<ExcelTemplate> toPopulate) {
        toPopulate.addAll(manager.getTemplates());
        return true;
    }

    @Override
    protected Node createNodeForKey(ExcelTemplate key) {
        return new ExcelTemplateNode(key);
    }
}
