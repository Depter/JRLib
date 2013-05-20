package org.jreserve.grscript.gui.script.functions;

import java.util.List;
import org.jreserve.grscript.FunctionProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FunctionProviderAdapter {

    public String getName();

    public FunctionProvider getFunctionProvider();

    public List<String> getFunctionSignitures();
    
    public String getFunctionHelpId(String function);
    
    public List<String> getPropertyNames();
    
    public String getPropertyHelpId(String property);
    
    public static @interface Registration {

        public String path();
        
        public int position() default Integer.MAX_VALUE;
    }

}
