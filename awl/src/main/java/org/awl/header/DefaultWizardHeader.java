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
package org.awl.header;

import javax.swing.SwingUtilities;
import org.awl.WizardPageDescriptor;

/**
 *
 * Default implementation of WizardHeaderPanel
 *
 * @author alexis
 */
public class DefaultWizardHeader extends AbstractWizardHeader
{
    /**
     * Creates a new instance of DefaultWizardHeader
     */
    public DefaultWizardHeader()
    {	}
    
    /** update the content of the panel according to the given descriptor
     *	@param descriptor a WizardPageDescriptor
     */
    public void displayDescriptorInformation(final WizardPageDescriptor descriptor)
    {
	Runnable runnable = new Runnable()
	{
	    public void run()
	    {
		if ( descriptor == null )
		{
		    if ( getTitleLabel() != null )
		    {
			getTitleLabel().setText("");
		    }
		    if ( getDescriptionLabel() != null )
		    {
			getDescriptionLabel().setText("");
		    }
		}
		else
		{
		    if ( getTitleLabel() != null )
		    {
			getTitleLabel().setText(descriptor.getTitle());
		    }
		    if ( getDescriptionLabel() != null )
		    {
			getDescriptionLabel().setText(descriptor.getDescription());
		    }
		}
	    }
	};
	
	if ( SwingUtilities.isEventDispatchThread() )
	{
	    runnable.run();
	}
	else
	{
	    SwingUtilities.invokeLater(runnable);
	}
    }
    
}
