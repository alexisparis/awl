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
package org.awl.test.test3;

import org.awl.DefaultWizardController;
import org.awl.WizardConstants;
import org.awl.WizardPageDescriptor;
import org.awl.exception.PageDescriptorChangingException;

/**
 *
 * @author alexis
 */
public class UglyWizardController extends DefaultWizardController
{
    
    /** Creates a new instance of UglyWizardController */
    public UglyWizardController()
    {   super(); }

    /**
     * method that is called to verify that all WizardModelListener agree to change the current descriptor
     * 
     * @param currentDescriptor the current descriptor
     * @param candidate the descriptor we try to set as current descriptor
     * @exception PageDescriptorChangingException if the listener refused the change
     */
    public void checkCurrentDescriptorChanging(WizardPageDescriptor currentDescriptor, WizardPageDescriptor candidate) throws PageDescriptorChangingException
    {
        super.checkCurrentDescriptorChanging(currentDescriptor, candidate);
        
        System.out.println("candidate next id : " + candidate.getNextDescriptorId());
        
        if ( candidate.getNextDescriptorId().equals(WizardConstants.TERMINAL_DESCRIPTOR_ID) )
            throw new PageDescriptorChangingException(this, "<html>I do not want to <br>change!!! na!!</html>");
        
        if ( candidate.getPreviousDescriptorId().equals("2") && ! currentDescriptor.getNextDescriptorId().equals("3") )
            throw new PageDescriptorChangingException(this, "I do not want to change TOO !!! na!!");
        
    }
    
    
    
}
