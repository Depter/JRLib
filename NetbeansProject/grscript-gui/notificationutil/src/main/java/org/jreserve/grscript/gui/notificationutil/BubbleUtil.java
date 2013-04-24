package org.jreserve.grscript.gui.notificationutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import org.openide.awt.Notification;
import org.openide.awt.NotificationDisplayer;
import org.openide.awt.NotificationDisplayer.Priority;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle.Messages;

/**
 * Utility class to display bubble pop-up messages to the user.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.ExceptionBubble.Title=Error"
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
        String title = Bundle.CTL_ExceptionBubble_Title();
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
        private Notification nd;

        public BubbleListener(String title, String msg, Object dialogMsg, MessageType type) {
            this.title = title;
            this.msg = msg;
            this.dialogMsg = dialogMsg;
            this.type = type;
        }
        
        void showBubble() {
            Priority priority = type.getPriority();
            Icon icon = type.getIcon();
            nd = NotificationDisplayer.getDefault().notify(title, icon, msg, this, priority);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            nd.clear();
            if(dialogMsg != null)
                DialogUtil.show(title, dialogMsg, type);
        }
    }
}
