/* =============================================================================
 * AWL : Another DefaultWizard Library
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
package org.awl.test.test1;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.DefaultWizard;

/**
 *
 * @author alexis
 */
public class PageWizardDescriptor extends DefaultWizardPageDescriptor
{   
    /** Creates a new instance of FirstPageWizardDescriptor
     *  @param content the content to apply to the label
     *  @param description the description of the descriptor
     *  @param previousId the id the previous descriptor
     *  @param nextId the id the next descriptor
     */
    public PageWizardDescriptor(String content, String description, String previousId, String nextId)
    {   super(null, description, previousId, nextId);
        
        JLabel label = new JLabel(content);
        
        label.setVerticalAlignment(SwingConstants.TOP);
        
        this.setComponent(label);
    }
    
    private String getLabelText()
    {   return ((JLabel)this.getComponent()).getText(); }
}
