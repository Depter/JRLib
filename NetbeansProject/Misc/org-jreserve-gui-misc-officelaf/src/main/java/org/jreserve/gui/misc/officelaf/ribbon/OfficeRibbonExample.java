package org.jreserve.gui.misc.officelaf.ribbon;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.EmptyResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies.Mid2Low;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies.Mid2Mid;
import org.pushingpixels.flamingo.api.ribbon.resize.RibbonBandResizePolicy;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.LifecycleManager;
import org.openide.actions.*;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class OfficeRibbonExample extends JRibbon {
    Result<Action> result;

    public OfficeRibbonExample() {
    }

    public void setup() {
        RibbonTask homeTask = new RibbonTask(NbBundle.getMessage(OfficeRibbonExample.class, "HOME"),
                createClipboard());
        addTask(homeTask);

        /*JRibbonBand tb = new JRibbonBand(NbBundle.getMessage(OfficeRibbonExample.class, "CLIPBOARD"),
                new EmptyResizableIcon(16));
        tb.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                new Mid2Mid(tb.getControlPanel()),
                new Mid2Mid(tb.getControlPanel()),
                new Mid2Low(tb.getControlPanel())
        ));
        RibbonTask t2 = new RibbonTask("Test", tb);
        addTask(t2);*/

        createApplicationMenu();
        createTaskBarButtons();
    }

    private void createApplicationMenu() {
        RibbonApplicationMenu appMenu = new RibbonApplicationMenu();

        result = Lookups.forPath("Menu/File").lookupResult(Action.class);
        /*result.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent le) {
                System.out.println("Menu changed");
                for (Iterator<? extends Action> i = result.allInstances().iterator(); i.hasNext(); ) {
                    Action a = i.next();
                    System.out.println("New Action: " + a.getValue(Action.NAME));
                }
            }
        });*/

        Collection<? extends Action> actions = Lookups.forPath("Menu/File").lookupAll(Action.class);
        for (final Action action : actions) {
            PrimaryMenuEntry primary = null;

            if (action instanceof Presenter.Menu && ((Presenter.Menu) action).getMenuPresenter() instanceof JMenu) {
                primary = new PrimaryMenuEntry(JCommandButton.CommandButtonKind.POPUP_ONLY, ActionUtil.lookupText(action), ActionUtil.lookupIcon(action), action) {
                    @Override
                    public int getSecondaryGroupCount() {
                        return 1;
                    }

                    @Override
                    public String getSecondaryGroupTitleAt(int index) {
                        return getText();
                    }

                    @Override
                    public List<RibbonApplicationMenuEntrySecondary> getSecondaryGroupEntries(int groupIndex) {
                        // TODO Find out why the JMenu always is empty.
                        JMenu menu = (JMenu) ((Presenter.Menu) action).getMenuPresenter();
                        Component[] menuComponents = menu.getMenuComponents();
                        List<RibbonApplicationMenuEntrySecondary> retVal = new ArrayList<RibbonApplicationMenuEntrySecondary>();
                        for (Component c : menuComponents) {
                            if (c instanceof JMenuItem) {
                                final JMenuItem subItem = (JMenuItem) c;
                                Action subAction = subItem.getAction();
                                TwoSizedIcon icon = subAction != null ? ActionUtil.lookupIcon(subAction, false) :
                                        new TwoSizedIcon(subItem.getIcon(), null);
                                retVal.add(new SecondaryMenuEntry(subItem.getText(), subItem.getToolTipText(), icon,
                                        subAction));
                            }
                        }
                        return retVal;
                    }
                };
            }

            if (primary == null) {
                primary = new PrimaryMenuEntry(action);
            }

            appMenu.addMenuEntry(primary);
        }

        appMenu.addFooterEntry(new FooterMenuEntry(new AbstractAction(NbBundle.getMessage(OfficeRibbonExample.class, "OPTIONS")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDisplayer.getDefault().open();
            }
        }));

        appMenu.addFooterEntry(new FooterMenuEntry(new AbstractAction(NbBundle.getMessage(OfficeRibbonExample.class, "EXIT")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LifecycleManager.getDefault().exit();
            }
        }));

        setApplicationMenu(appMenu);
    }

    private void createTaskBarButtons() {
        UndoAction undo = SystemAction.get(UndoAction.class);
        RedoAction redo = SystemAction.get(RedoAction.class);
        SaveAction save = SystemAction.get(SaveAction.class);

        addTaskbarComponent(new BoundCommandButton(save));
        addTaskbarComponent(new BoundCommandButton(undo));
        addTaskbarComponent(new BoundCommandButton(redo));
    }

    private JRibbonBand createClipboard() {
        JRibbonBand band = new JRibbonBand(NbBundle.getMessage(OfficeRibbonExample.class, "CLIPBOARD"),
                new EmptyResizableIcon(16));

        PasteAction paste = SystemAction.get(PasteAction.class);
        CutAction   cut   = SystemAction.get(CutAction.class);
        CopyAction  copy  = SystemAction.get(CopyAction.class);

        band.addCommandButton(new BoundCommandButton(paste), RibbonElementPriority.TOP);
        band.addCommandButton(new BoundCommandButton(cut),   RibbonElementPriority.MEDIUM);
        band.addCommandButton(new BoundCommandButton(copy),  RibbonElementPriority.MEDIUM);

        band.setResizePolicies(Arrays.<RibbonBandResizePolicy>asList(
                new Mid2Mid(band.getControlPanel()),
                new Mid2Mid(band.getControlPanel()),
                new Mid2Low(band.getControlPanel())
        ));

        return band;
    }
}
