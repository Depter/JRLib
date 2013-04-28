package org.jreserve.grscript.gui.script.explorer.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractScriptFile {
    
    @XmlTransient
    protected ScriptFolder parent;
    
    @XmlAttribute(name = "name", required = true)
    protected String name;
    
    public AbstractScriptFile() {
    }
    
    public AbstractScriptFile(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public ScriptFolder getParent() {
        return parent;
    }

    public void setParent(ScriptFolder parent) {
        this.parent = parent;
    }
        
    @Override
    public boolean equals(Object o) {
        return (o instanceof AbstractScriptFile) &&
               equals(name, ((AbstractScriptFile)o).name);
    }
    
    private boolean equals(String n1, String n2) {
        if(n1 == null)
            return n2==null;
        return n2==null? false : n1.equalsIgnoreCase(n2);
    }
    
    @Override
    public int hashCode() {
        return name==null? 0 : name.hashCode();
    }
    
    @Override
    public String toString() {
        return "ScriptFolder ["+(name==null? "" : name)+"]";
    }
}