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
package org.awl.test.test7;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.DefaultWizard;

/**
 *
 * @author alexis
 */
public class WizardPositioned extends DefaultWizard
{   
    /**
	 * Creates a new instance of WizardPositioned
	 */
    public WizardPositioned(Frame frame)
    {
	super(frame);
	
        DefaultWizardPageDescriptor page1 = new DefaultWizardPageDescriptor();
	JButton button = new JButton("center of owner");
	button.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent e)
	    {
		centerOnOwner();
	    }
	});
        page1.setComponent(button);
        page1.setDescription("my first page");

        DefaultWizardPageDescriptor page2 = new DefaultWizardPageDescriptor();
	JButton button2 = new JButton("center of screen");
	button2.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent e)
	    {
		centerOnScreen();
	    }
	});
        page2.setComponent(button2);
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
        
        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("Read a bad book...");
        this.setSize(new Dimension(430, 300));
    }
}
