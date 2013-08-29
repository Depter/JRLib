/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ActionSupport {
    
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    private String actionCommand = "action";
    private final Object source;

    ActionSupport(Object source) {
        this.source = source;
    }
    
    public void addActionListener(ActionListener listener) {
        if(listener == null)
            throw new NullPointerException("Can not add null listener!");
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand) {
        if(actionCommand == null)
            throw new NullPointerException("Action command can not be null!");
        this.actionCommand = actionCommand;
    }
    
    public void fireAction() {
        ActionEvent evt = null;
        for(ActionListener listener : listeners.toArray(new ActionListener[listeners.size()])) {
            if(evt == null)
                evt = new ActionEvent(source, 0, actionCommand);
            listener.actionPerformed(evt);
        }
    }
}
