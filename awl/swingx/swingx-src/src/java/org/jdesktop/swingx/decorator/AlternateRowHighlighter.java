/*
 * $Id: AlternateRowHighlighter.java,v 1.8 2007/05/16 11:09:37 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx.decorator;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.UIManager;

/**
 * AlternateRowHighlighter prepares a cell renderer to use different background
 * colors for alternating rows in a data view. The size (in terms of row count) of
 * an alternating group is configurable (defaults to 1);
 *
 * @author Ramesh Gupta
 * 
 * @deprecated use {@link HighlighterFactory} instead
 */
public class AlternateRowHighlighter extends LegacyHighlighter {
    private final static Color  defaultOddRowColor = Color.white;
    private final static Color  defaultEvenRowColor = new Color(0xF0, 0xF0, 0xE0);

    public final static LegacyHighlighter beige =
        new AlternateRowHighlighter(Color.white, new Color(245, 245, 220), null, true);

    public final static LegacyHighlighter linePrinter =
        new AlternateRowHighlighter(Color.white, new Color(0xCC, 0xCC, 0xFF), null, true);

    public final static LegacyHighlighter classicLinePrinter =
        new AlternateRowHighlighter(Color.white, new Color(0xCC, 0xFF, 0xCC), null, true);

    public final static LegacyHighlighter floralWhite =
        new AlternateRowHighlighter(Color.white, new Color(255, 250, 240), null, true);

    public final static LegacyHighlighter quickSilver =
        new AlternateRowHighlighter(Color.white, defaultEvenRowColor, null, true);
    
    public final static AlternateRowHighlighter genericGrey = 
        new AlternateRowHighlighter(Color.white, new Color(229, 229, 229), null, true);

    private Color oddRowBackground = defaultOddRowColor;
    private Color evenRowBackground = defaultEvenRowColor;
    private int linesPerGroup = 1; 

    /**
     * Constructs a default <code>AlternateRowHighlighter</code> that prepares a
     * cell renderer to paint a white background for odd rows and a silver
     * <code>(0xF0, 0xF0, 0xE0)</code> background for even rows.
     */
    public AlternateRowHighlighter() {
    }

    /**
     * Constructs an <code>AlternateRowHighlighter</code> that prepares a
     * cell renderer to paint the specified background colors for odd and even
     * and even rows. A foreground color may also be specified. If null is
     * specified for the foreground color, the foreground color for the renderer
     * is unchanged. Otherwise, it is set to the specified foreground color for
     * both odd and even rows.
     *
     * @param oddRowBackground
     * @param evenRowBackground
     * @param foreground
     */
    public AlternateRowHighlighter(Color oddRowBackground,
                                   Color evenRowBackground, Color foreground) {
        this(oddRowBackground, evenRowBackground, foreground, false);
    }

    public AlternateRowHighlighter(Color oddRowBackground,
            Color evenRowBackground, Color foreground, boolean immutable) {
        super(oddRowBackground, foreground, immutable); // same background for odd and even
        this.oddRowBackground = oddRowBackground;
        this.evenRowBackground = evenRowBackground;
    }

    /**
     * Returns the background color for odd rows, or null if the background color
     * of the cell renderer should be left unchanged for odd rows.
     *
     * @return the background color for odd rows, or null if the background color
     * of the cell renderer should be left unchanged for odd rows
     */
    public Color getOddRowBackground() {
        return oddRowBackground;
    }

    /**
     * Sets the background color for odd rows to the specified color. If null is
     * specified, the background color for odd rows is left unchanged in the
     * renderer

     *
     * @param color the background color for odd rows, or null if the background
     * color of the cell renderer should be left unchanged for odd rows
     */
    public void setOddRowBackground(Color color) {
        if (isImmutable()) return;
        oddRowBackground = color;
        fireStateChanged();
    }

    /**
     * Returns the background color for even rows, or null if the background color
     * of the cell renderer should be left unchanged for even rows.
     *
     * @return the background color for even rows, or null if the background color
     * of the cell renderer should be left unchanged for even rows
     */
    public Color getEvenRowBackground() {
        return evenRowBackground;
    }

    /**
     * Sets the background color for even rows to the specified color. If null is
     * specified, the background color for even rows is left unchanged in the
     * renderer.
     *
     * @param color the background color for even rows, or null if the background
     * color of the cell renderer should be left unchanged for even rows
     */
    public void setEvenRowBackground(Color color) {
        if (isImmutable()) return;
        evenRowBackground = color;
        fireStateChanged();
    }


    @Override
    protected Color computeUnselectedBackground(Component renderer, ComponentAdapter adapter) {
        return isOddRow(adapter) ?
                oddRowBackground : evenRowBackground;
    }

    /**
     * @param adapter
     * @return true if the adapter's row should be colored with the oddRowBackground,
     *          false is not
     */
    protected boolean isOddRow(ComponentAdapter adapter) {
        return ((adapter.row / getLinesPerGroup()) % 2) == 0;
    }


    public int getLinesPerGroup() {
        return linesPerGroup;
    }
    
    public void setLinesPerGroup(int linesPerGroup) {
        if (isImmutable()) return;
        this.linesPerGroup = Math.max(1, linesPerGroup);
        fireStateChanged();
    }
    
    /**
     * @deprecated use HighlighterFactory.UIColorHighlighter
     * 
     */
    public static class UIAlternateRowHighlighter extends AlternateRowHighlighter 
       implements UIHighlighter {

        private HashMap<Color, Color> colorMap;
        public UIAlternateRowHighlighter() {
            super(Color.WHITE, null, null);
            initColorMap();
            updateUI();
        }
        
 
        public void updateUI() {
            
            Color selection = UIManager.getColor("Table.selectionBackground");
            Color highlight = getMappedColor(selection);
            
            setEvenRowBackground(highlight);
        }

        private Color getMappedColor(Color selection) {
            Color color = colorMap.get(selection);
            if (color == null) {
                color = AlternateRowHighlighter.genericGrey.getEvenRowBackground();
            }
            return color;
        }
        /** 
         * this is a hack until we can think about something better!
         * we map all known selection colors to highlighter colors.
         *
         */
        private void initColorMap() {
            colorMap = new HashMap<Color, Color>();
            // Ocean
            colorMap.put(new Color(184, 207, 229), new Color(230, 238, 246));
            // xp blue
            colorMap.put(new Color(49, 106, 197), new Color(224, 233, 246));
            // xp silver
            colorMap.put(new Color(178, 180, 191), new Color(235, 235, 236));
            // xp olive
            colorMap.put(new Color(147, 160, 112), new Color(228, 231, 219));
            // win classic
            colorMap.put(new Color(10, 36, 106), new Color(218, 222, 233));
            // win 2k?
            colorMap.put(new Color(0, 0, 128), new Color(218, 222, 233));
            // default metal
            colorMap.put(new Color(205, 205, 255), new Color(235, 235, 255));
            // mac OS X
            colorMap.put(new Color(56, 117, 215), new Color(237, 243, 254));
            
        }
        
    }

}
