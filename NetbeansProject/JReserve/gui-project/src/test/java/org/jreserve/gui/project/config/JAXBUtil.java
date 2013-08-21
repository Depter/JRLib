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
package org.jreserve.gui.project.config;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class JAXBUtil {
    
    private final static String HEADER = "<?xml version=\"1.0\"?>";
    
    static String marshall(Object o) throws JAXBException {
        return marshall(o, o.getClass());
    }
    
    static String marshall(Object o, Class clazz) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(clazz);
        Marshaller m = ctx.createMarshaller();
        
        StringWriter writer = new StringWriter();
        m.marshal(o, writer);
        String result = writer.toString();
        
        int begin = result.indexOf("?>");
        if(begin == 0)
            return result;
        return result.substring(begin+2);
    }
    
    static <T> T unmarshall(String xml, Class<T> clazz) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(clazz);
        Unmarshaller m = ctx.createUnmarshaller();
        
        StringReader reader = new StringReader(HEADER+xml);
        return (T) m.unmarshal(reader);
    }
}
