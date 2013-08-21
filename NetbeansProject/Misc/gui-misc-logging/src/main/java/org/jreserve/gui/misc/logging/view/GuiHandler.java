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
package org.jreserve.gui.misc.logging.view;

import org.jreserve.gui.misc.logging.LoggingUtil;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GuiHandler extends Handler {
    
    private static GuiHandler INSTANCE = null;
    
    public static GuiHandler getInstance() {
        if(INSTANCE == null)
            INSTANCE = new GuiHandler(LogviewTopComponent.getDocument());
        return INSTANCE;
    }
    private final StyledDocument document;
    private final Map<Level, Style> styles = new HashMap<Level, Style>();
    
    private GuiHandler(StyledDocument document) {
        super.setLevel(Level.ALL);
        super.setFormatter(new GuiFormatter());
        this.document = document;
        createStyles();
    }
    
    private void createStyles() {
        Style base = document.addStyle("levelBase", null);
        
        Style error = document.addStyle("error", base);
        StyleConstants.setForeground(error, Color.red);
        styles.put(Level.SEVERE, error);
        
        Style warn = document.addStyle("warn", base);
        StyleConstants.setForeground(warn, new Color(227, 77, 2));
        styles.put(Level.WARNING, warn);
        
        Style info = document.addStyle("info", base);
        StyleConstants.setForeground(info, Color.blue);
        styles.put(Level.INFO, info);
        
        Style debug = document.addStyle("debug", base);
        StyleConstants.setForeground(debug, Color.gray);
        StyleConstants.setItalic(debug, true);
        styles.put(Level.FINE, debug);
        
        Style trace = document.addStyle("trace", base);
        StyleConstants.setForeground(trace, Color.gray);
        StyleConstants.setItalic(trace, true);
        styles.put(Level.FINER, trace);
        
        Style verbose = document.addStyle("verbose", base);
        StyleConstants.setForeground(verbose, Color.gray);
        StyleConstants.setItalic(verbose, true);
        styles.put(Level.FINEST, verbose);
        
        Style all = document.addStyle("all", base);
        StyleConstants.setForeground(all, Color.gray);
        StyleConstants.setItalic(all, true);
        styles.put(Level.ALL, all);
    }

    @Override
    public void publish(LogRecord record) {
        String msg = super.getFormatter().format(record);
        Style style = getStyle(record.getLevel());
        publishOnEDT(style, msg);
    }
    
    private Style getStyle(Level level) {
        level = LoggingUtil.escapeLevel(level);
        return styles.get(level);
    }
    
    private void publishOnEDT(final Style style, final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    document.insertString(document.getLength(), msg, style);
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    } 

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
