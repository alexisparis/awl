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
package org.awl.event;

import java.util.EventListener;
import org.awl.WizardPageDescriptor;
import org.awl.exception.PageDescriptorChangingException;

/**
 *
 * Define method to be warned when something change on a WizardModel
 *
 * @author alexis
 */
public interface WizardModelListener extends EventListener
{
    /** a new descriptor has been added<br/>
     *	always called on the Event dispach thread
     *  @param event a WizardModelEvent
     */
    public void descriptorAdded(WizardModelEvent event);
    
    /** a new descriptor has been removed<br/>
     *	always called on the Event dispach thread
     *  @param event a WizardModelEvent
     */
    public void descriptorRemoved(WizardModelEvent event);
    
    /** method that is called to verify that all WizardModelListener agree to change the current descriptor<br/>
     *	always called on the Event dispach thread
     *  @param currentDescriptor the current descriptor
     *  @param candidate the descriptor we try to set as current descriptor
     *
     *  @exception PageDescriptorChangingException if the listener refused the change
     */
    public void checkCurrentDescriptorChanging(WizardPageDescriptor currentDescriptor, WizardPageDescriptor candidate)
            throws PageDescriptorChangingException;
    
    /** a new descriptor has been set as current descriptor<br/>
     *	always called on the Event dispach thread
     *  @param event a CurrentDescriptorChangedEvent
     */
    public void currentDescriptorChanged(CurrentDescriptorChangedEvent event);
    
}
