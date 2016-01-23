/*
 * WizardLauncher.java
 *
 * Created on 7 janvier 2008, 22:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.awl.test;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.awl.DefaultWizard;

/**
 *
 * @author alexis
 */
public class WizardLauncher implements ActionListener
{
    /** type of wizard to launch */
    private Class                 wizardClass = null;

    /** frame used to display the wizard */
    private Frame                 frame       = null;

    /** button */
    private JButton               button      = null;

    /** soft reference to the wizard */
    private SoftReference     wizardRef   = new SoftReference(null);

    /** description of the test case */
    private String                description = null;

    public WizardLauncher(Class wizardClass, Frame frame, String desc)
    {   
	if ( wizardClass == null )
	    throw new IllegalArgumentException("wizardClass is null");

	this.description = desc;

	Icon icon = new ImageIcon(this.getClass().getResource("/org/awl/rc/right.png"));
	this.button = new JButton(icon);

	this.wizardClass = wizardClass;

	this.frame = frame;

	this.button.addActionListener(this);
    }

    /** return the button that allow to launch the wizard
     *  @return a JButton
     */
    public JButton getButton()
    {   return this.button; }

    public void actionPerformed(ActionEvent ae)
    {
	if ( ae.getSource() == this.button )
	{   
	    DefaultWizard wiz = (DefaultWizard)wizardRef.get();

	    if ( this.description != null )
	    {
		JOptionPane.showMessageDialog(frame, this.description, "Test case description", JOptionPane.INFORMATION_MESSAGE);
	    }

	    if ( wiz == null )
	    {
		try
		{
		    /** create an instance */
		    Constructor cs = wizardClass.getConstructor(new Class[]{Frame.class});

		    wiz = (DefaultWizard)cs.newInstance(new Object[]{frame});
		}
		catch(Exception e)
		{   e.printStackTrace(); }

		wizardRef = new SoftReference(wiz);
	    }

	    final DefaultWizard wizCopy = wiz;

	    if ( wizCopy == null )
	    {   throw new RuntimeException("unable to " +
				    "instantiate class '" + wizardClass + "'"); }
	    else
	    {   
		Runnable runnable = new Runnable()
		{
		    public void run()
		    {   
			wizCopy.setVisibleOnCenterOfOwner();
		    }
		};

//		    SwingUtilities.invokeLater(runnable);
		runnable.run();
	    }
	}
    }
}