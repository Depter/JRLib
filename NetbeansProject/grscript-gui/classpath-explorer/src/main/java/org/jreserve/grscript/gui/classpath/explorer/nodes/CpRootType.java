package org.jreserve.grscript.gui.classpath.explorer.nodes;

import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.CpRootType.Platform=Platform",
    "CTL.CpRootType.Module=Modules",
    "CTL.CpRootType.Custom=Custom"
})
public enum CpRootType {
    PLATFORM (Bundle.CTL_CpRootType_Platform()),
    MODULES  (Bundle.CTL_CpRootType_Module()),
    CUSTOM   (Bundle.CTL_CpRootType_Custom());
    
    private final String userName;
    
    private CpRootType(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
}
