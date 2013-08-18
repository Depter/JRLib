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
package org.jreserve.gui.data.api.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreePath;
import org.jreserve.gui.data.api.DataCategory;
import org.jreserve.gui.data.api.DataItem;
import org.jreserve.gui.data.api.util.DataItemChooserForm.Controller;
import org.jreserve.gui.data.api.DataManager;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.DataType;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DataItemChooser.Title.Source=Choose storage",
    "LBL.DataItemChooser.Title.Sources=Choose storages",
    "LBL.DataItemChooser.Title.Category=Choose category",
    "LBL.DataItemChooser.Title.Categories=Choose categories"
})
public class DataItemChooser {
    
    
    private DataItemChooser() {}
    
    public static DataSource chooseSource(DataManager dm) {
        return chooseSource(dm, null);
    }
    
    public static DataSource chooseSource(DataManager dm, DataType dataType) {
        Controller<DataSource> controller = new SourceController(false, dataType);
        return chooseOne(dm, controller);
    }
    
    private static <T> T chooseOne(DataManager dm, Controller<T> controller) {
        List<T> list = choose(dm, controller);
        return list.isEmpty()? null : list.get(0);
    }
    
    private static <T> List<T> choose(DataManager dm, Controller<T> controller) {
        return DataItemChooserForm.selectItems(dm, controller);
    }
    
    public static List<DataSource> chooseSoruces(DataManager dm) {
        return chooseSources(dm, null);
    }
    
    public static List<DataSource> chooseSources(DataManager dm, DataType dataType) {
        Controller<DataSource> controller = new SourceController(true, dataType);
        return choose(dm, controller);
    }

    public static DataCategory chooseCategory(DataManager dm) {
        Controller<DataCategory> controller = new CategoryController(false);
        return chooseOne(dm, controller);
    }
    
    public static List<DataCategory> chooseCategories(DataManager dm) {
        Controller<DataCategory> controller = new CategoryController(true);
        return choose(dm, controller);
    }
    
    private static abstract class AbstractController<T extends DataItem> implements DataItemChooserForm.Controller<T> {
        private boolean multipleSelection;
        private boolean showSources;
        private String title;
        
        private AbstractController(boolean multipleSelection, boolean showSources, String title) {
            this.multipleSelection = multipleSelection;
            this.showSources = showSources;
            this.title = title;
        }
        
        @Override
        public String getTitle() {
            return title;
        }
        
        @Override
        public boolean multipleSelection() {
            return multipleSelection;
        }

        @Override
        public boolean showSources() {
            return showSources;
        }

        @Override
        public List<T> getResults(TreePath[] pathes) {
            List<T> result = new ArrayList<T>();
            for(TreePath path : pathes) {
                T element = getSelectedElement(path.getLastPathComponent());
                if(element != null)
                    result.add(element);
            }
            return result;
        }
        
        protected abstract T getSelectedElement(Object lastElement);
    }
    
    private static class CategoryController extends AbstractController<DataCategory> {
        private CategoryController(boolean multipleSelection) {
            super(multipleSelection, false,
                    multipleSelection? Bundle.LBL_DataItemChooser_Title_Categories() :
                    Bundle.LBL_DataItemChooser_Title_Category());
        }

        @Override
        protected DataCategory getSelectedElement(Object lastElement) {
            if(lastElement instanceof DataCategory)
                return (DataCategory) lastElement;
            return null;
        }

        @Override
        public DataType getDataType() {
            return null;
        }
    }
    
    private static class SourceController extends AbstractController<DataSource> {
        private final DataType dataType;
        private SourceController(boolean multipleSelection, DataType dataType) {
            super(multipleSelection, true,
                    multipleSelection? Bundle.LBL_DataItemChooser_Title_Sources() :
                    Bundle.LBL_DataItemChooser_Title_Source());
            this.dataType = dataType;
        }

        @Override
        protected DataSource getSelectedElement(Object lastElement) {
            if(lastElement instanceof DataSource)
                return (DataSource) lastElement;
            return null;
        }

        @Override
        public DataType getDataType() {
            return dataType;
        }
    }
}
