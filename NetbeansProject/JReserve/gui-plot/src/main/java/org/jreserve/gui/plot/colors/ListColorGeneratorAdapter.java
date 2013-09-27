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
package org.jreserve.gui.plot.colors;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.AnnotationUtils;
import org.jreserve.gui.plot.ColorGenerator;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ListColorGeneratorAdapter extends AbstractColorGeneratorAdapter {
    
    private final static Logger logger = Logger.getLogger(ListColorGeneratorAdapter.class.getName());
    private final Color[] colors;
    
    ListColorGeneratorAdapter(FileObject fo) {
        super(fo);
        
        String cList = AnnotationUtils.stringAttribute(ListColorGeneratorRegistrationProcessor.COLORS, fo);
        if(cList==null || cList.length()==0) {
            String msg = "Invalid color list for color generator '%s'!";
            logger.warning(String.format(msg, super.getId()));
            colors = new Color[]{Color.BLACK};
        } else {
            colors = createColors(cList.split(""+ListColorGeneratorRegistrationProcessor.COLOR_SEPARATOR));
        }
    }
    
    private Color[] createColors(String[] names) {
        List<Color> result = new LinkedList<Color>();
        for(String name : names) {
            Color color = createColor(name);
            if(color != null)
                result.add(color);
        }
        
        if(result.isEmpty())
            return new Color[]{Color.BLACK};
        return result.toArray(new Color[result.size()]);
    }
    
    private Color createColor(String color) {
        Color result = ColorUtil.parseColor(color);
        
        if(result == null) {
            String msg = "Unable to parse hexadecimal color from '%s', within color generator '%s'!";
            msg = String.format(msg, color, super.getId());
            logger.log(Level.WARNING, msg);
        }
        
        return result;
    }

    @Override
    public ColorGenerator createColorGenerator() {
        return new ListColorGenerator(colors);
    }
}
