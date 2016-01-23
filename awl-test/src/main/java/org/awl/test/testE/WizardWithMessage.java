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
package org.awl.test.testE;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.awl.DefaultWizard;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.Wizard;
import org.awl.WizardConstants;
import org.awl.header.DefaultWizardHeader;
import org.awl.message.MessageLevel;

/**
 *
 * test addPage wizard methods
 *
 * @author alexis
 */
public class WizardWithMessage extends DefaultWizard
{
    
    /** Creates a new instance of BookWizardTest */
    public WizardWithMessage(Frame frame)
    {   super(frame);
	
//	this.setModal(false);
	
//	javax.swing.RepaintManager.validateInvalidComponents(RepaintManager.java:639)
//        javax.swing.SystemEventQueueUtilities$ComponentWorkRequest.run(SystemEventQueueUtilities.java:127)
//        java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:209)
//        java.awt.EventQueue.dispatchEvent(EventQueue.java:597)

        
        MessageProviderPageDescriptor desc1 = new MessageProviderPageDescriptor("provider 1", "this is provider 1");
        MessageProviderPageDescriptor desc2 = new MessageProviderPageDescriptor("provider 2", "this is provider 2");
        MessageProviderPageDescriptor desc3 = new MessageProviderPageDescriptor("provider 3", "this is provider 3");
        MessageProviderPageDescriptor desc4 = new MessageProviderPageDescriptor("provider 4", "this is provider 4");
	
	this.addPage(desc1, "1", WizardConstants.STARTING_DESCRIPTOR_ID,                                    "2");
	this.addPage(desc2, "2",                                    "1",                                    "3");
	this.addPage(desc3, "3",                                    "2",                                    "4");
	this.addPage(desc4, "4",                                    "3", WizardConstants.TERMINAL_DESCRIPTOR_ID);
	
	DefaultWizardHeader header = new DefaultWizardHeader();
	header.getIconLabel().setIcon(new ImageIcon(this.getClass().getResource("/org/awl/rc/people.png")));
	this.setHeader(header);
        
        this.setTitle("Messages...");
    }
    
    public static void main(String[] args)
    {
	Runnable run = new Runnable()
	{
	    public void run()
	    {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	
		JButton button = new JButton("launch wizard");
		frame.getContentPane().add(button);
	
		button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			Wizard wizard = new WizardWithMessage(frame);

			wizard.pack();
			wizard.setVisibleOnCenterOfScreen();
		    }
		});
		
		frame.setSize(200, 200);
		frame.setVisible(true);
	    }
	};
	
	Runnable run2 = new Runnable()
	{
	    public void run()
	    {
		Wizard wizard = new WizardWithMessage((Frame)null);

		wizard.pack();
		wizard.setVisibleOnCenterOfScreen();
	    }
	};
	
	try
	{
	    SwingUtilities.invokeAndWait(run2);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }
    
    private class MessageProviderPageDescriptor extends DefaultWizardPageDescriptor
    {
	public MessageProviderPageDescriptor(String title, String desc)
	{
	    super(title, desc);
	    
	    MessageLevel[] levels = MessageLevel.LEVELS;
	    
	    JPanel panel = new JPanel();
	    GridBagLayout layout = new GridBagLayout();
	    
	    panel.setLayout(layout);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    for(int i = 0; i < levels.length; i++)
	    {
		final MessageLevel currentLevel = levels[i];
		
		JLabel label = new JLabel("level " + (i + 1));
		gbc.gridx = 1;
		gbc.gridy = i + 1;
		panel.add(label, gbc);
		
		final JTextField field = new JTextField(25);
		gbc.gridx = 2;
		gbc.gridy = i + 1;
		panel.add(field, gbc);
		
		field.setText("<html>aaaa<br>bb<br>c</html>");
		
		final JButton button = new JButton("send");
		Dimension dim = new Dimension(button.getPreferredSize().width, field.getPreferredSize().height);
		button.setSize(dim);
		gbc.gridx = 3;
		gbc.gridy = i + 1;
		panel.add(button, gbc);
		
		button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			if ( e.getSource() == button )
			{
			    String text = field.getText();
			    if ( text.trim().length() == 0 )
			    {
				text = null;
			    }

			    setMessage(text, currentLevel);
			}
		    }
		});
		
		final JButton buttonClear = new JButton("X");
		Dimension dim2 = new Dimension(buttonClear.getPreferredSize().width, field.getPreferredSize().height);
		System.out.println("dim2 : " + dim2);
		buttonClear.setSize(dim2);
		gbc.gridx = 4;
		gbc.gridy = i + 1;
		panel.add(buttonClear, gbc);
		
		buttonClear.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
			if ( e.getSource() == buttonClear )
			{
			    setMessage(null, currentLevel);
			}
		    }
		});
	    }
	    
	    JButton clearButton = new JButton("clear all");
	    clearButton.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    resetMessages();
		}
	    });
	    
	    gbc.gridx = 1;
	    gbc.gridy = levels.length + 1;
	    gbc.gridwidth = 4;
	    panel.add(clearButton, gbc);
	    
	    this.setComponent(panel);
	}
    }
                        
    
}
