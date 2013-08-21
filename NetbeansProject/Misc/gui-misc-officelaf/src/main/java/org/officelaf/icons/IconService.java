package org.officelaf.icons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Set;
import org.officelaf.spi.IconProvider;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.Lookup.Template;

/**
 *
 * @author mikael
 */
public class IconService implements IconProvider {
    private static IconService service;
    private Lookup iconLookup;
    private Collection<? extends IconProvider> providers;
    private Template<IconProvider> providerTemplate;
    private Result<IconProvider>   providerResult;
    
    private IconService() {
        iconLookup       = Lookup.getDefault();
        providerTemplate = new Template<IconProvider>(IconProvider.class);
        providerResult   = iconLookup.lookup(providerTemplate);
        providers        = providerResult.allInstances();
    }
    
    public static synchronized IconService getInstance() {
        if(service == null) {
            service = new IconService();
        }
        
        return service;
    }

    public Collection<? extends IconProvider> getIconProviders() {
        return providers;
    }

    public Icon getIcon(String name) {
        Icon retVal = null;
        
        for(IconProvider provider : providers) {
            retVal = provider.getIcon(name);
            if(retVal != null) break;
        }
        
        if(retVal == null) {
            System.out.printf("IconService.getIcon(%s) failed to load: \n", name);
            retVal = createDefaultIcon(16);
        }
        
        return retVal;
    }

    public Icon getIcon(String name, int size) {
        Icon retVal = null;
        
        for(IconProvider provider : providers) {
            retVal = provider.getIcon(name, size);
            if(retVal != null) break;
        }
        
        if(retVal == null) {
            System.out.printf("IconService.getIcon(%s,%d) failed to load: \n", name,size);
            retVal = createDefaultIcon(16);
        }

        return retVal;
    }

    @Override
    public Set<Dimension> getDimensions(String name) {
        Set<Dimension> retVal = new HashSet<Dimension>();
        
        for(IconProvider provider : providers) {
            retVal.addAll(provider.getDimensions(name));
        }
        
        return retVal;
    }

    @Override
    public boolean isScalable(String name) {
        boolean retVal = false;

        for(IconProvider provider : providers) {
            if(provider.hasIcon(name)) {
                retVal = provider.isScalable(name);
                break;
            }
        }

        return retVal;
    }

    @Override
    public boolean hasIcon(String name, int size) {
        boolean retVal = false;

        for(IconProvider provider : providers) {
            if(provider.hasIcon(name,size)) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }

    @Override
    public boolean hasIcon(String name) {
        boolean retVal = false;

        for(IconProvider provider : providers) {
            if(provider.hasIcon(name)) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }

    @Override
    public Set<String> getNames() {
        HashSet<String> retVal = new HashSet<String>();
        
        for(IconProvider provider : providers) {
            retVal.addAll(provider.getNames());
        }
        
        return retVal;
    }
    
    public static Icon createDefaultIcon(int size) {
       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        BufferedImage dummy = ge.getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(size, size);
        Graphics2D g2d = dummy.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0,0,size-1, size-1);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Error", 2, 2);
        g2d.dispose();

        return new ImageIcon(dummy);
    }
}
