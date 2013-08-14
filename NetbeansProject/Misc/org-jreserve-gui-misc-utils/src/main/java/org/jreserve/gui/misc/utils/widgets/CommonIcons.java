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

package org.jreserve.gui.misc.utils.widgets;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CommonIcons {
    
    @StaticResource private final static String ADD = "org/jreserve/gui/misc/utils/add.png";
    @StaticResource private final static String ARROW_DOWN = "org/jreserve/gui/misc/utils/arrow_down.png";
    @StaticResource private final static String ARROW_UP = "org/jreserve/gui/misc/utils/arrow_up.png";
    @StaticResource private final static String CANCEL = "org/jreserve/gui/misc/utils/cancel.png";
    @StaticResource private final static String DELETE = "org/jreserve/gui/misc/utils/delete.png";
    @StaticResource private final static String EDIT = "org/jreserve/gui/misc/utils/edit.png";
    @StaticResource private final static String OK = "org/jreserve/gui/misc/utils/ok.png";
    @StaticResource private final static String REFRESH = "org/jreserve/gui/misc/utils/refresh.png";
    @StaticResource private final static String SEARCH = "org/jreserve/gui/misc/utils/search.png";
    
    private final static Map<String, ImageIcon> cache = new HashMap<String, ImageIcon>(9);
    
    public static ImageIcon add() {
        return getIcon(ADD);
    }
    
    private static ImageIcon getIcon(String path) {
        ImageIcon icon = cache.get(path);
        if(icon == null) {
            icon = ImageUtilities.loadImageIcon(path, false);
            cache.put(path, icon);
        }
        return icon;
    }
    
    public static ImageIcon arrowDown() {
        return getIcon(ARROW_DOWN);
    }
    
    public static ImageIcon arrowUp() {
        return getIcon(ARROW_UP);
    }
    
    public static ImageIcon cancel() {
        return getIcon(CANCEL);
    }
    
    public static ImageIcon delete() {
        return getIcon(DELETE);
    }
    
    public static ImageIcon edit() {
        return getIcon(EDIT);
    }
    
    public static ImageIcon ok() {
        return getIcon(OK);
    }
    
    public static ImageIcon refresh() {
        return getIcon(REFRESH);
    }
    
    public static ImageIcon search() {
        return getIcon(SEARCH);
    }
    
    private CommonIcons() {}
}
