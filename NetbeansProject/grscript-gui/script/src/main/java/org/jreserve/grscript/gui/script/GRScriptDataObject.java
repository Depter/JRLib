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
package org.jreserve.grscript.gui.script;

import java.io.File;
import java.io.IOException;
import org.jreserve.grscript.gui.script.editor.GRScriptEditor;
import org.jreserve.grscript.gui.script.registry.ScriptFile;
import org.jreserve.grscript.gui.script.registry.ScriptRegistry;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_GRScript_LOADER=Files of GRScript"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_GRScript_LOADER",
        mimeType = "text/x-grscript",
        extension = {"grs", "GRS"})
@DataObject.Registration(
        mimeType = "text/x-grscript",
        iconBase = "org/jreserve/grscript/gui/script/triangle.png",
        displayName = "#LBL_GRScript_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/text/x-grscript/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class GRScriptDataObject extends MultiDataObject {

    public GRScriptDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-grscript", true);
    }

    @Override
    protected Node createNodeDelegate() {
        return new GRScriptDataNode(this);
    }

    @Override
    protected void handleDelete() throws IOException {
        super.handleDelete();
        removeScriptFile();
    }

    private void removeScriptFile() {
        File file = FileUtil.toFile(getPrimaryFile());
        ScriptFile sf = ScriptRegistry.getScriptFile(file);
        if(sf != null && sf.getParent() != null)
            sf.getParent().removeFile(sf);
    }
    
    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(
            displayName = "#LBL_GRScript_EDITOR",
            iconBase = "org/jreserve/grscript/gui/script/triangle.png",
            mimeType = "text/x-grscript",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "GRScript",
            position = 1000)
    @Messages("LBL_GRScript_EDITOR=Source")
    public static MultiViewElement createEditor(Lookup lkp) {
        GRScriptDataObject obj = lkp.lookup(GRScriptDataObject.class);
        CookieSet cookies = obj.getCookieSet();
        return new GRScriptEditor(obj, cookies);
    }
}
