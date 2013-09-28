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

package org.jreserve.gui.plot;

import java.awt.Component;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jreserve.gui.misc.utils.actions.ClipboardUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ChartWrapperImpl implements ChartWrapper {

    private ChartPanel panel;

    ChartWrapperImpl(ChartPanel panel) {
        this.panel = panel;
    }
    
    @Override
    public Component getChartComponent() {
        return panel;
    }

    @Override
    public ClipboardUtil.Copiable createCopiable() {
        return new ChartCopy();
    }

    private class ChartCopy implements ClipboardUtil.Copiable {

        @Override
        public boolean canCopy() {
            return true;
        }

        @Override
        public Transferable clipboardCopy() throws IOException {
            return new ImageSelection();
        }
    }
    
    private class ImageSelection implements Transferable{
        
        private Image image;
        
        private ImageSelection(){
            JFreeChart chart = panel.getChart();
            int width = panel.getWidth();
            int height = panel.getHeight();
            this.image = chart.createBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB, null);
        }
        
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }
        
        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }       
        
        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) 
                throw new UnsupportedFlavorException(flavor);
            return image;
        }
    }    
}
