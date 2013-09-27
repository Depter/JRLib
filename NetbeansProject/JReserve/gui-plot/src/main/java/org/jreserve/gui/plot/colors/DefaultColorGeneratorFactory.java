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

import org.jreserve.gui.plot.ColorGenerator;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.DefaultColorGenerator.Name=Default"
})
public class DefaultColorGeneratorFactory implements ColorGenerator.Factory {

    @Override
    public ColorGenerator createColorGenerator() {
        return new DefaultColorGenerator();
    }
    
}
