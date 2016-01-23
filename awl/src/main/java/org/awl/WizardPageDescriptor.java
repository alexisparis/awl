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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import org.awl.message.MessageLevel;
import org.awl.util.Bean;

/**
 *
 * Descriptor of a panel in a wizard
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public interface WizardPageDescriptor extends Bean, WizardConstants
{   
    /** properties */
    public static final String PROPERTY_ANCESTOR_ID = "ancestorId";
    public static final String PROPERTY_NEXT_ID     = "nextId";
    public static final String PROPERTY_COMPONENT   = "component";
    public static final String PROPERTY_TITLE       = "title";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_MESSAGE     = "message";
    
    public static final String PROPERTY_PREVIOUS_ALLOWED = "previousAllowed";
    public static final String PROPERTY_NEXT_ALLOWED     = "nextAllowed";
    public static final String PROPERTY_FINISH_ALLOWED   = "finishAllowed";
    public static final String PROPERTY_CANCEL_ALLOWED   = "cancelAllowed";
    
    /** return the component related to this descriptor
     *  @return a Component
     */
    public Component getComponent();
    
    /** initialize the component related to this descriptor
     *  @param component a Component
     */
    public void setComponent(Component component);
    
    /** set the title of the page
     *  @param title the title of the page
     */
    public void setTitle(String title);
    
    /** return the title of the page
     *  @return the title of the page
     */
    public String getTitle();
    
    /** set the description of the page
     *  @param description the description of the page
     */
    public void setDescription(String description);
    
    /** return the description of the page
     *  @return the description of the page
     */
    public String getDescription();
    
    /** return the id of the descriptor coming next to this one
     *  @return an id
     */
    public String getNextDescriptorId();
    
    /** return the id of the descriptor that came before this one
     *  @return an id
     */
    public String getPreviousDescriptorId();
    
    /** initialize the id of the descriptor coming next to this one
     *  @param id an id
     */
    public void setNextDescriptorId(String id);
    
    /** initialize the id of the descriptor that came before this one
     *  @param id an id
     */
    public void setPreviousDescriptorId(String id);
    
    /**
     * Override this method to provide functionality that will be performed just before
     * the panel is to be displayed.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void aboutToDisplayPanel(Wizard wizard);
 
    /**
     * Override this method to perform functionality when the panel itself is displayed.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void displayingPanel(Wizard wizard);
    
    /**
     * Override this method to perform functionality just before the panel is to be
     * hidden.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void aboutToHidePanel(Wizard wizard);
 
    /**
     * Override this method to perform functionality when the panel itself has just been hidden.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void hidingPanel(Wizard wizard);
    
    /** return next page navigation authorization<br>
     *	this method does not care if the page is the last page, therefore, if it is the last page, 
     *	this method is allowed to return NavigationAuthorization.ALLOWED. The controller have itself to manage page sequence
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getNextPageAuthorization();
    
    /** set next page navigation authorization<br>
     *	this method does not care if the page is the last page, therefore, if it is the last page, 
     *	this method is allowed to return true. The controller have itself to manage page sequence
     *	@param authorization a NavigationAuthorization
     */
    public void setNextPageAuthorization(NavigationAuthorization authorization);
    
    /** return previous page navigation authorization<br>
     *	this method does not care if the page is the first page, therefore, if it is the first page, 
     *	this method is allowed to return NavigationAuthorization.ALLOWED. The controller have itself to manage page sequence
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getPreviousPageAuthorization();
    
    /** set previous pagenavigation authorization<br>
     *	this method does not care if the page is the first page, therefore, if it is the first page, 
     *	this method is allowed to return NavigationAuthorization.ALLOWED. The controller have itself to manage page sequence
     *	@param authorization a NavigationAuthorization
     */
    public void setPreviousPageAuthorization(NavigationAuthorization authorization);
    
    /** set cancelling navigation authorization
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getCancelAuthorization();
    
    /** set cancelling navigation authorization
     *	@param authorization a NavigationAuthorization
     */
    public void setCancelAuthorization(NavigationAuthorization authorization);
    
    /** set finishing navigation authorization
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getFinishAuthorization();
    
    /** set finishing navigation authorization
     *	@param authorization a NavigationAuthorization
     */
    public void setFinishAuthorization(NavigationAuthorization authorization);
    
    /** messages related methods */
    
    /** reset all messages for this descriptor */
    public void resetMessages();
    
    /** set a message for this descriptor
     *	@param msg the message
     *	@param level the level of the message
     */
    public void setMessage(String msg, MessageLevel level);
    
    /** get the message for this descriptor according to the level
     *	@param level the level of the message
     *	@return a message or null if no message is set
     */
    public String getMessage(MessageLevel level);
    
    /** define a change on messages as a PropertyChangeEvent */
    public static class MessageChangeEvent extends PropertyChangeEvent
    {
	/*  create a new MessageChangeEvent
	 *  @param source source of the event
	 *  
	 */
	public MessageChangeEvent(Object source)
	{
	    // shit
	    super(source, PROPERTY_MESSAGE, null, "a");
	}
    }
    
}
