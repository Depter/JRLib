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

package org.jreserve.gui.misc.utils.notifications;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DialogUtil {
    
    private final static String ERR_LINE_FORMAT = "%n\t%s.%s(line: %d)";
    
    /**
     * Displays a dialog, for the given type.
     * 
     * @see "org.openide.NotifyDescriptor.Message"
     * @param title The title of the dialog. May be `null`.
     * @param message The dialog message.
     * @param messageType The type of the message. If `null`, the message 
     *                    type will be {@link MessageType.PLAIN PLAIN}.
     */
    public static void show(String title, Object message, MessageType messageType) {
        if(messageType==null)
            messageType = MessageType.PLAIN;
        notify(title, message, messageType);
    }
    
    private static void notify(String title, Object message, MessageType messageType) {
        if(message instanceof Throwable)
            message = createThrowableMessage((Throwable)message);
        
        int ndType = messageType.getNotifyDescriptorType();
        NotifyDescriptor nd = new NotifyDescriptor.Message(message, ndType);
        if(title != null)
            nd.setTitle(title);
        
        DialogDisplayer.getDefault().notifyLater(nd);
    }
    
    private static Object createThrowableMessage(Throwable t) {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.add(new JLabel(t.getLocalizedMessage()), BorderLayout.NORTH);
        
        JTextArea stackTrace = new JTextArea();
        stackTrace.setForeground(Color.red);
        stackTrace.setColumns(50);
        stackTrace.setEditable(false);
        stackTrace.setTabSize(4);
        stackTrace.setText(toString(t));
        JScrollPane scroll = new JScrollPane(stackTrace);
        scroll.setPreferredSize(new Dimension(600, 200));
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static String toString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while(t != null) {
            appendThrowable(t, sb, isFirst);
            t = t.getCause();
            isFirst = false;
        }
        
        return sb.toString();
    }
    
    private static void appendThrowable(Throwable t, StringBuilder sb, boolean isFirst) {
        if(!isFirst)
            sb.append("Caused by: ");
        sb.append(t.getClass().getName()).append(": ").append(t.getLocalizedMessage());
        
        for(StackTraceElement e : t.getStackTrace())
            sb.append(String.format(ERR_LINE_FORMAT, e.getClassName(), e.getMethodName(), e.getLineNumber()));
    }
    
    /**
     * Shows a plain message.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showPlain(Object message) {
        showPlain(null, message);
    }
    
    /**
     * Shows a plain message with the given title.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showPlain(String title, Object message) {
        show(title, message, MessageType.PLAIN);
    }
    
    /**
     * Shows an info message.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showInfo(Object message) {
        showInfo(null, message);
    }
    
    /**
     * Shows an info message with the given title.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showInfo(String title, Object message) {
        show(title, message, MessageType.INFO);
    }
    
    /**
     * Shows a warning message.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showWarning(Object message) {
        showWarning(null, message);
    }
    
    /**
     * Shows a warning message with the given title.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showWarning(String title, Object message) {
        show(title, message, MessageType.WARNING);
    }
    
    /**
     * Shows an error message.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showError(Object message) {
        showError(null, message);
    }
    
    /**
     * Shows an error message with the given title.
     * 
     * @see #show(String, Object, MessageType)
     */
    public static void showError(String title, Object message) {
        show(title, message, MessageType.ERROR);
    }
    
    public static void showDialog(java.awt.Component component, ActionListener okListener) {
        String title = component.getName();
        ActionListener listener = okListener==null? null : new OkListener(okListener);
        DialogDescriptor dd = new DialogDescriptor(component, title, true, listener);
        if(component instanceof DialogContent)
            ((DialogContent)component).setDialogDescriptor(dd);
        DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
    }
    
    public static Dialog createDialog(Component content) {
        NbDialog dialog = new NbDialog(content.getName(), content);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        return dialog;
    }
    
    private DialogUtil() {}

    private static class OkListener implements ActionListener {
        
        private final ActionListener delegate;
        
        private OkListener(ActionListener delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(DialogDescriptor.OK_OPTION == e.getSource())
                delegate.actionPerformed(e);
        }
    }
    
    public static interface DialogContent {
        public void setDialogDescriptor(DialogDescriptor dd);
    }
}
