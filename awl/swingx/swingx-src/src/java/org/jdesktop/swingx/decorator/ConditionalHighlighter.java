/*
 * $Id: ConditionalHighlighter.java,v 1.15 2007/05/16 11:09:37 kleopatra Exp $
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

/**
 * ConditionalHighlighter.
 * 
 * 
 * @author Ramesh Gupta
 * 
 * @deprecated use a AbstractHighlighter with HighlightPredicate instead
 * 
 */
public abstract class ConditionalHighlighter extends LegacyHighlighter {
    protected int               testColumn = 0;         // always in model coordinates
    protected int               highlightColumn = -1;   // always in model coordinates

    // TODO JW ?? - changed from 255 to 256 to not be "on" by default...
    protected int               mask = 256;

    public ConditionalHighlighter() {
        // default constructor
    }

    /**
     * <p>Constructs a <code>ConditionalHighlighter</code> instance with the
     * specified background and foreground colors that will be used to highlight
     * the renderer component for a cell in the specified highlightColumn of any
     * row if and only if {@link #needsHighlight needsHighlight} returns true
     * for the adapter that identifies that cell.</p>
     *
     * @param cellBackground background color for highlighted cells, or null, if
     *          background should not be changed
     * @param cellForeground foreground color for highlighted cells, or null, if
     *          foreground should not be changed
     * @param testColumn column whose value is to be tested to determine if a
     *          cell <em>should</em> be highlighted
     * @param highlightColumn column whose index is used to determine if a cell
     *          <em>could</em> be highlighted; may be a valid column index in model
     *          coordinates, or -1 to indicate all columns
     */
    public ConditionalHighlighter(Color cellBackground, Color cellForeground,
                                  int testColumn, int highlightColumn) {
        super(cellBackground, cellForeground);
        this.testColumn = testColumn;
        this.highlightColumn = highlightColumn;
    }

    /**
     * TODO: JW ??
     */
    public void setMask(int alpha) {
        mask = alpha;
        fireStateChanged();
    }

    /**
     * TODO: JW ??
     * @return mask
     */
    public int getMask() {
        return mask;
    }

    /**
     * Performs a conditional highlight. Calls {@link #doHighlight doHighlight} if
     * and only if {@link #needsHighlight needsHighlight} returns true.
     *
     * @param renderer
     * @param adapter
     * @return the highlighted component
     */
    @Override
    public Component highlight(Component renderer, ComponentAdapter adapter) {
        if (needsHighlight(adapter)) {
            return doHighlight(renderer, adapter);
        }
        else if (getMask() < 256) {
            return doMask(renderer, adapter);
        }
        return renderer;
    }

    /**
     * TODO: ??
     * 
     * @param renderer
     * @param adapter
     * @return renderer
     */
    protected Component doMask(Component renderer, ComponentAdapter adapter) {

        maskBackground(renderer, adapter);
        maskForeground(renderer, adapter);
        // and so on...

        return renderer;
    }

    /**
     * TODO: ??
     */
    protected void maskBackground(Component renderer, ComponentAdapter adapter) {
        Color seed = renderer.getBackground();
        Color color = adapter.isSelected() ? computeSelectedBackground(seed) : seed;
        // fix issue#21-swingx: foreground of renderers can be null
        if (color != null) {
            renderer.setBackground(
                               new Color((getMask() << 24) | (color.getRGB() & 0x00FFFFFF), true));
        }
    }

    /**
     * TODO: ??
     * @param renderer
     * @param adapter
     */
    protected void maskForeground(Component renderer, ComponentAdapter adapter) {
        Color seed = renderer.getForeground();
        Color color = adapter.isSelected() ? computeSelectedForeground(seed) : seed;
        // fix issue#21-swingx: foreground of renderers can be null
        if (color != null) {
            renderer.setForeground(
                               new Color((getMask() << 24) | (color.getRGB() & 0x00FFFFFF), true));
    
        }
    }
   
    @Override
    @Deprecated
    protected Color computeSelectedForeground(Color seed) {
        return getSelectedForeground() == null ? 
                seed == null ? null : seed.brighter() : getSelectedForeground();
    }

//------ following methods are the core of the "conditional" notion
    
    /**
     * Returns the index of the column to use for deciding
     * if the highlight should be on.
     * 
     * @return the index of the column to use for deciding about highlighting.
     *   The index is in model coordinates, -1 denotes any column.
     *    
     * @see #setTestColumnIndex(int)
     *  
     */
    public int getTestColumnIndex() {
        return testColumn;
    }

    /**
     * Sets the index of the column to use for deciding
     * if the highlight should be on.
     * 
     * @param columnIndex index of the column to used for deciding about highlighting. 
     *   The index is in model coordinates, -1 denotes any column.
     */
    public void setTestColumnIndex(int columnIndex) {
        this.testColumn = columnIndex;
        fireStateChanged();
    }

    /**
     * Returns the index of the column to highlight if the 
     * condition is met.
     * 
     * @return the index of the column to highlight.
     *   The index is in model coordinates, -1 denotes any column.
     *    
     * @see #setHighlightColumnIndex(int)
     *  
     */
    public int getHighlightColumnIndex() {
        return highlightColumn;
    }

    /**
     * Sets the index of the column to highlight if the 
     * condition is met.
     * 
     * @param columnIndex index of the column to highlight.
     *   The index is in model coordinates, -1 denotes every column.
     *  
     */
    public void setHighlightColumnIndex(int columnIndex) {
        this.highlightColumn = columnIndex;
        fireStateChanged();
    }

    /**
     * Checks if the cell identified by the specified adapter is a potential
     * candidate for highlighting, and returns true if so; otherwise, it returns false.
     *
     * @param adapter
     * @return true if the cell identified by the specified adapter needs
     *          highlight; false otherwise
     */
    protected boolean needsHighlight(ComponentAdapter adapter) {
        // Before running test(), quickly check if the cell in the current
        // adapter is a potential candidate for highlighting. If so, run the test.
        // highlightColumn is always in "model" coordinates, but adapter.column
        // is in "view" coordinates. So, convert before comparing.
        if ((highlightColumn < 0) ||
            (highlightColumn == adapter.viewToModel(adapter.column))) {
            return test(adapter);
        }
        return false;   // cell is not a candidate for decoration;
    }

    /**
     * Tests if the cell identified by the specified adapter should actually be
     * highlighted, and returns true if so; otherwise, it returns false.
     *
     * @param adapter
     * @return true if the test succeeds; false otherwise
     */
    protected abstract boolean test(ComponentAdapter adapter);

}
