package org.jreserve.gui.misc.officelaf;

import org.jreserve.gui.misc.officelaf.spi.RibbonProvider;
import org.jreserve.gui.misc.officelaf.options.OfficeLAFPanel;
import org.openide.ErrorManager;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {
    private static final Logger LOG = Logger.getLogger(Installer.class.getName());
    private static final Color GRAY_76 = new Color(76,76,76);

    enum Laf {
        WINDOWS("windows", "org.jreserve.gui.misc.officelaf.OfficeWindowsLookAndFeel"),
        //LINUX("linux",     "org.jreserve.gui.misc.officelaf.OfficeNimbusLookAndFeel"),
        LINUX("linux",     "org.jreserve.gui.misc.officelaf.OfficeMetalLookAndFeel"),
        //LINUX("linux",     "org.jreserve.gui.misc.officelaf.OfficeGTKLookAndFeel"),
        MAC("mac os x",    "org.jreserve.gui.misc.officelaf.OfficeAquaLookAndFeel"),
        //MAC("mac os x",    "org.jreserve.gui.misc.officelaf.OfficeMetalLookAndFeel"),
        DEFAULT("default", null);

        private String osName;
        private String lafName;

        Laf(String osName, String lafName) {
            this.osName  = osName;
            this.lafName = lafName;
        }

        public static Laf get(String osName) {
            for (Laf laf : EnumSet.allOf(Laf.class)) {
                if (osName.toLowerCase().indexOf(laf.getOsName().toLowerCase()) == 0) {
                    return laf;
                }
            }
            return DEFAULT;
        }

        public String getOsName() {
            return osName;
        }                                                   

        public String getLafName() {
            return lafName;
        }

        public LookAndFeel createLafInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            if (lafName == null) {
                return null;
            }
            Class lafClass = Class.forName(lafName);
            return (LookAndFeel) lafClass.newInstance();
        }


        @Override
        public String toString() {
            return osName + ": " + lafName;
        }
    }

    @Override
    public void restored() {
        try {
            Boolean toolbars = !NbPreferences.forModule(OfficeLAFPanel.class).getBoolean("toolCheck", false);
            if(toolbars) {
                System.setProperty("netbeans.winsys.no_toolbars", toolbars.toString());
            }
            
            System.setProperty("netbeans.exception.report.min.level", "99999");
            System.setProperty("netbeans.winsys.status_line.path", "LookAndFeel/org-jreserve-gui-misc-officelaf-StatusBar.instance");

            Laf laf = Laf.get(System.getProperty("os.name"));
            LOG.log(Level.INFO, "LAF: {0}", laf);
            LookAndFeel lafInstance = laf.createLafInstance();
            if (lafInstance != null) {
                UIManager.setLookAndFeel(lafInstance);

                Frame[] frames = Frame.getFrames();
                for (Frame frame : frames) {
                    SwingUtilities.updateComponentTreeUI(frame);
                }
            } else {
                LOG.log(Level.INFO, "No LAF for {0}", System.getProperty("os.name"));
                OfficeLookAndFeelHelper helper = new OfficeLookAndFeelHelper();
                UIManager.getDefaults().putDefaults(helper.getClassDefaults());
                UIManager.getDefaults().putDefaults(helper.getComponentDefaults());
            }

//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    JFrame main = (JFrame) WindowManager.getDefault().getMainWindow();
//
//                    JPanel orange = new JPanel(new BorderLayout()) {
//                        @Override
//                        public void add(Component comp, Object constraints) {
//                            super.add(comp, constraints);
//                            if (constraints == BorderLayout.CENTER) {
//                                comp.setBackground(GRAY_76);
//                                if(comp instanceof JPanel) {
//                                    JPanel panel = (JPanel)comp;
//                                    panel.setBorder(BorderFactory.createEmptyBorder());
//                                }
//                            }
//                        }
//                    };
//                    main.setContentPane(orange);
//                    final OfficeRootPaneUI rootPaneUI = (OfficeRootPaneUI) main.getRootPane().getUI();
//
//                    final Lookup.Result<? extends RibbonProvider> r = Lookup.getDefault().lookupResult(RibbonProvider.class);
//                    if (r.allInstances().size() > 0) {
//                        updateRibbon(rootPaneUI, r.allInstances().iterator().next());
//                    } else {
//                        r.addLookupListener(new LookupListener() {
//                            @Override
//                            public void resultChanged(LookupEvent ev) {
//                                updateRibbon(rootPaneUI, r.allInstances().iterator().next());
//                            }
//                        });
//                    }
//                }
//            });
        } catch (Exception e) {
            ErrorManager.getDefault().notify(e);
            throw new RuntimeException(e);
        }
    }

//    private void updateRibbon(final OfficeRootPaneUI rootPaneUI, final RibbonProvider provider) {
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                rootPaneUI.setRibbon(provider.createRibbon());
//            }
//        };
//
//        if (SwingUtilities.isEventDispatchThread()) {
//            r.run();
//        } else {
//            SwingUtilities.invokeLater(r);
//        }
//    }
}
