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
package org.jreserve.gui.excel.poiutil.xlsx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.gui.excel.poiutil.ReferenceUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XlsxReferenceUtilReader extends XlsxWorkbookReader<ReferenceUtil> {
    private final static String NAME_TAG = "definedName";
    private final static String SHEET_TAG = "sheet";
    private final static String NAME_ATTR = "name";
    
    private List<String> sheets = new ArrayList<String>();
    private Map<String, String> names = new TreeMap<String, String>();
    
    private StringBuilder sb = new StringBuilder();
    private boolean withinName;
    private String name;
    
    @Override
    protected ReferenceUtil getResult() {
        return new ReferenceUtil(sheets, names, false);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(NAME_TAG.equals(qName)) {
            name = attributes.getValue(NAME_ATTR);
            withinName = true;
        } else if(SHEET_TAG.equals(qName)) {
            withinName = false;
            addName(attributes, sheets);
        } else {
            withinName = false;
        }
    }
    
    private void addName(Attributes attr, List<String> list) {
        String n = attr.getValue(NAME_ATTR);
        if(n!=null && n.length()>0)
            list.add(n);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(withinName) {
            sb.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(NAME_TAG.equals(qName)) {
            String formula = sb.toString();
            if(name!=null && name.length()>0 && formula != null && formula.length()>0)
                names.put(name, formula);
            
            withinName = false;
            sb.setLength(0);
            name = null;
        }
    }
}
