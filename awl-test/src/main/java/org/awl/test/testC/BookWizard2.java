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
package org.awl.test.testC;

import java.awt.Frame;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;
import org.awl.test.test1.PageWizardDescriptor;

/**
 *
 * test addPage wizard methods
 *
 * @author alexis
 */
public class BookWizard2 extends DefaultWizard
{
    
    /** Creates a new instance of BookWizardTest */
    public BookWizard2(Frame frame)
    {   super(frame);
        
        PageWizardDescriptor desc1 = new PageWizardDescriptor(
                            "<html><h1>page 1</h1></html>",
                                                              "page 1", WizardConstants.STARTING_DESCRIPTOR_ID, WizardConstants.STARTING_DESCRIPTOR_ID);
        PageWizardDescriptor desc2 = new PageWizardDescriptor(
                            "<html><h1>page 2</h1></html>",
                                                              "page 2", WizardConstants.STARTING_DESCRIPTOR_ID, WizardConstants.STARTING_DESCRIPTOR_ID);
        final PageWizardDescriptor desc3 = new PageWizardDescriptor(
                            "<html><h1>page 3</h1></html>",
                                                              "page 3", WizardConstants.STARTING_DESCRIPTOR_ID, WizardConstants.STARTING_DESCRIPTOR_ID);
        PageWizardDescriptor desc4 = new PageWizardDescriptor(
                            "<html><h1>page 4</h1></html>",
                                                              "page 4", WizardConstants.STARTING_DESCRIPTOR_ID, WizardConstants.STARTING_DESCRIPTOR_ID);
	
	this.addPage(desc4, "4",                                    "3", WizardConstants.TERMINAL_DESCRIPTOR_ID);
	this.addPage(desc1, "1", WizardConstants.STARTING_DESCRIPTOR_ID,                                    "2");
	this.addPage(desc2, "2",                                    "1",                                    "3");
	this.addPage(desc3, "3",                                    "2",                                    "4");
        
        this.setTitle("Read a bad book...");
//        this.getContentPane().setMinimumSize(new Dimension(300, 200));
//        this.getContentPane().setPreferredSize(new Dimension(500, 300));
        this.pack();
    }
                        
    
}
