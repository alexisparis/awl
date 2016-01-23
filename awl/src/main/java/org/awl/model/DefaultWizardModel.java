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
package org.awl.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.awl.*;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.event.DescriptorModificationEvent;
import org.awl.event.DescriptorModificationListener;
import org.awl.event.WizardModelEvent;
import org.awl.exception.PageDescriptorChangingException;
import org.awl.exception.PagesCycleDetectedException;
import org.awl.exception.UnregisteredDescriptorException;

/**
 *
 * Default implementation of WizardModel.
 * it manages descriptor in a map.
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public class DefaultWizardModel extends AbstractWizardModel implements PropertyChangeListener
{
    /** map linking id to their descriptor */
//    private Map<String, WizardPageDescriptor> map          = new HashMap<String, WizardPageDescriptor>();
    private Map map          = new HashMap();
    
    /** current descriptor */
    private WizardPageDescriptor             current       = null;
    
    /** Creates a new instance of DefaultWizardModel */
    public DefaultWizardModel()
    {   }
    
    /** method called to reinitialize the model
     *  this method is called when the wizard has finished (coz user hit finish, cancel or close button )
     *  the model should select the initial root descriptor
     */
    public void reinit()
    {   /** loop on all descriptor and set as current the one which previous is STARTING_DESCRIPTOR_ID */
        if ( this.map != null )
        {   Iterator ids = this.map.keySet().iterator();
            
            while(ids.hasNext())
            {   String currentId = (String)ids.next();
                
                WizardPageDescriptor desc = (WizardPageDescriptor)this.map.get(currentId);
                
                if ( desc != null && WizardConstants.STARTING_DESCRIPTOR_ID.equals(desc.getPreviousDescriptorId()) )
                {   try
                    {   this.setCurrentDescriptor(currentId);
                        break;
                    }
                    catch(Exception e)
                    {   e.printStackTrace(); }
                }
            }
        }
    }

    /**
     * set the current descriptor by giving its id
     * 
     * @param id the id of the descriptor to set as current descriptor
     *
     * @exception UnregisteredDescriptorException when trying to access to an unregistered descriptor
     * @exception PageDescriptorChangingException if descriptor could not be set as current descriptor
     */
    public void setCurrentDescriptor(String id) throws UnregisteredDescriptorException, PageDescriptorChangingException
    {   
        if ( ! WizardPageDescriptor.TERMINAL_DESCRIPTOR_ID.equals(id) )
        {   WizardPageDescriptor descriptor = (WizardPageDescriptor)this.map.get(id);

            if ( descriptor == null )
                throw new UnregisteredDescriptorException(id);
            else
                this.setCurrentDescriptor(descriptor);
        }
        else
        {   /* warn listeners that have the right to refuse the change */
            this.fireCheckCurrentDescriptorChanging(this.getCurrentDescriptor(), null);
        }
    }

    /**
     * set the current descriptor
     * 
     * @param descriptor a WizardPageDescriptor
     *
     *  @exception PageDescriptorChangingException if descriptor could not be set as current descriptor
     */
    protected void setCurrentDescriptor(WizardPageDescriptor descriptor) throws PageDescriptorChangingException
    {   
        if ( descriptor == null )
            throw new IllegalArgumentException("descriptor must be non null");
        
        if ( this.getCurrentDescriptor() != descriptor )
        {   WizardPageDescriptor old = this.getCurrentDescriptor();
            
            /* warn listeners that have the right to refuse the change */
            this.fireCheckCurrentDescriptorChanging(this.getCurrentDescriptor(), descriptor);
            
            this.current = descriptor;

            this.fireCurrentDescriptorChanged(new CurrentDescriptorChangedEvent(this, old, this.current));
        }
    }
    
    /** return the first descriptor of the wizard
     *  @return an instance of WizardPageDescriptor
     *		or null if there is actually no page with previous id
     *		equals to WizardConstants.STARTING_DESCRIPTOR_ID
     */
    public WizardPageDescriptor getFirstDescriptor()
    {
	WizardPageDescriptor desc = null;
	
	if ( this.map != null )
	{
	    Iterator it = this.map.values().iterator();
	    
	    while(it.hasNext())
	    {
		Object current = it.next();
		
		if ( current instanceof WizardPageDescriptor )
		{
		    String previousId = ((WizardPageDescriptor)current).getPreviousDescriptorId();
		    
		    if ( WizardConstants.STARTING_DESCRIPTOR_ID.equals(previousId) )
		    {
			desc = (WizardPageDescriptor)current;
			break;
		    }
		}
	    }
	}
	
	
	return desc;
    }
    
    /** return the last descriptor of the wizard<br/>
     *	if there are severall descriptors that convey as last descriptor,
     *	the returned descriptor should be one of those descriptors without any other constraint
     *  @return an instance of WizardPageDescriptor
     *		or null if there is actually no page with previous id
     *		equals to WizardConstants.TERMINAL_DESCRIPTOR_ID
     */
    public WizardPageDescriptor getLastDescriptor()
    {
	WizardPageDescriptor desc = null;
	
	if ( this.map != null )
	{
	    Iterator it = this.map.values().iterator();
	    
	    while(it.hasNext())
	    {
		Object current = it.next();
		
		if ( current instanceof WizardPageDescriptor )
		{
		    String nextId = ((WizardPageDescriptor)current).getNextDescriptorId();
		    
		    if ( WizardConstants.TERMINAL_DESCRIPTOR_ID.equals(nextId) )
		    {
			desc = (WizardPageDescriptor)current;
			break;
		    }
		}
	    }
	}
	
	
	return desc;
    }
    
    /** return the descriptor that represent at a given time the root descriptor according to
     *  the current descriptor
     *  @return the root WizardPageDescriptor or null if failed
     */
    public WizardPageDescriptor getRootDescriptor()
    {   WizardPageDescriptor root = null;
        WizardPageDescriptor current = this.getCurrentDescriptor();
        if ( current != null )
        {   root = current;
            
            /* loop until error occured or if we finally found the root descriptor */
            while(root != null && ! WizardConstants.STARTING_DESCRIPTOR_ID.equals(root.getPreviousDescriptorId()) )
            {   String previous = root.getPreviousDescriptorId();
                
                root = this.getDescriptor(previous);
            }
            
        }
        return root;
    }
    
    /** return a List of descriptor which describe the path of descriptor which contains current descriptor.
     *  descriptors before and after current descriptor are respectively keeped according to next/prev relationship
     *
     *  @return a list of WizardPageDescriptor
     *
     *  @exception PagesCycleDetectedException if a cycle is detected
     */
    public List getCurrentPathDescriptor() throws PagesCycleDetectedException
    {   
        List path = null;
        
        if ( this.getPageCount() >= 1 )
        {
            /** initailize path */
            path = new ArrayList();
            
            /* get the root descriptor and traverse to final */
            WizardPageDescriptor root = this.getRootDescriptor();

            /** set of id to check for cycle */
            List cycleList = new ArrayList(this.getPageCount());
            
            while(root != null)
            {   
                path.add(root);

                String next = root.getNextDescriptorId();

                /* cycle management */
                if ( cycleList.contains(next) )
                {   throw new PagesCycleDetectedException(cycleList); }
                else
                {   cycleList.add(next); }

                if ( WizardConstants.TERMINAL_DESCRIPTOR_ID.equals(next) )
                    break;
                else
                    root = this.getDescriptor(next);
            }
        }
        if ( path == null )
            path = Collections.EMPTY_LIST;
        
        return Collections.unmodifiableList(path);
    }
    
    /**
     * register a new WizardPanelDescriptor    
     * 
     * @param id the id of the WizardPanelDescriptor. id have to be non null and must be different from <br>
     *  WizardConstants.TERMINAL_DESCRIPTOR_ID and from WizardConstants.STARTING_DESCRIPTOR_ID.
     * @param descriptor an instanceof WizardPanelDescriptor
     */
    public void registerWizardPanel(String id, WizardPageDescriptor descriptor)
    {   
        if ( id == null || id.equals(WizardConstants.STARTING_DESCRIPTOR_ID) || id.equals(WizardConstants.TERMINAL_DESCRIPTOR_ID) )
            throw new IllegalArgumentException("id must be non null and not equals to '" + WizardConstants.TERMINAL_DESCRIPTOR_ID +
                                                "' and not equals to '" + WizardConstants.STARTING_DESCRIPTOR_ID + "'");
	
	boolean alreadyContained = this.map.containsValue(descriptor);
        
        if ( descriptor != null )
        {   this.map.put(id, descriptor);
	    
	    if ( ! alreadyContained )
	    {
		descriptor.addPropertyChangeListener(this);
	    }
            
            this.fireDescriptorAdded(new WizardModelEvent(this, id, descriptor));
            
            /** if id of this is starting id, then we set it as the current descriptor */
            if ( WizardConstants.STARTING_DESCRIPTOR_ID.equals(descriptor.getPreviousDescriptorId()) )
            {   
		try
                {   this.setCurrentDescriptor(descriptor); }
                catch(PageDescriptorChangingException e)
                {   e.printStackTrace(); }
            }
        }
    }

    /**
     * return the current descriptor
     * 
     * @return an instanceof WizardPageDescriptor
     */
    public WizardPageDescriptor getCurrentDescriptor()
    {   return this.current; }
    
    /** methods that returns a WizardPageDescriptor related to the given id
     *  @param id a String id
     *  @return a WizardPageDescriptor or null if not found
     */
    public WizardPageDescriptor getDescriptor(String id)
    {   WizardPageDescriptor desc = null;
        if ( this.map != null )
            desc = (WizardPageDescriptor)this.map.get(id);
        return desc;
    }
    
    /** return the number of WizardPageDescriptor contained by the model
     *  @return an integer
     */
    public int getPageCount()
    {   return (this.map == null ? 0 : this.map.size()); }
    
    /** return an ietrator over all pages registered in the model
     *  @return an Iterator over WizardPageDescriptor
     */
    public Iterator descriptors()
    {   return (this.map == null ? (Iterator)Collections.EMPTY_LIST.iterator() : this.map.values().iterator()); }
    
    /* #########################################################################
     * ################ PropertyChangeListener implementation ##################
     * ######################################################################### */
    
    /**
     * This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source 
     *   	and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
	if (  evt.getSource() instanceof WizardPageDescriptor && this.map != null && this.map.containsValue(evt.getSource()) )
	{
	    DescriptorModificationEvent modifEvt = new DescriptorModificationEvent(this,
										   (WizardPageDescriptor)evt.getSource(),
										   evt);
	    
	    this.fireDescriptorModification(modifEvt);
	}
    }
}
