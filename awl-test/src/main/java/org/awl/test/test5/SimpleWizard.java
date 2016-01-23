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
package org.awl.test.test5;

import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JLabel;
import org.awl.DefaultWizardController;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;

/**
 *
 * @author alexis
 */
public class SimpleWizard extends DefaultWizard
{   
    /** Creates a new instance of SimpleWizard */
    public SimpleWizard(Frame frame)
    {
	super(frame);
	
        DefaultWizardPageDescriptor page1 = new DefaultWizardPageDescriptor();
        page1.setComponent(new JLabel("this is my first page"));
        page1.setDescription("my first page");

        DefaultWizardPageDescriptor page2 = new DefaultWizardPageDescriptor();
        page2.setComponent(new JLabel("this is my second page"));
        page2.setDescription("my second page");
	
	this.getController().setManageI18n(false);
	this.getBackButton().setText("previous page");
	this.getNextButton().setText("next page");
	this.getFinishButton().setText("End");
	this.getCancelButton().setText("Hate books");
        
        page1.setNextDescriptorId("2");
        page2.setPreviousDescriptorId("1");
        
        this.registerWizardPanel("1", page1);
        this.registerWizardPanel("2", page2);
        
//        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("Read a bad book...");
        this.setSize(new Dimension(430, 300));
    }
}
