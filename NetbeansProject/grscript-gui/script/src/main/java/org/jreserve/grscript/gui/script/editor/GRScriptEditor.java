package org.jreserve.grscript.gui.script.editor;

import groovy.ui.ConsoleTextEditor;
import java.awt.BorderLayout;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.jreserve.grscript.gui.script.GRScriptDataObject;
import org.netbeans.api.actions.Savable;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "MSG.GRScriptEditor.LoadError=Unable to load file..."
})
public class GRScriptEditor extends TopComponent implements MultiViewElement {
    
    private final static Logger logger = Logger.getLogger(GRScriptEditor.class.getName());
    
    private GRScriptDataObject obj;
    private JToolBar toolBar = new JToolBar();
    private MultiViewElementCallback callback;
    private ConsoleTextEditor editor;
    private ScriptLoader loader;
    
    public GRScriptEditor(Lookup lkp) {
        associateLookup(lkp);
        initComponents();
        
        obj = lkp.lookup(GRScriptDataObject.class);
        if(obj != null) {
            loader = new ScriptLoader(obj.getPrimaryFile());
            loader.execute();
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        editor = new ConsoleTextEditor();
        add(editor, BorderLayout.CENTER);
    }
    
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolBar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return super.getLookup();
    }

    @Override
    public void componentOpened() {
        super.componentOpened();
    }

    @Override
    public void componentClosed() {
        super.componentClosed();
        if(loader != null) {
            loader.cancel(true);
        }
    }

    @Override
    public void componentShowing() {
        super.componentShowing();
    }

    @Override
    public void componentHidden() {
        super.componentHidden();
    }

    @Override
    public void componentActivated() {
        super.componentActivated();
    }

    @Override
    public void componentDeactivated() {
        super.componentDeactivated();
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        initTabName();
    }
    
    private void initTabName() {
        if(obj == null || callback == null)
            return;
        
        String name = obj.getPrimaryFile().getName();
        setDisplayName(name);
        setHtmlDisplayName(name);
    }

    @Override
    public CloseOperationState canCloseElement() {
        Savable sav = getLookup().lookup(Savable.class);
        if(sav == null)
            return CloseOperationState.STATE_OK;
        
        Action proceed = null;
        Action cancel = null;
        return MultiViewFactory.createUnsafeCloseState("editor", proceed, cancel);
    }
    
    private class ScriptLoader extends SwingWorker<String, Void> {
        
        private final FileObject fo;

        public ScriptLoader(FileObject fo) {
            this.fo = fo;
        }
        
        @Override
        protected String doInBackground() throws Exception {
            return fo.asText();
        }

        @Override
        protected void done() {
            try {
                String str = get();
                editor.getTextEditor().setText(str);
            } catch (InterruptedException ex) {
                logger.log(Level.FINE, "Loading of file interrupted! {0}", fo.getPath());
            } catch (ExecutionException ex) {
                logger.log(Level.SEVERE, "Unable to load file: "+fo.getPath(), ex);
                BubbleUtil.showException(Bundle.MSG_GRScriptEditor_LoadError(), ex);
            } finally {
                loader = null;
            }
        }
    }
}
