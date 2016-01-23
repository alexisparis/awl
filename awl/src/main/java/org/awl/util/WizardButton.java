/* =============================================================================
 * AWL : Another Wizard Library
 * =============================================================================
 *
 * Project Lead:  Alexis Paris
 *
 * (C) Copyright 2006, by Alexis Paris.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
package org.awl.util;

import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * JButton that is used inside a Wizard.
 *
 * This kind of button is related to ButtonCaracteristics
 *
 * @author alexis
 */
public class WizardButton extends JButton //implements PropertyChangeListener
{   
    /** Creates a new instance of WizardButton */
    public WizardButton()
    {   super();
        this.setIconTextGap(1);
        
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
							  BorderFactory.createEmptyBorder(1, 2, 1, 2)));
    }
    
}
