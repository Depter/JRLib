package org.jreserve.grscript.gui.script.explorer.nodes;

import java.awt.Image;
import org.jreserve.grscript.gui.script.explorer.data.ScriptFile;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptFileNode extends AbstractNode {
    
    public final static String ACTION_PATH = "Scripts/Actions/Node/File";
    
    private ScriptFile file;

    public ScriptFileNode(ScriptFile file) {
        super(Children.LEAF, Lookups.singleton(file));
        this.file = file;
        setDisplayName(file.getName());
        setShortDescription(file.getPath());
    }
}
