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
 * Implementation of WizardPageDescriptor.
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public class DefaultWizardPageDescriptor extends AbstractWizardPageDescriptor
{   
    /** Creates a new instance of AbstractWizardPageDescriptor
     *	@param title the title of the descriptor
     *  @param description the description of the descriptor
     *  @param previousId the id the previous descriptor
     *  @param nextId the id the next descriptor
     */
    public DefaultWizardPageDescriptor(String title, String description, String previousId, String nextId)
    {   super();
        
	// TODO : what to do if nextId and previousId are equals ???
	
	this.setTitle(title);
        this.setDescription(description);
        this.setNextDescriptorId(nextId);
        this.setPreviousDescriptorId(previousId);
    }
    
    /** Creates a new instance of AbstractWizardPageDescriptor
     *	@param title the title of the descriptor
     *  @param description the description of the descriptor
     */
    public DefaultWizardPageDescriptor(String title, String description)
    {   this(title, description, WizardConstants.STARTING_DESCRIPTOR_ID, WizardConstants.TERMINAL_DESCRIPTOR_ID); }
    
    /** Creates a new instance of AbstractWizardPageDescriptor
     *	@param title the title of the descriptor
     */
    public DefaultWizardPageDescriptor(String title)
    {   this(title, null); }
    
    /** Creates a new instance of AbstractWizardPageDescriptor */
    public DefaultWizardPageDescriptor()
    {   this(null); }
    
}
