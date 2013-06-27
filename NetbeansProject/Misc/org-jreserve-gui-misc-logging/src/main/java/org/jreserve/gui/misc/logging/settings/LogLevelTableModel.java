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
package org.jreserve.gui.misc.logging.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import org.jreserve.gui.misc.logging.LoggerProperties;
import org.openide.util.NbBundle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@NbBundle.Messages({
    "LBL.LogLevelTableModel.logger=Logger",
    "LBL.LogLevelTableModel.level=Level",
    "# {0} - level, typed by the user",
    "MSG.LogLevelTableModel.wronglevel=Value \"{0}\" is not a valid level.\nUse a number or one of the following values 'OFF', 'SEVERE','WARNING', 'INFO', 'CONFIG', 'FINE', 'FINER', 'FINEST' or 'ALL'"
})
class LogLevelTableModel extends DefaultTableModel {

    private final static String LEVEL_END = ".level";
    private final static int COLUMN_COUNT = 2;
    final static int COLUMN_LOGGER = 0;
    final static int COLUMN_LEVEL = 1;
    
    private Map<String, Level> levels = new HashMap<String, Level>();
    private List<String> loggers = new ArrayList<String>();
    
    LogLevelTableModel() {
        Properties props = LoggerProperties.getProperties();
    }
    
    void loadProperties(Properties props) {
        deleteRows();
        addLoggerLevels(props);
        int size = loggers.size();
        super.fireTableRowsInserted(0, size);
    }
    
    private void deleteRows() {
        int size = loggers.size();
        loggers.clear();
        levels.clear();
        super.fireTableRowsDeleted(size, size);
    }
    
    private void addLoggerLevels(Properties props) {
        for(String key : props.stringPropertyNames())
            if(isLevelProperty(key))
                addLevelProperty(key, props.getProperty(key));
        Collections.sort(loggers);
    }
    
    private boolean isLevelProperty(String property) {
        return property.endsWith(LEVEL_END) && property.length() > LEVEL_END.length();
    }
    
    private void addLevelProperty(String property, String value) {
        String logger = getLoggerName(property);
        loggers.add(logger);
        levels.put(logger, Level.parse(value));
    }
    
    private String getLoggerName(String property) {
        int index = property.lastIndexOf('.');
        return property.substring(0, index);
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case COLUMN_LOGGER:
                return Bundle.LBL_LogLevelTableModel_logger();
            case COLUMN_LEVEL:
                return Bundle.LBL_LogLevelTableModel_level();
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public int getRowCount() {
        if(loggers == null)
            return 0;
        return loggers.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        String logger = loggers.get(row);
        switch(column) {
            case COLUMN_LOGGER:
                return logger;
            case COLUMN_LEVEL:
                return levels.get(logger);
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return COLUMN_LEVEL == column;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Level level = (Level) value;
        if(level == null)
            return;
        levels.put(loggers.get(row), level);
        super.fireTableCellUpdated(row, column);
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case COLUMN_LOGGER:
                return String.class;
            case COLUMN_LEVEL:
                return Level.class;
            default:
                throw new IllegalArgumentException("Unknown column index: "+column);
        }
    }
    
    void addValue(String logger, Level level) {
        if(!loggers.contains(logger)) {
            loggers.add(logger);
            Collections.sort(loggers);
        }
        levels.put(logger, level);
        int index = loggers.indexOf(logger);
        super.fireTableRowsInserted(index, index);
    }
    
    void deleteRow(int row) {
        String logger = loggers.remove(row);
        levels.remove(logger);
        super.fireTableRowsDeleted(row, row);
    }
    
    void storeLevels(Properties props) {
        for(String logger : loggers)
            props.put(logger+LEVEL_END, levels.get(logger).getName());
    }
}
