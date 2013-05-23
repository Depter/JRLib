package org.jreserve.grscript.gui.script.editor;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TogleCommentAction extends AbstractAction {

    private final static KeyStroke STROKE = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK);
    private JEditorPane editor;

    TogleCommentAction(JEditorPane editor) {
        this.editor = editor;
        putValue(Action.ACCELERATOR_KEY, STROKE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        if(start >= 0) 
            togleComment(start, end);
    }
    
    private void togleComment(int start, int end) {
        try {
            if(shouldComment(start, end)) {
                commentRows(start, end);
            } else {
                uncommentRows(start, end);
            }
        } catch (BadLocationException ex) {}
    }
    
    private boolean shouldComment(int start, int end) throws BadLocationException {
        int offset = start;
        int length = editor.getDocument().getLength();
        
        do {
            int rowStart = Utilities.getRowStart(editor, offset);
            int rowEnd = Utilities.getRowEnd(editor, offset);
            if(!isCommented(rowStart, rowEnd))
                return true;
            offset = rowEnd + 1;
        } while(offset <= end && offset < length);
        
        return false;
    }
    
    private boolean isCommented(int start, int end) throws BadLocationException {
        return getLine(start, end).trim().startsWith("//");
    }
    
    private String getLine(int start, int end) throws BadLocationException {
        return editor.getText(start, end-start);
    }
    
    private void commentRows(int start, int end) throws BadLocationException {
        int offset = start;
        int length = editor.getDocument().getLength();
        
        do {
            int rowStart = Utilities.getRowStart(editor, offset);
            commentRow(rowStart);
            end +=2;
            offset = Utilities.getRowEnd(editor, rowStart) + 1;
        } while(offset <= end && offset < length);
    }
    
    private void commentRow(int start) throws BadLocationException {
        editor.getDocument().insertString(start, "//", null);
    }
    
    private void uncommentRows(int start, int end) throws BadLocationException {
        int offset = start;
        int length = editor.getDocument().getLength();
        
        do {
            int rowStart = Utilities.getRowStart(editor, offset);
            int rowEnd = Utilities.getRowEnd(editor, offset);
            int cOffset = getCommentPosition(rowStart, rowEnd);
            if(cOffset >= 0) {
                removeComment(cOffset);
                end -= 2;
            }
            offset = Utilities.getRowEnd(editor, rowStart) + 1;
        } while(offset <= end && offset < length);
    }
    
    private int getCommentPosition(int rowStart, int rowEnd) throws BadLocationException {
        String line = getLine(rowStart, rowEnd);
        int size = line.length();
        for(int i=0; i<size; i++) {
            char c = line.charAt(i);
            if(Character.isWhitespace(c)) {
            } else if(c == '/') {
                return rowStart+i;
            } else {
                return -1;
            }
        }
        return -1;
    }
    
    private void removeComment(int offset) throws BadLocationException {
        editor.getDocument().remove(offset, 2);
    }
}
