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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import org.openide.awt.Notification;
import org.openide.awt.NotificationDisplayer;
import org.openide.awt.NotificationDisplayer.Priority;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.BubbleUtil.ExceptionBubble.Title=Error"
})
public class BubbleUtil {
    
    public static void show(String title, String msg, MessageType messageType) {
        show(title, msg, null, messageType);
    }
    
    public static void show(String title, String bubbleMsg, Object dialogMsg, MessageType messageType) {
        new BubbleListener(title, bubbleMsg, dialogMsg, messageType).showBubble();
    }
    
    public static void showPlain(String title, String msg) {
        showPlain(title, msg, null);
    }
    
    public static void showPlain(String title, String bubbleMsg, Object dialogMsg) {
        show(title, bubbleMsg, dialogMsg, MessageType.PLAIN);
    }
    
    public static void showInfo(String title, String msg) {
        showInfo(title, msg, null);
    }
    
    public static void showInfo(String title, String bubbleMsg, Object dialogMsg) {
        show(title, bubbleMsg, dialogMsg, MessageType.INFO);
    }
    
    public static void showWarning(String title, String msg) {
        showWarning(title, msg, null);
    }
    
    public static void showWarning(String title, String bubbleMsg, Object dialogMsg) {
        show(title, bubbleMsg, dialogMsg, MessageType.WARNING);
    }
    
    public static void showError(String title, String msg) {
        showError(title, msg, null);
    }
    
    public static void showError(String title, String bubbleMsg, Object dialogMsg) {
        show(title, bubbleMsg, dialogMsg, MessageType.ERROR);
    }
    
    public static void showException(Exception exception) {
        String bubbleMsg = exception.getLocalizedMessage();
        showException(bubbleMsg, exception);
    }
    
    public static void showException(String bubbleMsg, Exception exception) {
        String title = Bundle.CTL_BubbleUtil_ExceptionBubble_Title();
        showException(title, bubbleMsg, exception);
    }
    
    public static void showException(String title, String bubbleMsg, Exception exception) {
        show(title, bubbleMsg, exception, MessageType.ERROR);
    }
    
    /**
     * Displays the given text at the status bar.
     * 
     * @see StatusDisplayer#setStatusText(String)
     */
    public static void setStatusText(String str) {
        StatusDisplayer.getDefault().setStatusText(str);
    }
    
    private BubbleUtil() {}
    
    private static class BubbleListener implements ActionListener {
        
        private final String title;
        private final String msg;
        private final Object dialogMsg;
        private final MessageType type;
        private volatile Notification nd;

        public BubbleListener(String title, String msg, Object dialogMsg, MessageType type) {
            this.title = title;
            this.msg = msg;
            this.dialogMsg = dialogMsg;
            this.type = type;
        }
        
        void showBubble() {
            final Priority priority = type.getPriority();
            final Icon icon = type.getIcon();
            
            if(SwingUtilities.isEventDispatchThread()) {
               nd = NotificationDisplayer.getDefault().notify(title, icon, msg, this, priority);
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                       nd = NotificationDisplayer.getDefault().notify(title, icon, msg, BubbleListener.this, priority);
                    }
                });
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            nd.clear();
            if(dialogMsg != null)
                DialogUtil.show(title, dialogMsg, type);
        }
    }
}
