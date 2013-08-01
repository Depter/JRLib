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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.NotifyDescriptor;
import org.openide.awt.NotificationDisplayer.Priority;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public enum MessageType {
    
    PLAIN   (NotifyDescriptor.PLAIN_MESSAGE      , null),
    INFO    (NotifyDescriptor.INFORMATION_MESSAGE, "info.png"),
    WARNING (NotifyDescriptor.WARNING_MESSAGE    , "warning.gif"),
    ERROR   (NotifyDescriptor.ERROR_MESSAGE      , "error.gif");
    
    private final static String IMG_HOME = "org/jreserve/gui/misc/utils/notifications/"; //NOI18
    
    private static Icon loadImage(String resourceID) {
        if(resourceID == null)
            return new ImageIcon();
        
        Icon img = ImageUtilities.loadImageIcon(IMG_HOME+resourceID, false);
        if(img == null) {
            logLoadError(resourceID);
            img = new ImageIcon();
        }
        return img;
    }
    
    private static void logLoadError(String source) {
        Logger logger = Logger.getLogger(MessageType.class.getName());
        String msg = "Unable to load image from \"{0}\"! Empty image will be used instead.";
        logger.log(Level.WARNING, msg, source);
    }
    
    private final Icon img;
    private final int ndType;
    private final Priority priority;
    
    private MessageType(int ndType, String imgResourceID) {
        img = loadImage(imgResourceID);
        this.ndType = ndType;
        
        switch(ndType) {
            case NotifyDescriptor.ERROR_MESSAGE:
                priority = Priority.HIGH;
                break;
            case NotifyDescriptor.WARNING_MESSAGE:
                priority = Priority.HIGH;
                break;
            case NotifyDescriptor.INFORMATION_MESSAGE:
                priority = Priority.NORMAL;
                break;
            case NotifyDescriptor.PLAIN_MESSAGE:
                priority = Priority.LOW;
                break;
            default: 
                priority = Priority.LOW;
        }
    }
    
    int getNotifyDescriptorType() {
        return ndType;
    }
    
    Icon getIcon() {
        return img;
    }
    
    Priority getPriority() {
        return priority;
    }
}
