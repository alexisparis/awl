/*
 * $Id: JXPanelBeanInfo.java,v 1.5 2007/05/09 15:54:40 kleopatra Exp $
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
package org.jdesktop.swingx;

import org.jdesktop.swingx.editors.PainterPropertyEditor;

/**
 *
 * @author rbair
 */
public class JXPanelBeanInfo extends BeanInfoSupport {
    
    public JXPanelBeanInfo() {
        super(JXPanel.class);
    }

    protected void initialize() {
        setPreferred(true, "opaque", "alpha", "inheritAlpha", "backgroundPainter");
//        setHidden(true, "drawGradient", "gradientPaint");
        setPropertyEditor(PainterPropertyEditor.class, "backgroundPainter");
    }
}
