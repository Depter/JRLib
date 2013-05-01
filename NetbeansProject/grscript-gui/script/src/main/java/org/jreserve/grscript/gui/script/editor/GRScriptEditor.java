package org.jreserve.grscript.gui.script.editor;

import groovy.ui.text.GroovyFilter;
import groovy.ui.text.TextEditor;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import org.jreserve.grscript.gui.notificationutil.BubbleUtil;
import org.netbeans.api.actions.Savable;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.core.spi.multiview.MultiViewFactory;
import org.openide.actions.RedoAction;
import org.openide.actions.UndoAction;
import org.openide.awt.UndoRedo;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.CookieSet;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.SystemAction;
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
    private final static String FONT_FAMILY = "Monospaced";
    private final static int FONT_SIZE = 12;
    
    private DataObject obj;
    private CookieSet cookies;
    
    private JToolBar toolBar = new JToolBar();
    private MultiViewElementCallback callback;
    private ScriptLoader loader;
    
    private GRSEditorUndoRedo undoRedo;
    private EditorDocListener documentListener;
    private TextEditor editor;
    
    private SaveCookieImpl saveCookie = new SaveCookieImpl();
    
    public GRScriptEditor(DataObject obj, CookieSet cookies) {
        this.obj = obj;
        this.cookies = cookies;
        associateLookup(obj.getLookup());
        initComponents();
        loadFile();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        createEditor();
        add(editor, BorderLayout.CENTER);

        undoRedo = new GRSEditorUndoRedo();
        documentListener = new EditorDocListener();
    }
    
    private void createEditor() {
        editor = new TextEditor(true);
        editor.setFont(new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE));

        DefaultStyledDocument doc = new DefaultStyledDocument();
        doc.setDocumentFilter(new GroovyFilter(doc));
        editor.setDocument(doc);
        
        registerEditorActions();
    }
    
    private void registerEditorActions() {
        InputMap keys = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke stroke;
        SystemAction action;
        
        action = SystemAction.get(UndoAction.class);
        stroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        keys.put(stroke!=null? stroke : KeyStroke.getKeyStroke("control Z"), action);
        
        action = SystemAction.get(RedoAction.class);
        stroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        keys.put(stroke!=null? stroke : KeyStroke.getKeyStroke("control R"), action);
        
        action = SystemAction.get(org.openide.actions.SaveAction.class);
        stroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        keys.put(stroke!=null? stroke : KeyStroke.getKeyStroke("control S"), action);
    }
    
    private void loadFile() {
        loader = new ScriptLoader(obj.getPrimaryFile());
        loader.execute();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
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
        return undoRedo;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        initTabName();
    }
    
    private void initTabName() {
        if(obj == null || callback == null)
            return;
        TopComponent tc = callback.getTopComponent();
        String name = obj.getPrimaryFile().getName();
        //tc.setDisplayName(name);
        if(obj.isModified())
            name = "<b>"+name+"</b>";
        tc.setHtmlDisplayName("<html>"+name+"</html>");
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
    
    DataObject getDataObject() {
        return obj;
    }
    
    JEditorPane getEditor() {
        return editor;
    }
    
    private void addSaveCookie() {
        if(obj.getLookup().lookup(SaveCookie.class) == null) {
            cookies.add(saveCookie);
            obj.setModified(true);
        }
    }

    private void removeSaveCookie() {
        SaveCookie cookie = obj.getLookup().lookup(SaveCookie.class);

        if(cookie != null && cookie.equals(saveCookie)) {
            cookies.remove(saveCookie);
            obj.setModified(false);
        }
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
                editor.setText(str);
                editor.getDocument().addDocumentListener(documentListener);
                undoRedo.enable(editor);
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
    
    private class EditorDocListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            contentChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            contentChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    
        private void contentChanged() {
            addSaveCookie();
            initTabName();
        }
    }

    private class SaveCookieImpl implements SaveCookie {

        private SaveCookieImpl() {
        }

        @Override
        public void save() throws IOException {
            FileSystem.AtomicAction aa = new SaveAction(GRScriptEditor.this);
            FileUtil.runAtomicAction(aa);
            undoRedo.discardAllEdits();
            removeSaveCookie();
            initTabName();
        }

        @Override
        public String toString() {
            return "SaveCookie: "+obj.getPrimaryFile().getNameExt();
        }
    }
}
