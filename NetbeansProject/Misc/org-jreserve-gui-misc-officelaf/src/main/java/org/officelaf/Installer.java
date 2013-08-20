package org.officelaf;

import org.officelaf.spi.RibbonProvider;
import org.officelaf.options.OfficeLAFPanel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.ErrorManager;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {
    private static final Logger LOG = Logger.getLogger(Installer.class.getName());
    private static final Color GRAY_76 = new Color(76,76,76);

    @Override
    public void restored() {
        try {
            setSystemProperties();
            setLookAndFeel();
            SwingUtilities.invokeLater(new RibbonInstaller());
        } catch (Exception e) {
            ErrorManager.getDefault().notify(e);
            throw new RuntimeException(e);
        }
    }
    
    private void setSystemProperties() {
        Boolean toolbars = !NbPreferences.forModule(OfficeLAFPanel.class).getBoolean("toolCheck", false);
        if(toolbars)
            System.setProperty("netbeans.winsys.no_toolbars", toolbars.toString());
        System.setProperty("netbeans.winsys.status_line.path", "LookAndFeel/org-officelaf-StatusBar.instance");
    }
    
    private void setLookAndFeel() throws Exception {
        LookAndFeel lafInstance = getLookAndFeel();
        if (lafInstance != null) {
            setLookAndFeel(lafInstance);
        } else {
            setDummyLookAndFeel();
        }
    }
    
    private LookAndFeel getLookAndFeel() throws Exception {
        LaF laf = LaF.get(System.getProperty("os.name"));
        LOG.log(Level.INFO, "LAF: {0}", laf);
        return laf.createLafInstance();
    }
    
    private void setLookAndFeel(LookAndFeel lafInstance) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(lafInstance);
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames)
            SwingUtilities.updateComponentTreeUI(frame);
    }
    
    private void setDummyLookAndFeel() {
        LOG.log(Level.INFO, "No LAF for {0}", System.getProperty("os.name"));
        OfficeLookAndFeelHelper helper = new OfficeLookAndFeelHelper();
        UIManager.getDefaults().putDefaults(helper.getClassDefaults());
        UIManager.getDefaults().putDefaults(helper.getComponentDefaults());
    }
    
    private class RibbonInstaller implements Runnable {
        @Override
        public void run() {
            JFrame main = (JFrame) WindowManager.getDefault().getMainWindow();
            JPanel orange = new OrangePanel();
            main.setContentPane(orange);
            final OfficeRootPaneUI rootPaneUI = (OfficeRootPaneUI) main.getRootPane().getUI();

            final Lookup.Result<? extends RibbonProvider> r = Lookup.getDefault().lookupResult(RibbonProvider.class);
            if(r.allInstances().size() > 0) {
                updateRibbon(rootPaneUI, r.allInstances().iterator().next());
            } else {
                LOG.log(Level.INFO, "No RibbonProvider found! Listening for changes on default lookup...");
                r.addLookupListener(new LookupListener() {
                    @Override
                    public void resultChanged(LookupEvent ev) {
                        updateRibbon(rootPaneUI, r.allInstances().iterator().next());
                    }
                });
            }
        }

        private void updateRibbon(final OfficeRootPaneUI rootPaneUI, final RibbonProvider provider) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    LOG.log(Level.INFO, "Creating Ribbon from {0}", provider);
                    rootPaneUI.setRibbon(provider.createRibbon());
                }
            });
        }
    }
    
    private static class OrangePanel extends JPanel {
        
        private OrangePanel() {
            super(new BorderLayout());
        }
        
        @Override
        public void add(Component comp, Object constraints) {
            super.add(comp, constraints);
            if(constraints == BorderLayout.CENTER)
                initCenterComponent(comp);
        }
        
        private void initCenterComponent(Component c) {
            c.setBackground(GRAY_76);
            if(c instanceof JPanel)
                ((JPanel)c).setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
