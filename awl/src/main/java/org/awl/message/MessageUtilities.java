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
package org.awl.message;

import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * Utilities for MessageLevel
 *
 * @author alexis
 */
public class MessageUtilities
{
    /** map linking message level and icons */
    private static Map iconMap = new WeakHashMap(MessageLevel.LEVELS.length);
    
    /** Creates a new instance of MessageUtilities */
    private MessageUtilities()
    {	}
    
    /** return the default icon for the given message level
     *	@param level a MessageLevel
     *	@return an Icon or null
     */
    public static Icon getIconForLevel(MessageLevel level)
    {
	Icon icon = null;
	
	if ( level != null )
	{
	    Object cached = iconMap.get(level);
	    
	    if ( cached instanceof Icon )
	    {
		icon = (Icon)cached;
	    }
	    
	    if ( icon == null )
	    {
		if ( level.equals(MessageLevel.INFO) )
		{
		    icon = new ImageIcon(MessageUtilities.class.getResource("/org/awl/rc/info.png"));
		}
		else if ( level.equals(MessageLevel.WARN) )
		{
		    icon = new ImageIcon(MessageUtilities.class.getResource("/org/awl/rc/warn.png"));
		}
		else if ( level.equals(MessageLevel.ERROR) )
		{
		    icon = new ImageIcon(MessageUtilities.class.getResource("/org/awl/rc/error.png"));
		}
		else if ( level.equals(MessageLevel.FATAL) )
		{
		    icon = new ImageIcon(MessageUtilities.class.getResource("/org/awl/rc/error.png"));
		}
		
		iconMap.put(level, icon);
	    }
	}
	
	return icon;
    }
    
}
