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

package org.jreserve.gui.data.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "CTL.ClipboardTable.Paste=Paste"
})
class ClipboardTable extends JTable {
    
    private final ClipboardTableModel tableModel = new ClipboardTableModel();
    
    ClipboardTable() {
        setModel(tableModel);
        addKeyListener(new KeyInputListener());
        addMouseListener(new PopupListener());
        setFillsViewportHeight(true);
        setShowGrid(true);
    }
    
    String[][] getValues() {
        return tableModel.getValues();
    }
    
    private void pasteClipboard() {
        tableModel.setText(getClipboardText());
    }
    
    private String getClipboardText() {
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Clipboard cb = tk.getSystemClipboard();
            return (String) cb.getData(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return null;
        }
    }
    
    private class KeyInputListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.isControlDown() && KeyEvent.VK_V == e.getKeyCode())
                pasteClipboard();
        }
    }
    
    private class PopupListener extends MouseAdapter {
        
        private JPopupMenu menu;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.isPopupTrigger())
                showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger())
                showPopup(e);
        }
        
        private void showPopup(MouseEvent e) {
            JPopupMenu popUp = getMenu();
            popUp.show(ClipboardTable.this, e.getX(), e.getY());
        }
        
        private JPopupMenu getMenu() {
            if(menu == null) {
                menu = new JPopupMenu();
                JMenuItem item = new JMenuItem(Bundle.CTL_ClipboardTable_Paste());
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        pasteClipboard();
                    }
                });
                menu.add(item);
            }
            return menu;
        }
    }    
}
