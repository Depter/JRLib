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
package org.jreserve.gui.poi.read.xlsx;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XlsxNameReader extends XlsxWorkbookReader<List<String>>{

    private final static String NAME_TAG = "definedName";
    private final static String NAME_ATTR = "name";
    private final List<String> names = new ArrayList<String>();
    
    @Override
    protected List<String> getResult() {
        return names;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(NAME_TAG.equals(qName))
            addName(attributes);
    }
    
    private void addName(Attributes attr) {
        String name = attr.getValue(NAME_ATTR);
        if(name!=null && name.length()>0)
            names.add(name);
    }
    
    
}
