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
package org.jreserve.grscript.gui.classpath.explorer.nodes;

import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.CpRootType.Platform=Platform",
    "CTL.CpRootType.Module=Modules",
    "CTL.CpRootType.Custom=Custom"
})
public enum CpRootType {
    PLATFORM (Bundle.CTL_CpRootType_Platform()),
    MODULES  (Bundle.CTL_CpRootType_Module()),
    CUSTOM   (Bundle.CTL_CpRootType_Custom());
    
    private final String userName;
    
    private CpRootType(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
}
