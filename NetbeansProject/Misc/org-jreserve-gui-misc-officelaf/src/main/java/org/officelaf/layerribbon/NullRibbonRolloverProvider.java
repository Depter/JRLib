/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.officelaf.layerribbon;

import javax.swing.JPanel;
import org.openide.util.lookup.ServiceProvider;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service = RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback.class)
public class NullRibbonRolloverProvider implements RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback {

    @Override
    public void menuEntryActivated(JPanel targetPanel) {
    }
    
}
