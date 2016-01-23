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

import java.util.List;

/**
 *
 * Exception that could be throwed when a cycle is detected in the pages descriptors relationship
 *
 * @author alexis
 */
public class PagesCycleDetectedException extends Exception
{
    /** list of id that caused the cycle */
    private List ids = null;
    
    /** Creates a new instance of PagesCycleDetectedException
     *  @param ids a list of String descriptors id representing the cycle
     */
    public PagesCycleDetectedException(List ids)
    {   super();
        
        this.ids = ids;
    }

    public String getMessage()
    {   /** create a message with the list */
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("cycle occurs with descriptor id sequence : ");
        buffer.append("[");
        
        if ( this.ids != null )
        {   for(int i = 0; i < this.ids.size(); i++)
            {   buffer.append(this.ids.get(i));
                
                if ( i < this.ids.size() - 1 )
                    buffer.append(", ");
            }
        }
        
        buffer.append("]");
        
        return buffer.toString();
    }
    
}
