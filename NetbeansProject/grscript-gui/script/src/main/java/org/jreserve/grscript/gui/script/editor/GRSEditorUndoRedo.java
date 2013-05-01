package org.jreserve.grscript.gui.script.editor;

import groovy.ui.text.TextUndoManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openide.awt.UndoRedo;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class GRSEditorUndoRedo implements UndoRedo {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private TextUndoManager mgr = new TextUndoManager();

    GRSEditorUndoRedo() {
        mgr.addPropertyChangeListener(new MgrListener());
    }

    void enable(JEditorPane editor) {
        editor.getDocument().addUndoableEditListener(mgr);
    }

    @Override
    public boolean canUndo() {
        return mgr.canUndo();
    }

    @Override
    public void undo() throws CannotUndoException {
        if (mgr.canUndo()) {
            mgr.undo();
        }
    }

    @Override
    public boolean canRedo() {
        return mgr.canRedo();
    }

    @Override
    public void redo() throws CannotRedoException {
        if (mgr.canRedo()) {
            mgr.redo();
        }
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for (ChangeListener l : listenrsToIterate()) {
            l.stateChanged(evt);
        }
    }

    private ChangeListener[] listenrsToIterate() {
        int size = listeners.size();
        return listeners.toArray(new ChangeListener[size]);
    }
    
    void discardAllEdits() {
        mgr.discardAllEdits();
    }
    
    @Override
    public String getUndoPresentationName() {
        return "Undo";
    }

    @Override
    public String getRedoPresentationName() {
        return "Redo";
    }

    private class MgrListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fireChange();
        }
    }
}
