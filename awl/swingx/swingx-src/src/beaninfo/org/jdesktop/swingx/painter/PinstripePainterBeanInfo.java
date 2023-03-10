/*
 * PinstripePainterBeanInfo.java
 *
 * Created on March 21, 2006, 12:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.painter;

import org.jdesktop.swingx.editors.Paint2PropertyEditor;

/**
 *
 * @author Richard
 */
public class PinstripePainterBeanInfo extends AbstractPainterBeanInfo {
    
    /** Creates a new instance of PinstripePainterBeanInfo */
    public PinstripePainterBeanInfo() {
        super(PinstripePainter.class);
    }
    protected void initialize() {
        super.initialize();
        setPreferred(true, "angle", "spacing", "paint");
        setHidden(true, "class", "propertyChangeListeners");
        setPropertyEditor(Paint2PropertyEditor.class, "paint");
    }
}
