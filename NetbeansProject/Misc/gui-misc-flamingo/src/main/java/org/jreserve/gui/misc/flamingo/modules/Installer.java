/*
 * Copyright (c) 2010 Chris Böhme - Pinkmatter Solutions. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Chris Böhme, Pinkmatter Solutions, nor the names of
 *    any contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jreserve.gui.misc.flamingo.modules;

import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import org.jreserve.gui.misc.flamingo.spi.RibbonComponentProvider;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {
    
    private final static Logger logger = Logger.getLogger(Installer.class.getName());
    
    private final static String PROP_NO_NB_TOOLBAR = "netbeans.winsys.no_toolbars"; //NOI18
    private final static String TRUE = "true"; //NOI18
    private final static String FALSE = "false"; //NOI18
    
    private final static String NIMBUS = "Nimbus";
    
    @Override
    public void restored() {
        ToolTipManager.sharedInstance().setInitialDelay(500);
        System.setProperty(PROP_NO_NB_TOOLBAR, TRUE);
        SwingUtilities.invokeLater(new RibbonInstaller());
    }
    
    private static class RibbonInstaller implements Runnable {
        @Override
        public void run() {
            installLF();
            installRibbon();
        }
            
        private void installLF() {
            try {
                if(setNimbusLF()) {
                    updateComponentTree();
                    logger.log(Level.CONFIG, "Installed Nimbus L&F!");
                } else {
                    logger.log(Level.WARNING, "Nimbus L&F not found!");
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Unable to install Nimbus L&F!", ex);
            }
        }
        
        private boolean setNimbusLF() throws Exception {
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if(NIMBUS.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return true;
                }
            return false;
        }
        
        private void updateComponentTree() {
            Frame[] frames = Frame.getFrames();
            for (Frame frame : frames)
                SwingUtilities.updateComponentTreeUI(frame);        
        }
            
        private void installRibbon() {
            UIManager.getDefaults().putDefaults(LAFConfiguration.getClassDefaults());
            JFrame frame = (JFrame) WindowManager.getDefault().getMainWindow();
            JComponent toolbar = RibbonComponentProvider.getDefault().createRibbon();
            frame.getRootPane().setLayout(new RibbonRootPaneLayout(toolbar));
            toolbar.putClientProperty(JLayeredPane.LAYER_PROPERTY, 0);
            frame.getRootPane().getLayeredPane().add(toolbar, 0);
        }
    }
}
