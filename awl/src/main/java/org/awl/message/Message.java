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

/**
 *
 * Describe a message that can be processed by a wizard
 *
 * @author alexis
 */
public class Message
{
    /** content of the message */
    private String       content = null;
    
    /** level */
    private MessageLevel level   = null;
    
    /** Creates a new instance of Message
     *	@param content the content of the message
     *	@param level the level of the message
     */
    public Message(String content, MessageLevel level)
    {
	this.content = content;
	this.level = level;
    }

    /** return the content of the message
     *	@return a String
     */
    public String getContent()
    {
	return content;
    }

    /** return the level of the message
     *	@return a MessageLevel
     */
    public MessageLevel getLevel()
    {
	return level;
    }
    
}
