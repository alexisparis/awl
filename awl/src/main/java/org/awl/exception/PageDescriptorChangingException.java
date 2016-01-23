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
package org.awl.exception;

/**
 *
 * Exception that can be throwed when a WizardModelListener refused to change to a given new
 * WizardPageDescriptor.
 *
 * @author alexis
 */
public class PageDescriptorChangingException extends Exception
{
    /** the object that refused the change */
    private Object object = null;
    
    /** Creates a new instance of PageDescriptorChangingException
     *  @param o the object that refused the change
     *  @param message a description of the reason why 'object' refuse the change
     */
    public PageDescriptorChangingException(Object o, String message)
    {   super(message);
        this.object = o;
    }
    
    /** return the object that refused the change
     *  @return an Object
     */
    public Object getRefusingObject()
    {   return this.object; }
    
    
}
