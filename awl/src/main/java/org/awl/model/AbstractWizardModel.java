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
import org.awl.WizardPageDescriptor;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.event.DescriptorModificationListener;
import org.awl.event.WizardModelEvent;
import org.awl.event.WizardModelListener;
import org.awl.exception.PageDescriptorChangingException;
import org.awl.util.BeanImpl;

/**
 *
 * Abstract implementation of WizardModel that care about PropertyChange and ButtonCaracteristics's
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public abstract class AbstractWizardModel extends AbstractPageContainer implements WizardModel
{   
    /** Creates a new instance of AbstractWizardModel */
    public AbstractWizardModel()
    {   }
    
    /* #########################################################################
     * ################# WizardModelListener related methods ###################
     * ######################################################################### */
    
    /** add a new WizardModelListener
     *  @param listener a WizardModelListener
     */
    public void addWizardModelListener(WizardModelListener listener)
    {   this.listeners.add(WizardModelListener.class, listener); }
    
    /** remove a WizardModelListener
     *  @param listener a WizardModelListener
     */
    public void removeWizardModelListener(WizardModelListener listener)
    {   this.listeners.remove(WizardModelListener.class, listener); }
    
    /** fire a description add
     *  @param event a WizardModelEvent
     */
    protected void fireDescriptorAdded(WizardModelEvent event)
    {   if ( this.listeners != null )
        {   WizardModelListener[] ar = (WizardModelListener[])this.listeners.getListeners(WizardModelListener.class);
            if ( ar != null )
            {   for(int i = 0; i < ar.length; i++)
                {   WizardModelListener current = ar[i];
                    if ( current != null )
                    {   current.descriptorAdded(event); }
                }
            }
        }
    }
    
    /** fire a description remove
     *  @param event a WizardModelEvent
     */
    protected void fireDescriptorRemoved(WizardModelEvent event)
    {   if ( this.listeners != null )
        {   WizardModelListener[] ar = (WizardModelListener[])this.listeners.getListeners(WizardModelListener.class);
            if ( ar != null )
            {   for(int i = 0; i < ar.length; i++)
                {   WizardModelListener current = ar[i];
                    if ( current != null )
                    {   current.descriptorRemoved(event); }
                }
            }
        }
    }
    
    /** current descriptor changed
     *  @param event a CurrentDescriptorChangedEvent
     */
    protected void fireCurrentDescriptorChanged(CurrentDescriptorChangedEvent event)
    {   if ( this.listeners != null )
        {   WizardModelListener[] ar = (WizardModelListener[])this.listeners.getListeners(WizardModelListener.class);
            if ( ar != null )
            {   for(int i = 0; i < ar.length; i++)
                {   WizardModelListener current = ar[i];
                    if ( current != null )
                    {   current.currentDescriptorChanged(event); }
                }
            }
        }
    }
    
    /** current descriptor is about to change
     *  @param currentDescriptor the current descriptor
     *  @param candidate the descriptor we try to set as current descriptor
     *
     *  @exception PageDescriptorChangingException if the listener refused the change
     */
    protected void fireCheckCurrentDescriptorChanging(WizardPageDescriptor currentDescriptor, WizardPageDescriptor candidate)
                throws PageDescriptorChangingException
    {   if ( this.listeners != null )
        {   WizardModelListener[] ar = (WizardModelListener[])this.listeners.getListeners(WizardModelListener.class);
            if ( ar != null )
            {   for(int i = 0; i < ar.length; i++)
                {   WizardModelListener current = ar[i];
                    if ( current != null )
                    {   current.checkCurrentDescriptorChanging(currentDescriptor, candidate); }
                }
            }
        }
    }
    
}
