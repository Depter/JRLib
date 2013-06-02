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
package org.jreserve.org.jreserve.gui.misc.expandable.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FlotableComponentButton extends JLabel implements MouseListener {

    private final static Color FOREGROUND = Color.WHITE;
    private final static int BORDER_WIDTH = 1;
    private final static Dimension SIZE = new Dimension(16, 16);
    private Border border;
    private Border emptyBorder = BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
    private boolean opened = true;
    protected boolean pressed = false;
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private String actionCommand = "NavigablePanelButton";

    public FlotableComponentButton() {
        this(FOREGROUND);
    }

    public FlotableComponentButton(Color foreGround) {
        super("");
        initLabel();
        initButton(foreGround);
    }

    private void initLabel() {
        setVerticalTextPosition(JLabel.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    private void initButton(Color foreground) {
        super.setForeground(foreground);
        border = BorderFactory.createLineBorder(foreground, BORDER_WIDTH);
        setOpaque(false);
        setRequestFocusEnabled(false);
        super.setForeground(foreground);
        super.setBorder(emptyBorder);
        addMouseListener(this);
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        border = BorderFactory.createLineBorder(color, BORDER_WIDTH);
    }

    @Override
    public boolean isFocusTraversable() {
        return isRequestFocusEnabled();
    }

    @Override
    public Dimension getSize() {
        return SIZE;
    }

    @Override
    public Dimension getPreferredSize() {
        return SIZE;
    }

    @Override
    public Dimension getMinimumSize() {
        return SIZE;
    }

    @Override
    public Dimension getMaximumSize() {
        return SIZE;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    public void addActionListener(ActionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }

    protected void fireActionEvent() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_FIRST, actionCommand);
        for (ActionListener listener : new ArrayList<ActionListener>(listeners)) {
            listener.actionPerformed(evt);
        }
    }

    @Override
    public void updateUI() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintBorder(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        opened = !opened;
        fireActionEvent();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(border);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(emptyBorder);
    }
}
