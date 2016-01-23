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
package org.awl;

import javax.swing.JOptionPane;

/**
 *
 * Define constants for general wizard purposes
 *
 * @author alexis
 */
public interface WizardConstants
{
    /** id of the descriptor that is the terminal descriptor */
    public static final String TERMINAL_DESCRIPTOR_ID = "terminal";
    
    /** id of the descriptor that is the starting descriptor */
    public static final String STARTING_DESCRIPTOR_ID = "starting";
    
    /** define constants representing the result code of the wizard */
    
    /* define the status of a wizard when finish has been pressed and accepted */
    public static final int    WIZARD_VALID_OPTION    = JOptionPane.OK_OPTION;     // 0
    
    /* define the status of a wizard when close button has been pressed and accepted */
    public static final int    WIZARD_CLOSED_OPTION   = JOptionPane.CLOSED_OPTION; // -1
    
    /* define the status of a wizard when cancel button has been pressed and accepted */
    public static final int    WIZARD_CANCEL_OPTION   = JOptionPane.CANCEL_OPTION; // 2
    
    
}
