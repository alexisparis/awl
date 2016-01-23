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

/**
 *
 * Define the autorization concerning next, previous, cancel or finish
 *
 * @author alexis
 */
public class NavigationAuthorization
{
    /** indicate that the navigation is allowed */
    public static final NavigationAuthorization ALLOWED   = new NavigationAuthorization(Boolean.TRUE);
    
    /** indicate that the navigation is refused */
    public static final NavigationAuthorization FORBIDDEN = new NavigationAuthorization(Boolean.FALSE);
    
    /** indicate that the navigation is determined by controller */
    public static final NavigationAuthorization DEFAULT   = new NavigationAuthorization(null);
    
    /** authorization */
    private Boolean enabled = null;
    
    /** create a NavigationAuthorization
     *	@param value the boolean value of he authorization
     */
    private NavigationAuthorization(Boolean enabled)
    {
	this.enabled = enabled;
    }
    
    /** return the boolean value related to the authorization
     *	@return a Boolean
     */
    public Boolean getEnableState()
    {
	return this.enabled;
    }
}
