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

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DialogUtil {
    
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
        int ndType = messageType.getNotifyDescriptorType();
        NotifyDescriptor nd = new NotifyDescriptor.Message(message, ndType);
        if(title != null)
            nd.setTitle(title);
        
        DialogDisplayer.getDefault().notify(nd);
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
    
    private DialogUtil() {}
}
