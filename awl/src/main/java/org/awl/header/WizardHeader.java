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
package org.awl.header;

import java.awt.Component;
import org.awl.WizardPageDescriptor;

/**
 *
 * Define a object that represents the header of a wizard
 *
 * @author alexis
 */
public interface WizardHeader
{
    /** update the content of the panel according to the given descriptor
     *	@param descriptor a WizardPageDescriptor
     */
    public void displayDescriptorInformation(WizardPageDescriptor descriptor);
    
    /** return the component that represents the header
     *	@return a Component
     */
    public Component getComponent();
}
