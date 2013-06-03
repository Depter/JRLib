package org.jreserve.gui.misc.officelaf.icons;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.swing.Icon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.jreserve.gui.misc.officelaf.spi.IconProvider;

/**
 *
 * @author mikael
 */
public abstract class AbstractIconProvider implements IconProvider {
    protected Map<String,Map<Dimension,Icon>> icons;
    protected int defaultSize = 16;
    
    public AbstractIconProvider() {
        icons = new TreeMap<String,Map<Dimension,Icon>>();
    }



    @Override
    public Icon getIcon(String name) {
        return getIcon(name,defaultSize);
    }

    @Override
    public Icon getIcon(String name, int size) {
        Icon retVal = null;

        Map<Dimension,Icon> icon = icons.get(name);
        if(icon != null && !icon.isEmpty()) {
            retVal = icon.get(new Dimension(size, size));
        }
        
        return retVal;
    }
    
    protected Icon addIcon(String name, int size, Icon icon) {
        Map<Dimension,Icon> imap = icons.get(name);

        if(imap == null) {
            imap = new HashMap<Dimension,Icon>();
            icons.put(name, imap);
        }

        imap.put(new Dimension(size,size), icon);

        return icon;
    }

    @Override
    public Set<Dimension> getDimensions(String name) {
        Set<Dimension> retVal = null;

        Map<Dimension,Icon> icon = icons.get(name);
        if(icon != null && !icon.isEmpty()) {
            retVal = icon.keySet();
        }

        return retVal;
    }

    @Override
    public boolean isScalable(String name) {
        boolean retVal = false;
        
        Icon icon = getIcon(name);
        if(icon instanceof ExtendedIcon)
            retVal = ((ExtendedIcon)icon).isScaleable();
        else if(icon instanceof ResizableIcon) 
            retVal = true;
            
        return retVal;
    }


    public String getName(Icon icon) {
        String retVal = null;

        if(icon instanceof ExtendedIcon) {
            retVal = ((ExtendedIcon)icon).getName();
        } else {
            for(Entry<String,Map<Dimension,Icon>> entry : icons.entrySet()) {
                if(entry.getValue().containsValue(icon)) {
                    retVal = entry.getKey();
                }
            }
        }

        return retVal;
    }

    public int size() {
        int retVal = 0;

        for(Map<Dimension,Icon> map : icons.values()) {
            retVal += map.size();
        }
        
        return retVal;
    }

    public Iterator<Icon> iterator() {
        ArrayList retVal = new ArrayList<Icon>(size());
        
        for(Map<Dimension,Icon> map : icons.values()) {
            retVal.addAll(map.values());
        }
                
        return retVal.iterator();
    }

    @Override
    public boolean hasIcon(String name) {
        return getIcon(name) != null;
    }

    @Override
    public boolean hasIcon(String name, int size) {
        return getIcon(name,size) != null;
    }

    protected static List<String> getPaths(Class loader, String path) {
        List<String> retVal = new ArrayList<String>();

        if(path.charAt(path.length()-1) != '/')
            path += '/';
        
        URL url = loader.getResource(path);
        if (url != null) {
            try {
                /*System.out.println("SURL:        " + url);
                System.out.println("Authority:   " + url.getAuthority());
                System.out.println("DefaultPort: " + url.getDefaultPort());
                System.out.println("File:        " + url.getFile());
                System.out.println("Host:        " + url.getHost());
                System.out.println("Path:        " + url.getPath());
                System.out.println("Port:        " + url.getPort());
                System.out.println("Protocol:    " + url.getProtocol());
                System.out.println("Query:       " + url.getQuery());
                System.out.println("Ref:         " + url.getRef());
                System.out.println("UserInfo:    " + url.getUserInfo());
                System.out.println("ExternalForm:" + url.toExternalForm());*/
                String protocol = url.getProtocol();

                if ("file".compareTo(protocol) == 0) {
                    File file = new File(url.toURI());
                    if (file.isDirectory()) {
                        String[] children = file.list();
                        for (String name : children) {
                            retVal.add(path + name);
                        }
                    }
                } else if ("jar".compareTo(protocol) == 0 || "nbjcl".compareTo(protocol) == 0) {
                    String urlPath = url.getPath();
                    url = new URL(urlPath.substring(0, urlPath.indexOf('!')));
                    //System.out.println("\nJAR URL = '" + url.toString() + "'");

                    if (path.charAt(0) == '/') {
                        path = path.substring(1);
                    }

                    JarInputStream jis = new JarInputStream(url.openStream());
                    JarEntry ze;
                    while ((ze = jis.getNextJarEntry()) != null) {
                        String name = ze.getName();
                        if (name.startsWith(path) && name.charAt(name.length() - 1) != '/') {
                            retVal.add('/' + name);
                        }
                    }
                    jis.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }    
}
