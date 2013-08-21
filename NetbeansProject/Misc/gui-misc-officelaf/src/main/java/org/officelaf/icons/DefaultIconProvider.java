package org.officelaf.icons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;

/**
 *
 * @author mikael
 */
public class DefaultIconProvider extends AbstractIconProvider {
    protected Map<String, String> namePath;
    protected Class               resourceLoader;
    
    public DefaultIconProvider() {
        init();
    }
    
    protected void init() {
        if(namePath == null) {
            namePath = new HashMap<String, String>();
            resourceLoader = DefaultIconProvider.class;

            List<String> paths = getPaths(resourceLoader, "/com/exie/icons/images/16x16");
            for(String path : paths) {
                namePath.put(path.substring(path.lastIndexOf('/')+1),path);
            }
        }
    }

    @Override
    public Set<String> getNames() {
        return namePath.keySet();
    }

    @Override
    public Icon getIcon(String name) {
        return super.getIcon(name);
    }
}
