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
package org.jreserve.gui.calculations.smoothing.dialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.jrlib.triangle.Triangle;
import org.jreserve.jrlib.triangle.smoothing.SmoothingCell;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractSmoothDialogController<T extends Triangle> implements SmoothDialogController<T> {

    private String title;
    private final ChangeSupport cs = new ChangeSupport(this);
    private boolean canModifyCells = true;
    
    protected AbstractSmoothDialogController(String title) {
        this.title = title;
    }
    
    @Override
    public boolean canModifyCells() {
        return canModifyCells;
    }

    public void setAllowsModifyCells(boolean allow) {
        this.canModifyCells = allow;
    }
    
    protected final List<SmoothingCell> createCells() {
        List<SmoothRecord> records = getRecords();
        List<SmoothingCell> cells = new ArrayList<SmoothingCell>(records.size());
        for(SmoothRecord record : records)
            cells.add(createCell(record));
        return cells;
    }
    
    private SmoothingCell createCell(SmoothRecord record) {
        int accident = record.getAccident();
        int development = record.getDevelopment();
        boolean applied = record.isUsed();
        return new SmoothingCell(accident, development, applied);
    }
    
    @Override
    public String getDialogTitle() {
        return title;
    }
    
    @Override
    public HelpCtx getHelpContext() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }
    
    protected final void fireChange() {
        cs.fireChange();
    }
}
