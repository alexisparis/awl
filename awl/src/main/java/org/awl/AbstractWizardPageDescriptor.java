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
import java.util.HashMap;
import java.util.Map;
import org.awl.message.MessageLevel;
import org.awl.util.BeanImpl;

/**
 *
 * Abstract implementation of WizardPageDescriptor
 *
 * PropertyChangeListeners can be warned on those properties : <br>
 *  <ul>
 *      <li>WizardPageDescriptor.PROPERTY_ANCESTOR_ID</li>
 *      <li>WizardPageDescriptor.PROPERTY_NEXT_ID</li>
 *      <li>WizardPageDescriptor.PROPERTY_COMPONENT</li>
 *  </ul>
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public abstract class AbstractWizardPageDescriptor extends BeanImpl implements WizardPageDescriptor
{
    /** the title */
private String                  title           = null;
    
    /** the description */
    private String                  description     = null;
    
    /** component owned by this descriptor */
    private Component               component       = null;
    
    /** next id */
    private String                  nextId          = WizardConstants.TERMINAL_DESCRIPTOR_ID;
    
    /** previous id */
    private String                  previousId      = WizardConstants.STARTING_DESCRIPTOR_ID;
    
    /** next allowed */
    private NavigationAuthorization nextAuth        = NavigationAuthorization.DEFAULT;
    
    /** previous allowed */
    private NavigationAuthorization previousAuth    = NavigationAuthorization.DEFAULT;
    
    /** cancel allowed */
    private NavigationAuthorization cancelAuth      = NavigationAuthorization.DEFAULT;
    
    /** finish allowed */
    private NavigationAuthorization finishAuth      = NavigationAuthorization.DEFAULT;
    
    /** map of messages */
    private Map                     messages        = null;
    
    /** Creates a new instance of AbstractWizardPageDescriptor */
    public AbstractWizardPageDescriptor()
    {   }
    
    /** set the title of the page
     *  @param title the title of the page
     */
    public void setTitle(String title)
    {
	boolean equals = false;
	
	if ( title == null )
	{
	    if ( this.getTitle() == null )
	    {
		equals = true;
	    }
	}
	else
	{
	    equals = title.equals(this.getTitle());
	}
	
	if ( ! equals )
	{
	    String old = this.getTitle();
	    this.title = title;
	    
	    this.firePropertyChange(PROPERTY_TITLE, old, this.getTitle());
	}
    }
    
    /** return the title of the page
     *  @return the title of the page
     */
    public String getTitle()
    {
	return this.title;
    }
    
    /** set the description of the page
     *  @param description the description of the page
     */
    public void setDescription(String description)
    {   
	boolean equals = false;
	
	if ( description == null )
	{
	    if ( this.getDescription() == null )
	    {
		equals = true;
	    }
	}
	else
	{
	    equals = description.equals(this.getDescription());
	}
	
	if ( ! equals )
	{
	    String old = this.getDescription();
	    this.description = description;
	    
	    this.firePropertyChange(PROPERTY_DESCRIPTION, old, this.getDescription());
	}
    }
    
    /** return the description of the page
     *  @return the description of the page
     */
    public String getDescription()
    {   return this.description; }

    /**
     * return the id of the descriptor that came before this one
     * 
     * @return an id
     */
    public String getPreviousDescriptorId()
    {   return this.previousId; }

    /**
     * initialize the id of the descriptor that came before this one
     * 
     * @param id an id
     */
    public void setPreviousDescriptorId(String id)
    {   
	String old = this.getPreviousDescriptorId();
	
	boolean equals = false;
	
	if ( id == null )
	{
	    if ( old == null )
	    {
		equals = true;
	    }
	}
	else
	{
	    equals = id.equals(old);
	}
	
	if ( ! equals )
	{
	    this.previousId = id;
	    this.firePropertyChange(PROPERTY_ANCESTOR_ID, old, id);
	}
    }

    /**
     * return the id of the descriptor coming next to this one
     * 
     * @return an id
     */
    public String getNextDescriptorId()
    {   return this.nextId; }

    /**
     * initialize the id of the descriptor coming next to this one
     * 
     * @param id an id
     */
    public void setNextDescriptorId(String id)
    {   
	String old = this.getNextDescriptorId();
	
	boolean equals = false;
	
	if ( id == null )
	{
	    if ( old == null )
	    {
		equals = true;
	    }
	}
	else
	{
	    equals = id.equals(old);
	}
	
	if ( ! equals )
	{
	    this.nextId = id;
	    this.firePropertyChange(PROPERTY_NEXT_ID, old, id);
	}
    }

    /**
     * initialize the component related to this descriptor
     * 
     * @param component a Component
     */
    public void setComponent(Component component)
    {   
	Component old = this.getComponent();
	
	if ( old != component )
	{
	    this.component = component;
	    this.firePropertyChange(PROPERTY_COMPONENT, old, this.component);
	}
    }

    /**
     * return the component related to this descriptor
     * 
     * @return a Component
     */
    public Component getComponent()
    {   return this.component; }
    
    /**
     * Override this method to provide functionality that will be performed just before
     * the panel is to be displayed.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void aboutToDisplayPanel(Wizard wizard)
    {   
	
    }
 
    /**
     * Override this method to perform functionality when the panel itself is displayed.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void displayingPanel(Wizard wizard)
    {   
	
    }
    
    /**
     * Override this method to perform functionality just before the panel is to be
     * hidden.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void aboutToHidePanel(Wizard wizard)
    {   
	
    }
 
    /**
     * Override this method to perform functionality when the panel itself has just been hidden.
     *  @param wizard the Wizard that contains this descriptor
     */    
    public void hidingPanel(Wizard wizard)
    {   
	
    }
    
    /** return next page navigation authorization<br>
     *	this method does not care if the page is the last page, therefore, if it is the last page, 
     *	this method is allowed to return NavigationAuthorization.ALLOWED. The controller have itself to manage page sequence
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getNextPageAuthorization()
    {
	return this.nextAuth;
    }
    
    /** set next page navigation authorization<br>
     *	this method does not care if the page is the last page, therefore, if it is the last page, 
     *	this method is allowed to return true. The controller have itself to manage page sequence
     *	@param authorization a NavigationAuthorization
     */
    public void setNextPageAuthorization(NavigationAuthorization authorization)
    {
	boolean equals = false;
	
	NavigationAuthorization old = this.getNextPageAuthorization();
	
	if ( authorization == null )
	{
	    if ( old == null )
	    {	equals = true; }
	}
	else
	{
	    equals = authorization.equals(old);
	}
	
	if ( ! equals )
	{
	    this.nextAuth = authorization;
	    
	    this.firePropertyChange(PROPERTY_NEXT_ALLOWED, old, this.getNextPageAuthorization());
	}
    }
    
    /** return previous page navigation authorization<br>
     *	this method does not care if the page is the first page, therefore, if it is the first page, 
     *	this method is allowed to return NavigationAuthorization.ALLOWED. The controller have itself to manage page sequence
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getPreviousPageAuthorization()
    {
	return this.previousAuth;
    }
    
    /** set previous pagenavigation authorization<br>
     *	this method does not care if the page is the first page, therefore, if it is the first page, 
     *	this method is allowed to return NavigationAuthorization.ALLOWED. The controller have itself to manage page sequence
     *	@param authorization a NavigationAuthorization
     */
    public void setPreviousPageAuthorization(NavigationAuthorization authorization)
    {
	boolean equals = false;
	
	NavigationAuthorization old = this.getPreviousPageAuthorization();
	
	if ( authorization == null )
	{
	    if ( old == null )
	    {	equals = true; }
	}
	else
	{
	    equals = authorization.equals(old);
	}
	
	if ( ! equals )
	{
	    this.previousAuth = authorization;
	    
	    this.firePropertyChange(PROPERTY_PREVIOUS_ALLOWED, old, this.getPreviousPageAuthorization());
	}
    }
    
    /** set cancelling navigation authorization
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getCancelAuthorization()
    {
	return this.cancelAuth;
    }
    
    /** set cancelling navigation authorization
     *	@param authorization a NavigationAuthorization
     */
    public void setCancelAuthorization(NavigationAuthorization authorization)
    {
	boolean equals = false;
	
	NavigationAuthorization old = this.getCancelAuthorization();
	
	if ( authorization == null )
	{
	    if ( old == null )
	    {	equals = true; }
	}
	else
	{
	    equals = authorization.equals(old);
	}
	
	if ( ! equals )
	{
	    this.cancelAuth = authorization;
	    
	    this.firePropertyChange(PROPERTY_CANCEL_ALLOWED, old, this.getCancelAuthorization());
	}
    }
    
    /** set finishing navigation authorization
     *	@return a NavigationAuthorization
     */
    public NavigationAuthorization getFinishAuthorization()
    {
	return this.finishAuth;
    }
    
    /** set finishing navigation authorization
     *	@param authorization a NavigationAuthorization
     */
    public void setFinishAuthorization(NavigationAuthorization authorization)
    {
	boolean equals = false;
	
	NavigationAuthorization old = this.getFinishAuthorization();
	
	if ( authorization == null )
	{
	    if ( old == null )
	    {	equals = true; }
	}
	else
	{
	    equals = authorization.equals(old);
	}
	
	if ( ! equals )
	{
	    this.finishAuth = authorization;
	    
	    this.firePropertyChange(PROPERTY_FINISH_ALLOWED, old, this.getFinishAuthorization());
	}
    }
    
    /** ########################################################################
     *  ########################### Messages methods ###########################
     *  ######################################################################## */
    
    /** reset all messages for this descriptor */
    public void resetMessages()
    {
	if ( this.messages != null )
	{
	    this.messages.clear();
	    
	    this.firePropertyChange(new WizardPageDescriptor.MessageChangeEvent(this));
	}
    }
    
    /** get the message for this descriptor according to the level
     *	@param level the level of the message
     *	@return a message or null if no message is set
     */
    public String getMessage(MessageLevel level)
    {
	String msg = null;
	
	if ( this.messages != null )
	{
	    msg = (String)this.messages.get(level);
	}
	
	return msg;
    }
    
    /** set a message for this descriptor
     *	@param msg the message
     *	@param level the level of the message
     */
    public void setMessage(String msg, MessageLevel level)
    {
	if ( level != null )
	{
	    if ( msg != null || this.messages != null )
	    {
		if ( this.messages == null )
		{
		    this.messages = new HashMap();
		}
		
		this.messages.put(level, msg);
		
		this.firePropertyChange(new WizardPageDescriptor.MessageChangeEvent(this));
	    }
	}
    }
    
    /** set the info message
     *	@param message
     */
    public void setInfoMessage(String message)
    {
	this.setMessage(message, MessageLevel.INFO);
    }
    
    /** set the error message
     *	@param message
     */
    public void setErrorMessage(String message)
    {
	this.setMessage(message, MessageLevel.ERROR);
    }
    
    /** set the warning message
     *	@param message
     */
    public void setWarnMessage(String message)
    {
	this.setMessage(message, MessageLevel.WARN);
    }
    
    /** set the fatal message
     *	@param message
     */
    public void setFatalMessage(String message)
    {
	this.setMessage(message, MessageLevel.FATAL);
    }
    
}
