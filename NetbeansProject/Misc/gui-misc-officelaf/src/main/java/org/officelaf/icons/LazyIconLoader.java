package org.officelaf.icons;

import java.lang.ref.SoftReference;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author mikael
 */
public class LazyIconLoader implements LazyLoader<Icon> {

    SoftReference<Icon> icon;
    Class loader;
    String path;

    public LazyIconLoader(Class loader, String path) {
        this.loader = loader;
        this.path = path;
    }



    @Override
    public Icon getResource() {
        Icon retVal = null;

        if(icon == null || (retVal = icon.get()) == null) {
            retVal = new ImageIcon(loader.getResource(path));
            icon = new SoftReference<Icon>(retVal);
        }

        return retVal;
    }

}
