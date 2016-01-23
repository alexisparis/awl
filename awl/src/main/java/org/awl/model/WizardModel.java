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

import java.util.Iterator;
import java.util.List;
import org.awl.*;
import org.awl.event.WizardModelListener;
import org.awl.exception.PageDescriptorChangingException;
import org.awl.exception.PagesCycleDetectedException;
import org.awl.exception.UnregisteredDescriptorException;
import org.awl.util.Bean;

/**
 *
 * Model attached to a Wizard.
 *
 * It is used to keep meta data about buttons and about registered WizardPageDescriptor
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public interface WizardModel extends Bean, PageContainer
{   
    /** register a new WizardPanelDescriptor    
     *  @param id the id of the WizardPanelDescriptor
     *  @param descriptor an instanceof WizardPanelDescriptor
     */
    public void registerWizardPanel(String id, WizardPageDescriptor descriptor);
    
    /** return the descriptor that represent at a given time the root descriptor according to
     *  the current descriptor
     *  @return the root WizardPageDescriptor or null if failed
     */
    public WizardPageDescriptor getRootDescriptor();
    
    /** return a List of descriptor which describe the path of descriptor which contains current descriptor.
     *  descriptors before and after current descriptor are respectively keeped according to next/prev relationship
     *
     *  @return a list of WizardPageDescriptor
     *
     *  @exception PagesCycleDetectedException if a cycle is detected
     */
    public List getCurrentPathDescriptor() throws PagesCycleDetectedException;
    
    /** set the current descriptor by giving its id
     *  @param id the id of the descriptor to set as current descriptor
     *
     *  @exception UnregisteredDescriptorException when trying to access to an unregistered descriptor
     *  @exception PageDescriptorChangingException if descriptor could not be set as current descriptor
     */
    public void setCurrentDescriptor(String id) throws UnregisteredDescriptorException, PageDescriptorChangingException;
    
    /** return the current descriptor
     *  @return an instanceof WizardPageDescriptor
     */
    public WizardPageDescriptor getCurrentDescriptor();
    
    /** return the first descriptor of the wizard
     *  @return an instance of WizardPageDescriptor
     *		or null if there is actually no page with previous id
     *		equals to WizardConstants.STARTING_DESCRIPTOR_ID
     */
    public WizardPageDescriptor getFirstDescriptor();
    
    /** return the last descriptor of the wizard<br/>
     *	if there are severall descriptors that convey as last descriptor,
     *	the returned descriptor should be one of those descriptors without any other constraint
     *  @return an instance of WizardPageDescriptor
     *		or null if there is actually no page with previous id
     *		equals to WizardConstants.TERMINAL_DESCRIPTOR_ID
     */
    public WizardPageDescriptor getLastDescriptor();
    
    /** methods that returns a WizardPageDescriptor related to the given id
     *  @param id a String id
     *  @return a WizardPageDescriptor or null if not found
     */
    public WizardPageDescriptor getDescriptor(String id);
    
    /** add a new WizardModelListener
     *  @param listener a WizardModelListener
     */
    public void addWizardModelListener(WizardModelListener listener);
    
    /** remove a WizardModelListener
     *  @param listener a WizardModelListener
     */
    public void removeWizardModelListener(WizardModelListener listener);
    
    /** method called to reinitialize the wizard
     *  this method is called when the wizard has finished (coz user hit finish, cancel or close button )
     *  the model should select the initial root descriptor
     */
    public void reinit();
    
}
