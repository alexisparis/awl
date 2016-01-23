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
package org.awl.test.test4;

import java.awt.Dimension;
import java.awt.Frame;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;
import org.awl.test.test1.PageWizardDescriptor;

/**
 *
 * @author alexis
 */
public class SinglePageWizard extends DefaultWizard
{
    /** Creates a new instance of BookWizardTest */
    public SinglePageWizard(Frame frame)
    {   super(frame);
        
        PageWizardDescriptor desc1 = new PageWizardDescriptor(
                            "<html><h1>Single page</h1></html>",
                                                              "the only page", WizardConstants.STARTING_DESCRIPTOR_ID, WizardConstants.TERMINAL_DESCRIPTOR_ID);
        
        this.registerWizardPanel("1", desc1);
        
        this.setTitle("Read a book with only one page ...");
//        this.setMinimumSize(new Dimension(300, 200));
//        this.setPreferredSize(new Dimension(500, 300));
        this.pack();
    }
                        
    
}
