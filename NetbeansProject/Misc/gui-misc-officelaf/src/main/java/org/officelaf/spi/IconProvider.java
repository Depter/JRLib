package org.officelaf.spi;

import java.awt.Dimension;
import java.util.Set;
import javax.swing.Icon;

/**
 *
 * @author mikael
 */
public interface IconProvider {
    Icon getIcon(String name);
    Icon getIcon(String name, int size);
    Set<Dimension> getDimensions(String name);
    boolean isScalable(String name);
    boolean hasIcon(String name);
    boolean hasIcon(String name, int size);
    Set<String> getNames();
}
