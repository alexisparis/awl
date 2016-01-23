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

import java.util.EventObject;
import org.awl.WizardPageDescriptor;
import org.awl.model.WizardModel;

/**
 *
 * @author alexis
 */
public class CurrentDescriptorChangedEvent extends EventObject
{   
    /* old descriptor */
    private WizardPageDescriptor oldDesc = null;
    
    /* new descriptor */
    private WizardPageDescriptor newDesc = null;
    
    /** Creates a new instance of CurrentDescriptorChangedEvent
     *  @param model a WizardModel
     *  @param oldDescriptor a WizardPageDescriptor
     *  @param newDescriptor a WizardPageDescriptor
     */
    public CurrentDescriptorChangedEvent(WizardModel model, WizardPageDescriptor oldDescriptor,
                                                            WizardPageDescriptor newDescriptor)
    {   super(model);
        this.oldDesc = oldDescriptor;
        this.newDesc = newDescriptor;
    }

    /** return the model responsible for such change
     *  @return a WizardModel
     */
    public WizardModel getModel()
    {   return (WizardModel)this.getSource(); }

    /** return the old current descriptor
     *  @return a WizardPageDescriptor
     */
    public WizardPageDescriptor getOldDesc()
    {   return oldDesc; }

    /** return the new current descriptor
     *  @return a WizardPageDescriptor
     */
    public WizardPageDescriptor getNewDesc()
    {   return newDesc; }
    
}
