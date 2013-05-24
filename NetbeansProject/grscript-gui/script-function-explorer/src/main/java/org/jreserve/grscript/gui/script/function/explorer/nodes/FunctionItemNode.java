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
package org.jreserve.grscript.gui.script.function.explorer.nodes;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItemNode extends AbstractNode {
    
    public final static String ACTION_PATH = "Scripts/Actions/Nodes/FunctionItemNode";
    private final static Image FUNCTION_IMG = ImageUtilities.loadImage(FunctionFolderNode.IMG_PATH+"function.png");
    private final static Image PROPERTY_IMG = ImageUtilities.loadImage(FunctionFolderNode.IMG_PATH+"property.png");
    private FunctionItem item;
    private HelpCtx help;
    
    public FunctionItemNode(FunctionItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
        setDisplayName(item.getSigniture());
        help = item.getHelpCtx();
    }

    @Override
    public Image getIcon(int type) {
        return item.isProperty()? PROPERTY_IMG : FUNCTION_IMG;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return help;
    }

    @Override
    public Action[] getActions(boolean context) {
        List<Action> actions = new ArrayList<Action>(Utilities.actionsForPath(ACTION_PATH));
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        return new OpenHelpAction();
    }
    
    private class OpenHelpAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            help.display();
        }
    }
}
