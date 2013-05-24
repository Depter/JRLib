/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript.gui.script.registry;

import java.io.File;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name="file")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptFile implements Comparable<ScriptFile>{
 
    @XmlTransient
    private ScriptFolder parent;
    
    @XmlAttribute(name="path")
    private String path;
    @XmlAttribute(name="name")
    private String name;
    
    public ScriptFile() {
    }
    
    public ScriptFile(File path) {
        this.path = path.getAbsolutePath();
        this.name = path.getName();
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
    
    public ScriptFolder getParent() {
        return parent;
    }
    
    void setParent(ScriptFolder parent) {
        this.parent = parent;
    }
    
    @Override
    public int compareTo(ScriptFile file) {
        return name.compareToIgnoreCase(file.name);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ScriptFile) &&
               compareTo((ScriptFile)o) == 0;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return "ScriptFile ["+path+"]";
    }
}
