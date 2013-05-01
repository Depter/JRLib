package org.jreserve.grscript.gui.script.editor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem.AtomicAction;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SaveAction implements AtomicAction {
    
    private GRScriptEditor editor;
    private DataObject obj;
    
    private OutputStream out = null;
    private FileObject fo = null;
    private FileLock lock = null;
    
    public SaveAction(GRScriptEditor editor) {
        this.editor = editor;
        this.obj = editor.getDataObject();
    }

    @Override
    public void run() throws IOException {
        try {
            openStream();
            writeDocumentToStream();
        } finally {
            closeStream();
        }
    }
    
    private void openStream() throws IOException {
        fo = obj.getPrimaryFile();
        lock = fo.lock();
        out = new BufferedOutputStream(fo.getOutputStream(lock));
    }
    
    private void writeDocumentToStream() throws IOException {
        try {
            Document documnet = editor.getEditor().getDocument();
            EditorKit kit = editor.getEditor().getEditorKit();
            int length = documnet.getLength();
            kit.write(out, documnet, 0, length);
        } catch (BadLocationException ex) {
            throw new IOException(ex);
        }
    }
    
    private void closeStream() throws IOException {
        try {
            if(out != null)
                out.close();
        } finally {
            out = null;
            if(lock != null) {
                lock.releaseLock();
                lock = null;
            }
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

}
