package org.jreserve.grscript.gui.script.explorer.data;

import java.io.File;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptFile extends AbstractScriptFile implements Comparable<ScriptFile> {
    
    @XmlAttribute(name = "path", required = true)
    private String path;
    
    public ScriptFile() {
    }

    public ScriptFile(File path) {
        super.name = path.getName();
        this.path = path.getAbsolutePath();
    }
    
    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(ScriptFile f) {
        return name.compareToIgnoreCase(f.name);
    }
}