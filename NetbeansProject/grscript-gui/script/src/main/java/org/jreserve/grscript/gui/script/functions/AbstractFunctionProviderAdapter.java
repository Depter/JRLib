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
package org.jreserve.grscript.gui.script.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jreserve.grscript.FunctionProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractFunctionProviderAdapter implements FunctionProviderAdapter {
//    
//    protected static String surroundHtml(String str) {
//        return "<html><head></head><body>"+escapeString(str)+"</body></html>";
//    }
//    
//    private static String escapeString(String str) {
//        str = str.replaceAll("'", "&quot;");
//        str = str.replaceAll("\"", "&quot;");
//        str = str.replaceAll("<", "&lt;");
//        str = str.replaceAll(">", "&gt;");
//        return str;
//    }
    
    private FunctionProvider provider;
    private List<String> functions;
    private List<String> properties;
    
    @Override
    public synchronized FunctionProvider getFunctionProvider() {
        if(provider == null)
            provider = createFunctionProvider();
        return provider;
    }

    protected abstract FunctionProvider createFunctionProvider();
    
    @Override
    public synchronized List<String> getFunctionSignitures() {
        if(functions == null) {
            functions = new ArrayList<String>();
            initFunctions(functions);
        }
        return functions;
    }
    
    protected abstract void initFunctions(List<String> functions);
    
//    @Override
//    public synchronized String getFunctionDescription(String signiture) {
//        String desc = getFunctions().get(signiture);
//        return desc == null? "" : desc;
//    }

    @Override
    public List<String> getPropertyNames() {
        if(properties == null) {
            properties = new ArrayList<String>();
            initProperties(properties);
        }
        return properties;
    }
    
    protected abstract void initProperties(List<String> properies);
//    
//    @Override
//    public String getPropertyDescription(String name) {
//        String desc = getProperties().get(name);
//        return desc==null? "" : desc;
//    }
}
