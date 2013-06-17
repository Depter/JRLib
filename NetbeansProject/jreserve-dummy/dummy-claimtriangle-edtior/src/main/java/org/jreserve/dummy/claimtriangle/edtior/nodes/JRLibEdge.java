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
package org.jreserve.dummy.claimtriangle.edtior.nodes;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibEdge extends JRLibObject {
    
    private JRLibNode source;
    private JRLibNode target;
    
    public JRLibEdge(JRLibNode source, JRLibNode target) {
        this.source = source;
        this.target = target;
    }

    public JRLibNode getSource() {
        return source;
    }

    public JRLibNode getTarget() {
        return target;
    }
    
    @Override
    public String toString() {
        return String.format("JRLibEdge [%d; %s -> %s]", id, source, target);
    }
}
