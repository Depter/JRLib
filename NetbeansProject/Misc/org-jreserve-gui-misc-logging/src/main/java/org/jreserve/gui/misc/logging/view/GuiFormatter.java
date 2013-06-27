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

import org.jreserve.gui.misc.logging.LoggingUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class GuiFormatter extends Formatter {
    
    private final static int MAX_USER_NAME = 7;
    private final static String FORMAT = "[%-"+MAX_USER_NAME+"s]: %s: %s%n";
    
    @Override
    public String format(LogRecord record) {
        String level = LoggingUtil.getUserName(record.getLevel());
        String source = record.getSourceClassName();
        String msg = getMessage(record);
        msg = String.format(msg, record.getParameters());
        return String.format(FORMAT, level, source, msg);
    }
    
    private String getMessage(LogRecord record) {
        String msg = super.formatMessage(record);
        if(record.getThrown() != null)
            msg+="\n"+getStackTrace(record.getThrown());
        return msg;
    }
    
    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
