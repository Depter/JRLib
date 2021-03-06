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
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
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
package org.jreserve.gui.misc.flamingo.spi;

import org.jreserve.gui.misc.flamingo.modules.LayerRibbonAppMenuProvider;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;

/**
 *
 * @author Chris
 */
@Messages({
    "Hint.AppMenu=Click here to save, print or access other important features of the Netbeans Platform",
    "LBL.AppMenuTitle=Application Menu",
    "Hint.AppMenuHelp=Click on the help button to get more help"
})
public abstract class RibbonAppMenuProvider {

    private final static String IMG_HOME = "org/jreserve/gui/misc/flamingo/modules/";
    private final static String APP_IMG = IMG_HOME+"app-menu.png"; //NOI18N
    private final static String HELP_IMG = IMG_HOME+"help.png"; //NOI18N
    
    public abstract RibbonApplicationMenu createApplicationMenu();

    public RichTooltip createApplicationMenuTooltip() {
        RichTooltip tooltip = new RichTooltip();
        tooltip.setTitle(Bundle.LBL_AppMenuTitle());
        tooltip.addDescriptionSection(Bundle.Hint_AppMenu());
        tooltip.addFooterSection(Bundle.Hint_AppMenuHelp());
        tooltip.setMainImage(ImageUtilities.loadImage(APP_IMG, true));
        tooltip.setFooterImage(ImageUtilities.loadImage(HELP_IMG, true));
        return tooltip;
    }

    protected RibbonApplicationMenuEntryPrimary.PrimaryRolloverCallback createPrimaryRolloverCallback() {
        return null;
    }

    public static RibbonAppMenuProvider getDefault() {
        RibbonAppMenuProvider provider = Lookup.getDefault().lookup(RibbonAppMenuProvider.class);
        if (provider == null) {
            provider = new LayerRibbonAppMenuProvider();
        }
        return provider;
    }
}
