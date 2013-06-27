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
package org.jreserve.gui.misc.logging;

import java.util.logging.Level;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.LoggingUtil.off=Off",
    "LBL.LoggingUtil.severe=Severe",
    "LBL.LoggingUtil.warning=Warning",
    "LBL.LoggingUtil.config=Config",
    "LBL.LoggingUtil.info=Info",
    "LBL.LoggingUtil.fine=Fine",
    "LBL.LoggingUtil.finer=Finer",
    "LBL.LoggingUtil.finest=Finest",
    "LBL.LoggingUtil.all=All",
    "LBL.LoggingUtil.unknown=Unknown"
})
public class LoggingUtil {
    
    public static Level escapeLevel(Level level) {
        int intLevel = level.intValue();
        if(Level.ALL.intValue() >= intLevel)
            return Level.ALL;
        else if(Level.FINEST.intValue() >= intLevel)
            return Level.FINEST;
        else if(Level.FINER.intValue() >= intLevel)
            return Level.FINER;
        else if(Level.FINE.intValue() >= intLevel)
            return Level.FINE;
        else if(Level.CONFIG.intValue() >= intLevel)
            return Level.CONFIG;
        else if(Level.INFO.intValue() >= intLevel)
            return Level.INFO;
        else if(Level.WARNING.intValue() >= intLevel)
            return Level.WARNING;
        else if(Level.SEVERE.intValue() >= intLevel)
            return Level.SEVERE;
        else
            return Level.OFF;
    }
    
    public static String getUserName(Level level) {
        level = escapeLevel(level);
        if(Level.SEVERE.equals(level))
            return Bundle.LBL_LoggingUtil_severe();
        else if(Level.WARNING.equals(level))
            return Bundle.LBL_LoggingUtil_warning();
        else if(Level.INFO.equals(level))
            return Bundle.LBL_LoggingUtil_info();
        else if(Level.CONFIG.equals(level))
            return Bundle.LBL_LoggingUtil_config();
        else if(Level.FINE.equals(level))
            return Bundle.LBL_LoggingUtil_fine();
        else if(Level.FINER.equals(level))
            return Bundle.LBL_LoggingUtil_finer();
        else if(Level.FINEST.equals(level))
            return Bundle.LBL_LoggingUtil_finest();
        else if(Level.ALL.equals(level))
            return Bundle.LBL_LoggingUtil_all();
        else if(Level.OFF.equals(level))
            return Bundle.LBL_LoggingUtil_off();
        else
            return Bundle.LBL_LoggingUtil_unknown();
    }
    
}
