package org.jreserve.grscript.gui.script.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name="folder")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptFolder implements Comparable<ScriptFolder> {
    
    @XmlTransient
    private ScriptFolder parent;
    
    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlElementRef
    private Set<ScriptFolder> folders = new TreeSet<ScriptFolder>();
    @XmlElementRef
    private Set<ScriptFile> files = new TreeSet<ScriptFile>();
    
    public ScriptFolder() {
    }
    
    public ScriptFolder(String name) {
        this.name = name;
    }

    void initParents() {
        for(ScriptFolder child : folders) {
            child.parent = this;
            child.initParents();
        }
        
        for(ScriptFile child : files)
            child.setParent(this);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        
    }
    
    public ScriptFolder getParent() {
        return parent;
    }
    
    public Set<ScriptFolder> getFolders() {
        return new TreeSet<ScriptFolder>(folders);
    }
    
    public void addFolder(ScriptFolder folder) {
        if(folders.add(folder)) {
            folder.parent = this;
            ScriptEvent.folderAdded(this, folder);
        }
    }

    public void removeFolder(ScriptFolder folder) {
        if(folders.remove(folder)) {
            folder.parent = null;
            ScriptEvent.folderDeleted(this, folder);
        }
    }
    
    public Set<ScriptFile> getFiles() {
        return new TreeSet<ScriptFile>(files);
    }
    
    public void addFiles(List<ScriptFile> files) {
        List<ScriptFile> added = new ArrayList<ScriptFile>();
        for(ScriptFile file : files) {
            if(this.files.add(file)) {
                file.setParent(this);
                added.add(file);
            }
        }
        
        if(!added.isEmpty())
            ScriptEvent.filesAdded(this, added);
    }
    
    public void addFile(ScriptFile file) {
        if(files.add(file)) {
            file.setParent(this);
            ScriptEvent.fileAdded(this, file);
        }
    }

    public void removeFile(ScriptFile file) {
        if(files.remove(file)) {
            file.setParent(null);
            ScriptEvent.fileDeleted(this, file);
        }
    }

    @Override
    public int compareTo(ScriptFolder o) {
        return name.compareToIgnoreCase(o.name);
    }
}
