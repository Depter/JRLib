package org.jreserve.opaquenimbus;

import javax.swing.*;

/**
 */
class TableHeaderTableHeaderRendererSortedState extends State {
    TableHeaderTableHeaderRendererSortedState() {
        super("Sorted");
    }

    @Override protected boolean isInState(JComponent c) {

                    String sortOrder = (String)c.getClientProperty("Table.sortOrder");
                    return  sortOrder != null && ("ASCENDING".equals(sortOrder) || "DESCENDING".equals(sortOrder)); 
    }
}

