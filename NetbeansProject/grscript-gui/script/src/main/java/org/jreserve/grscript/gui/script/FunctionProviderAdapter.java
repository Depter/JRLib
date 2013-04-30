package org.jreserve.grscript.gui.script;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.grscript.FunctionProvider;
import org.jreserve.grscript.gui.script.registration.FunctionProviderAdapterRegistrationProcessor;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FunctionProviderAdapter {

    public String getName();

    public FunctionProvider getFunctionProvider();

    public List<String> getFunctionSignitures();

    public String getFunctionDescription(String signiture);

    public static @interface Registration {

        public String path();

        public int position() default Integer.MAX_VALUE;
    }

}
