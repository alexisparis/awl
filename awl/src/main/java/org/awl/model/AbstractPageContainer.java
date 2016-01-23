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

import javax.swing.event.EventListenerList;
import org.awl.event.DescriptorModificationEvent;
import org.awl.event.DescriptorModificationListener;
import org.awl.util.BeanImpl;

/**
 *
 * Abstract implementation of PageContainer
 *
 * @author alexis
 */
public abstract class AbstractPageContainer extends BeanImpl implements PageContainer
{
    /** list of listeners */
    protected EventListenerList     listeners = new EventListenerList();
    
    /** Creates a new instance of AbstractPageContainer */
    public AbstractPageContainer()
    {	}
    
    /* #########################################################################
     * ############ DescriptorModificationListener related methods #############
     * ######################################################################### */
    
    /** add a new DescriptorModificationListener
     *  @param listener a DescriptorModificationListener
     */
    public void addDescriptorModificationListener(DescriptorModificationListener listener)
    {
	if ( listener != null )
	{
	    this.listeners.add(DescriptorModificationListener.class, listener);
	}
    }
    
    /** remove a DescriptorModificationListener
     *  @param listener a DescriptorModificationListener
     */
    public void removeDescriptorModificationListener(DescriptorModificationListener listener)
    {
	if ( this.listeners != null )
	{
	    this.listeners.remove(DescriptorModificationListener.class, listener);
	}
    }
    
    /** fire a DescriptorModificationEvent
     *	@param evt a DescriptorModificationEvent
     */
    protected void fireDescriptorModification(DescriptorModificationEvent evt)
    {
	if ( this.listeners != null )
	{
	    DescriptorModificationListener[] list = (DescriptorModificationListener[])this.listeners.getListeners(DescriptorModificationListener.class);
	    
	    if ( list != null )
	    {
		for(int i = 0; i < list.length; i++)
		{
		    DescriptorModificationListener currentListener = list[i];
		    
		    if ( currentListener != null )
		    {
			currentListener.descriptorChanged(this, evt);
		    }
		}
	    }
	}
    }
    
}
