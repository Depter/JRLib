package org.jreserve.grscript.gui.script.explorer.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@XmlRootElement(name = "folder")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptFolder extends AbstractScriptFile implements Comparable<ScriptFolder>{
    
    @XmlAttribute(name="id", required = true)
    private String uuid = UUID.randomUUID().toString();
    
    @XmlElementRef
    private Set<ScriptFolder> folders = new TreeSet<ScriptFolder>();
    @XmlElementRef
    private Set<ScriptFile> files = new TreeSet<ScriptFile>();

    public ScriptFolder() {
    }

    public ScriptFolder(String name) {
        super(name);
    }
    
    public void setName(String name) {
        this.name = name;
        ScriptEvent.folderRenamed(this);
        ScriptRegistry.saveRegistry();
    }

    public List<ScriptFolder> getFolders() {
        return new ArrayList<ScriptFolder>(folders);
    }
    
    public void addFolder(ScriptFolder folder) {
        if(folder != null && folders.add(folder)) {
            folder.parent = this;
            ScriptEvent.folderAdded(folder);
            ScriptRegistry.saveRegistry();
        }
    }
    
    public void removeFolder(ScriptFolder folder) {
        if(folders.remove(folder)) {
            folder.parent = null;
            ScriptEvent.folderRemoved(folder, this);
            ScriptRegistry.saveRegistry();
        }
    }

    public List<ScriptFile> getFiles() {
        return new ArrayList<ScriptFile>(files);
    }
    
    public void addFile(ScriptFile file) {
        if(file != null && files.add(file)) {
            file.parent = this;
            ScriptEvent.fileAdded(file);
            ScriptRegistry.saveRegistry();
        }
    }
    
    public void removeFile(ScriptFile file) {
        if(files.remove(file)) {
            file.parent = null;
            ScriptEvent.fileRemoved(file, this);
            ScriptRegistry.saveRegistry();
        }
    }
    
    void setParentsAfterUnmarshal() {
        for(ScriptFolder folder : folders) {
            folder.setParent(this);
            folder.setParentsAfterUnmarshal();
        }
        
        for(ScriptFile file : files)
            file.setParent(this);
    }
    
    @Override
    public int compareTo(ScriptFolder f) {
        if(equals(f)) return 0;
        return compare(name, f.name);
    }
    
    private int compare(String n1, String n2) {
        if(n1 == null)
            return n2==null? 0 : -1;
        return n2==null? 1 : n1.compareTo(n2);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ScriptFolder) &&
               uuid.equals(((ScriptFolder)o).uuid);
    }
    
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
