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

@OptionsPanelController.ContainerRegistration(
        categoryName = "#LBL.ImportDataSettings.Title",
        iconBase = "org/jreserve/gui/data/icons/import_data32.png",
        id = "ImportDataSettings",
        position = 600
)
@Messages({
    "LBL.ImportDataSettings.Title=Data Import"
})
package org.jreserve.gui.data.inport;

import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle.Messages;

