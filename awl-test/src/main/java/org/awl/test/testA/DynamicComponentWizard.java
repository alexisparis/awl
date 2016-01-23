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
package org.awl.test.testA;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.DefaultWizard;
import org.awl.Wizard;
import org.awl.WizardPageDescriptor;

/**
 *
 * @author alexis
 */
public class DynamicComponentWizard extends DefaultWizard
{   
    /**
     * Creates a new instance of DynamicComponentWizard
     */
    public DynamicComponentWizard(Frame frame)
    {
	super(frame);
	
	WizardPageDescriptor page1 = new WizardPage("a");
	page1.setNextDescriptorId("2");
	WizardPageDescriptor page2 = new WizardPage("b");
	page2.setPreviousDescriptorId("1");
	page2.setNextDescriptorId("3");
	WizardPageDescriptor page3 = new WizardPage("c");
	page3.setPreviousDescriptorId("2");
        
        this.registerWizardPanel("1", page1);
        this.registerWizardPanel("2", page2);
        this.registerWizardPanel("3", page3);
        
        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("dynamic book...");
        this.setSize(new Dimension(430, 300));
    }
    
    /** navigable page */
    private class WizardPage extends DefaultWizardPageDescriptor
    {
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	private int             count   = 0;
	
	public WizardPage(String desc)
	{
	    super(desc);
	    
	    this.setComponent(new JLabel("<html>label d'attente<br>" +
		    "change when displayed after a temporization of 1 second" +
		    "</html>"));
	}

	public void aboutToDisplayPanel(Wizard wizard)
	{
	    System.out.println("aboutToDisplayPanel : ");
	    super.aboutToDisplayPanel(wizard);
	    
	    /** change the component */
	    service.submit(new Runnable()
	    {
		public void run()
		{
		    try
		    {
			Thread.sleep(1000);
		    }
		    catch(Exception e)
		    {
			e.printStackTrace();
		    }
		}
	    });
	    
	    
	    /** change the component */
	    service.submit(new Runnable()
	    {
		public void run()
		{
		    setComponent(new JLabel("<html>component change n° " + ++count + "<br>" +
					    "change when displayed after a temporization of 1 second" +
					    "</html>"));
		}
	    });
	}
    }
}
