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
package org.jreserve.gui.calculations.api;

import java.util.List;
import org.jreserve.gui.misc.namedcontent.DefaultContentChooserController;
import org.jreserve.gui.misc.namedcontent.NamedContentUtil;
import org.netbeans.api.project.Project;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.CalculationContents.One.Title=Select calculation",
    "LBL.CalculationContents.Multiple.Title=Select calculations"
})
public class CalculationContents {
    
    public static boolean containsCalculations(Project p) {
        return getNCP(p) != null;
    }
    
    public static <T extends CalculationProvider> T getCalculation(Project p, String path, Class<T> clazz) {
        NamedCalculationProvider ncp = getNCP(p);
        return ncp==null? null : NamedContentUtil.getContent(ncp, path, clazz);
    }
    
    private static NamedCalculationProvider getNCP(Project p) {
        return p.getLookup().lookup(NamedCalculationProvider.class);
    }
    
    public static <T extends CalculationProvider> T selectOne(Project p, Class<T> clazz) {
        return selectOne(p, clazz, Bundle.LBL_CalculationContents_One_Title());
    }
    
    public static <T extends CalculationProvider> T selectOne(NamedCalculationProvider ncp, Class<T> clazz) {
        return selectOne(ncp, clazz, Bundle.LBL_CalculationContents_One_Title());
    }

    public static <T extends CalculationProvider> T selectOne(Project p, Class<T> clazz, String title) {
        NamedCalculationProvider ncp = getNCP(p);
        return ncp==null? null : selectOne(ncp, clazz, title);
    }

    public static <T extends CalculationProvider> T selectOne(NamedCalculationProvider ncp, Class<T> clazz, String title) {
        DefaultContentChooserController controller = new DefaultContentChooserController(ncp, clazz, title);
        return NamedContentUtil.userSelect(controller, clazz);
    }
    
    public static <T extends CalculationProvider> List<T> selectMultiple(Project p, Class<T> clazz) {
        return selectMultiple(p, clazz, Bundle.LBL_CalculationContents_Multiple_Title());
    }
    
    public static <T extends CalculationProvider> List<T> selectMultiple(NamedCalculationProvider ncp, Class<T> clazz) {
        return selectMultiple(ncp, clazz, Bundle.LBL_CalculationContents_Multiple_Title());
    }

    public static <T extends CalculationProvider> List<T> selectMultiple(Project p, Class<T> clazz, String title) {
        NamedCalculationProvider ncp = getNCP(p);
        return ncp==null? null : selectMultiple(ncp, clazz, title);
    }

    public static <T extends CalculationProvider> List<T> selectMultiple(NamedCalculationProvider ncp, Class<T> clazz, String title) {
        DefaultContentChooserController controller = new DefaultContentChooserController(ncp, clazz, title);
        return NamedContentUtil.userMultiSelect(controller, clazz);
    }
    
    private CalculationContents() {
    }
}
