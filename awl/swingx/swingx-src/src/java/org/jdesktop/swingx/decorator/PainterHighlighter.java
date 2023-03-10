/*
 * $Id: PainterHighlighter.java,v 1.2 2007/05/16 11:09:37 kleopatra Exp $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.decorator;

import java.awt.Component;

import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.PainterAware;

/**
 * Highlighter implementation which uses Painter.<p>
 * 
 * NOTE: this will change once the Painter api is stable.
 * 
 * @author Jeanette Winzenburg
 */
public class PainterHighlighter extends AbstractHighlighter {


    private Painter painter;

    /**
     * Instantiates a PainterHighlighter with null painter and 
     * default predicate.
     */
    public PainterHighlighter() {
        this(null);
    }
    
    /**
     * Instantiates a PainterHighlighter with the given Painter and 
     * default predicate.
     * 
     * @param painter the painter to use
     */
    public PainterHighlighter(Painter painter) {
        this(painter, null);
    }

    /**
     * Instantiates a PainterHighlighter with the given painter and 
     * predicate.
     * 
     * @param painter
     * @param predicate
     */
    public PainterHighlighter(Painter painter, HighlightPredicate predicate) {
        super(predicate);
        setPainter(painter);
    }
    

    /**
     * Sets the Painter to use in this Highlighter, may be null.
     * 
     * PENDING: Null indicates to not highlight (?). Or up to subclasses
     * 
     * @param painter
     */
    public void setPainter(Painter painter) {
        this.painter = painter;
        fireStateChanged();
    }

    /**
     * Returns to Painter used in this Highlighter. 
     * 
     * @return the Painter used in this Highlighter, may be null.
     */
    public Painter getPainter() {
        return painter;
    }
    
    /**
     * {@inheritDoc}
     * 
     * Overridden to type check the component. Calls 
     * super if the component is PainterAware, returns the
     * undecorated component otherwise.
     * 
     */
    @Override
    public Component highlight(Component component, ComponentAdapter adapter) {
        if (component instanceof PainterAware) {
            component = super.highlight(component, adapter);
        }
        return component;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation sets the painter if it is not null. Does nothing
     * otherwise.
     */
    @Override
    protected Component doHighlight(Component component,
            ComponentAdapter adapter) {
        // Painter painter = getPainter(component)
        if (painter != null) {
            ((PainterAware) component).setPainter(painter);
        }
        return component;
    }
    

}
