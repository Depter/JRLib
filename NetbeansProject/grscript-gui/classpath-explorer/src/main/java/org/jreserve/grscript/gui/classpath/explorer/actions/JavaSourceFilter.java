package org.jreserve.grscript.gui.classpath.explorer.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.jreserve.grscript.gui.classpath.ClassPathUtil;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.JavaSourceFilter.Description=Java binary (*.jar, *.class)"
})
class JavaSourceFilter extends FileFilter {

    private static JavaSourceFilter INSTANCE;
    
    static JavaSourceFilter getInstance() {
        if(INSTANCE == null)
            INSTANCE = new JavaSourceFilter();
        return INSTANCE;
    }
    
    private JavaSourceFilter() {
    }
    
    @Override
    public boolean accept(File f) {
        return f!=null &&
               (f.isDirectory() || ClassPathUtil.isJavaBinary(f));
    }

    @Override
    public String getDescription() {
        return Bundle.CTL_JavaSourceFilter_Description();
    }
}
