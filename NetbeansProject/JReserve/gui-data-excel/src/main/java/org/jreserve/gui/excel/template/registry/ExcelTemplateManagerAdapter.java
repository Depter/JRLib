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
package org.jreserve.gui.excel.template.registry;

import javax.swing.Icon;
import org.jreserve.gui.excel.template.ExcelTemplateManager;
import org.jreserve.gui.misc.utils.widgets.Displayable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExcelTemplateManagerAdapter implements Displayable {
    
    private final ExcelTemplateManager delegate;
    private final String name;
    private final Icon icon;
    
    ExcelTemplateManagerAdapter(ExcelTemplateManager delegate, String name, Icon icon) {
        this.delegate = delegate;
        this.name = name;
        this.icon  = icon;
    }
    
    @Override
    public Icon getIcon() {
        return icon;
    }
    
    @Override
    public String getDisplayName() {
        return name;
    }
    
    public ExcelTemplateManager getDelegate() {
        return delegate;
    }
    
}
