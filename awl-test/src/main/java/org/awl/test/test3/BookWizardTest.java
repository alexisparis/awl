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
package org.awl.test.test3;

import java.awt.Dimension;
import javax.swing.JFrame;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.event.WizardModelAdapter;
import org.awl.test.test1.PageWizardDescriptor;

/**
 *
 * @author alexis
 */
public class BookWizardTest
{
    
    /** Creates a new instance of BookWizardTest */
    public BookWizardTest()
    {
    }
    
    public static void main(String[] args)
    {
        PageWizardDescriptor desc1 = new PageWizardDescriptor(
                            "<html><h1>page 1</h1><p>This is the first page of the wizard</p><p>you can put everything you want</p></html>",
                                                              "page 1 dsfsdfsdf", WizardConstants.STARTING_DESCRIPTOR_ID, "2");
        PageWizardDescriptor desc2 = new PageWizardDescriptor(
                            "<html><h1>page 2</h1><p>This is the second page</p><p>what you want?</p></html>",
                                                              "page 2 sdfsdfsdf", "1", "3");
        final PageWizardDescriptor desc3 = new PageWizardDescriptor(
                            "<html><h1>page 3</h1><p>This is the third page</p><p>Here you cannot continue???</p></html>",
                                                              "page 3 sdfsdfsdf", "2", "4");
        PageWizardDescriptor desc4 = new PageWizardDescriptor(
                            "<html><h1>page 4</h1><p>This is the last page</p><p>Did you enjoyed??</p></html>",
                                                              "page 4 sfsdfsdsdf", "3", WizardConstants.TERMINAL_DESCRIPTOR_ID);
        
        DefaultWizard wizard = new DefaultWizard((JFrame)null);
        
        wizard.registerWizardPanel("1", desc1);
        wizard.registerWizardPanel("2", desc2);
        wizard.registerWizardPanel("3", desc3);
        wizard.registerWizardPanel("4", desc4);
        
        wizard.setController(new UglyWizardController());
        
        wizard.getModel().addWizardModelListener(new WizardModelAdapter()
        {   
            /** a new descriptor has been set as current descriptor
             *  @param event a CurrentDescriptorChangedEvent
             */
            public void currentDescriptorChanged(CurrentDescriptorChangedEvent event)
            {   if ( event.getNewDesc() == desc3 )
                {   event.getNewDesc().setNextDescriptorId(WizardConstants.TERMINAL_DESCRIPTOR_ID); }
            }
        });
        
        wizard.setDefaultCloseOperation(wizard.DISPOSE_ON_CLOSE);
        wizard.setTitle("Read a bad book...");
//        wizard.setMinimumSize(new Dimension(300, 200));
//        wizard.setPreferredSize(new Dimension(500, 300));
        wizard.pack();
        wizard.setVisible(true);
        
        
    }
                        
    
}
