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

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class JRLibNode extends JRLibObject {
    private String title;
    private Image img;
    private List<JRLibPin> pins = new ArrayList<JRLibPin>();
    
    private final DefaultPin defaultPin;
    
    public JRLibNode(String title, Image img) {
        this.title = title;
        this.img = img;
        this.defaultPin = new DefaultPin(this, title);
    }
    
    public JRLibPin getDefaultPin() {
        return defaultPin;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void addPin(JRLibPin pin) {
        pins.add(pin);
    }
    
    public List<JRLibPin> getPins() {
        return pins;
    }
    
    @Override
    public String toString() {
        return String.format("JRLibNode [%d; %s]", id, title);
    }
    
    private static class DefaultPin extends JRLibPin {

        public DefaultPin(JRLibNode node, String title) {
            super(node, title);
        }

        @Override
        public int getId() {
            return -getNode().getId();
        }
        
    }
}
