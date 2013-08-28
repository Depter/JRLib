/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.test.scenetest.scene;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Peter Decsi
 */
public class JRLibNode {
    
    private String title;
    private Image img;
    
//    private final DefaultPin defaultPin;
    private Set<JRLibPin> pins = new LinkedHashSet<JRLibPin>();

    public JRLibNode(String title, Image img) {
        this.title = title;
        this.img = img;
//        this.defaultPin = new DefaultPin(this, title);
    }
    
//    public JRLibPin getDefaultPin() {
//        return defaultPin;
//    }
    
    public boolean hasInput() {
        return true;
    }
    
    public boolean hasOutput() {
        return true;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Image getImage() {
        return img;
    }
    
    protected void addPin(JRLibPin pin) {
        if(pin == null)
            throw new NullPointerException("Pin can not be null!");
        if(!pins.add(pin))
            throw new IllegalArgumentException("Pin already added!");
    }
    
    public List<JRLibPin> getPins() {
        return new ArrayList<JRLibPin>(pins);
    }
    
    @Override
    public String toString() {
        return String.format("JRLibNode [%s]", title);
    }
}
