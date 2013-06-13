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
package org.jreserve.dummy.claimtriangle.edtior.scene;

import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class JRLibScene extends GraphScene<JRLibScene.JRLibNode, Object> {

    @Override
    protected Widget attachNodeWidget(JRLibNode n) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Widget attachEdgeWidget(Object e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void attachEdgeSourceAnchor(Object e, JRLibNode n, JRLibNode n1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void attachEdgeTargetAnchor(Object e, JRLibNode n, JRLibNode n1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    static class JRLibNode implements Comparable {
        
        private static int ID = 0;
        
        private final int id = ID++;
        private Object content;

        public JRLibNode(Object content) {
            this.content = content;
        }
        
        @Override
        public int compareTo(Object o) {
            if(o instanceof JRLibNode) 
                return id - ((JRLibNode)o).id;
            return -1;
        }
        
        @Override
        public boolean equals(Object o) {
            return this==o? true : compareTo(o) == 0;
        }
        
        @Override
        public int hashCode() {
            return id;
        }
    }
    
    static class JRLibEdge implements Comparable {
        
        private static int ID = 0;
        
        private final int id = ID++;
        private JRLibNode from;
        private JRLibNode to;
        
        private JRLibEdge(JRLibNode from, JRLibEdge to) {}
        
        @Override
        public int compareTo(Object o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
    }
    
    private class JRLibWidget extends Widget {

        public JRLibWidget(Scene scene) {
            super(scene);
        }
    
    }
    
}
