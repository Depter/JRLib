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

package org.jreserve.gui.calculations.claimtriangle.impl;

import org.jreserve.gui.calculations.api.CalculationEventUtil;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleCalculation;
import org.jreserve.gui.calculations.claimtriangle.ClaimTriangleEvent;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - path",
    "LBL.EventUtil.DataSourceChange=Storage changed to ''{0}''."
})
public class EventUtil {

    public static void fireDataSourceChange(AuditedObject ao, ClaimTriangleCalculation calculation) {
        DataSourceChange evt = new DataSourceChange(ao, calculation);
        CalculationEventUtil.fireEvent(evt);
    }
    
    private EventUtil() {
    }
    
    private static class DataSourceChange extends CalculationEventUtil.Change implements ClaimTriangleEvent.SourceChange {
        public DataSourceChange(AuditedObject ao, ClaimTriangleCalculation cp) {
            super(ao, cp, Bundle.LBL_EventUtil_DataSourceChange(cp.getDataSource().getPath()));
        }
    }
}
