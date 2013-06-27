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
package org.jreserve.gui.misc.logging.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledDocument;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.actions.CopyAction;
import org.openide.actions.DeleteAction;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@TopComponent.Description(
    preferredID = "LogviewTopComponent",
    persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(
    mode = "output", 
    openAtStartup = false
)
@ActionID(
    category = "Window", 
    id = "org.jreserve.gui.misc.logging.view.LogviewTopComponent"
)
@ActionReference(
    path = "Menu/Window", 
    position = 400
)
@TopComponent.OpenActionRegistration (
    displayName = "#CTL_LogviewAction",
    preferredID = "LogviewTopComponent"
)
@Messages({
    "CTL_LogviewAction=Show log",
    "CTL_LogviewTopComponent=Log"
})
public class LogviewTopComponent extends TopComponent {
    
    private final static String COPY_KEY = "control C";
    private final static String DELETE_ACTION_KEY = "delete";
    private final static String DELETE_KEY = "DELETE";
    private static LogviewTopComponent INSTANCE = null;

    public static void openView() {
        if(INSTANCE != null && INSTANCE.isOpened())
            return;
        getInstance().open();
    }
    
    private static LogviewTopComponent getInstance() {
        if(INSTANCE == null)
            return new LogviewTopComponent();
        return INSTANCE;
    }
    
    public static void closeView() {
        if(INSTANCE == null || !INSTANCE.isOpened())
            return;
        INSTANCE.close();
    }
    
    static StyledDocument getDocument() {
        return getInstance().logText.getStyledDocument();
    }
    
    private JTextPane logText;
    
    public LogviewTopComponent() {
        setInstance();
        initComponents();
        setName(Bundle.CTL_LogviewTopComponent());
        registerCopyAction();
        registerDeleteAction();
    }
    
    private void setInstance() {
        if(INSTANCE != null)
            throw new IllegalStateException("Not a singleton!");
        INSTANCE = this;
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        logText = new JTextPane();
        logText.setEditable(false);
        logText.addMouseListener(new LogPopUp());

        add(new JScrollPane(logText), BorderLayout.CENTER);
    }    
    
    private void registerCopyAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke(COPY_KEY);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DefaultEditorKit.copyAction);
        logText.getInputMap().put(stroke, DefaultEditorKit.copyAction);
        getActionMap().put(DefaultEditorKit.copyAction, new CopyLogAction());
    }
    
    private void registerDeleteAction() {
        KeyStroke stroke = KeyStroke.getKeyStroke(DELETE_KEY);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, DELETE_ACTION_KEY);
        logText.getInputMap().put(stroke, DELETE_ACTION_KEY);
        getActionMap().put(DELETE_ACTION_KEY, new DeleteLogAction());
    }
    
    private class CopyLogAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringSelection text = new StringSelection(getText());
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(text, null);
        }
        
        private String getText() {
            String txt = logText.getSelectedText();
            return txt!=null? txt : logText.getText();
        }
    }
    
    private class DeleteLogAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            logText.setText(null);
        }
    }
    
    private class LogPopUp extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent evt) {
            popupEvent(evt);
        }
        
        @Override
        public void mousePressed(MouseEvent evt) {
            popupEvent(evt);
        }
        
        private void popupEvent(MouseEvent evt) {
            if(evt.isPopupTrigger())
                showPopup(evt.getPoint());
        }
        
        private void showPopup(Point point) {
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(SystemAction.get(CopyAction.class).getMenuPresenter());
            popUp.add(SystemAction.get(DeleteAction.class).getMenuPresenter());
            popUp.show(logText, point.x, point.y);
        }
    }
}
