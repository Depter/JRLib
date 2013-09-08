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
package org.jreserve.gui.data.spi.inport;

import java.util.List;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ImportDataProvider {
    public final static String PROP_SOURCE_PROVIDER = "dataSourceProvider";
    public final static String PROP_INIT_DATA_SOURCE = "init.data.Source"; //NOI18
    public final static String PROP_DATA_SOURCE = "data.source";    //NOI18
    public final static String PROP_IMPORT_WIZARD = "import.wizard";    //NOI18
    public final static String PROP_IMPORT_DATA = "import.data";    //NOI18
    public final static String PROP_IMPORT_DATA_CUMMULATED = "import.data.cummulated";    //NOI18
    public final static String PROP_SAVE_TYPE = "save.type";    //NOI18
    public final static String PROP_SHOULD_IMPORT_DATA = "shouldImportData";    //NOI18

    public List<? extends WizardDescriptor.Panel> getPanels();

    public void addChangeListener(ChangeListener listener);

    public void removeChangeListener(ChangeListener listener);

    public static @interface Registration {
        public String id();
        public String displayName();
        public String iconBase() default "";
        public boolean requiresDataSource() default true;
        public int position() default Integer.MAX_VALUE;
    }
    
}
