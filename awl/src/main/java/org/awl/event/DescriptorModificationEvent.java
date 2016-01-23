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
 * You should have received a copy of the paGNU Lesser General Public License
 * along with this library;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
package org.awl.event;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;
import org.awl.WizardPageDescriptor;

/**
 *
 * Define a modification of a WizardPageDescriptor
 *
 * @author alexis
 */
public class DescriptorModificationEvent extends EventObject
{
    /** descriptor modified */
    private WizardPageDescriptor descriptor  = null;
    
    /** description of the modification */
    private PropertyChangeEvent  changeEvent = null;
    
    /** Creates a new instance of DescriptorModificationEvent
     *	@param source the source of the modification
     *	@param descriptor a WizardPageDescriptor
     *	@param changeEvent a PropertyChangeEvent
     */
    public DescriptorModificationEvent(Object source, WizardPageDescriptor descriptor, PropertyChangeEvent changeEvent)
    {
	super(source);
	
	this.descriptor  = descriptor;
	this.changeEvent = changeEvent;
    }
    
    /** return the page descriptor affected by the modification
     *	@return a WizardPageDescriptor
     */
    public WizardPageDescriptor getPageDescriptor()
    {
	return this.descriptor;
    }
    
    /** return the event describing the modification
     *	@return a PropertyChangeEvent
     */
    public PropertyChangeEvent getChangeEvent()
    {
	return this.changeEvent;
    }
    
}
