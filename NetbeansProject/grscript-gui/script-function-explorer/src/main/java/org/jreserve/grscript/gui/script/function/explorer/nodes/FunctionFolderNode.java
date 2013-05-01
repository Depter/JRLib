package org.jreserve.grscript.gui.script.function.explorer.nodes;

import java.awt.Image;
import org.jreserve.grscript.gui.script.functions.FunctionFolder;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionFolderNode extends AbstractNode {

    final static String IMG_PATH = "org/jreserve/grscript/gui/script/function/explorer/nodes/";
    private final static Image OPENED_IMG = ImageUtilities.loadImage(IMG_PATH+"folderOpen.gif");
    private final static Image CLOSED_IMG = ImageUtilities.loadImage(IMG_PATH+"folder.gif");
    
    public FunctionFolderNode(FunctionFolder folder) {
        super(Children.create(new FunctionFolderChildren(folder), true), Lookups.singleton(folder));
        setDisplayName(folder.getName());
    }

    @Override
    public Image getIcon(int type) {
        return CLOSED_IMG;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return OPENED_IMG;
    }
}
