/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.officelaf;

import javax.swing.LookAndFeel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
enum LaF {
    
    //WINDOWS("windows", "org.officelaf.OfficeWindowsLookAndFeel"),
    WINDOWS("windows",     "org.officelaf.OfficeNimbusLookAndFeel"),
    LINUX("linux",     "org.officelaf.OfficeMetalLookAndFeel"),
    //LINUX("linux",     "org.officelaf.OfficeGTKLookAndFeel"),
    MAC("mac os x",    "org.officelaf.OfficeAquaLookAndFeel"),
    //MAC("mac os x",    "org.officelaf.OfficeMetalLookAndFeel"),
    DEFAULT("default", null);

    private String osName;
    private String lafName;

    private LaF(String osName, String lafName) {
        this.osName  = osName;
        this.lafName = lafName;
    }

    String getOsName() {
        return osName;
    }                                                   

    String getLafName() {
        return lafName;
    }
    
    LookAndFeel createLafInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(lafName == null)
            return null;
        Class lafClass = Class.forName(lafName);
        return (LookAndFeel) lafClass.newInstance();
    }

    @Override
    public String toString() {
        return osName + ": " + lafName;
    }

    static LaF get(String osName) {
        String osn = osName.toLowerCase();
        for(LaF laf : LaF.values())
            if(osn.startsWith(laf.osName.toLowerCase()))
                return laf;
        return DEFAULT;
    }        
}
