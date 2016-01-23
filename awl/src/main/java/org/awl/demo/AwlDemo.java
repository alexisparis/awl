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
package org.awl.demo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.awl.DefaultWizard;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.NavigationAuthorization;
import org.awl.Wizard;
import org.awl.WizardPageDescriptor;
import org.awl.header.AbstractWizardHeader;
import org.awl.header.DefaultWizardHeader;
import org.awl.header.EclipseWizardHeader;
import org.awl.message.Message;
import org.awl.message.MessageLevel;

/**
 *
 * Demonstration of Awl
 *
 * @author alexis
 */
public class AwlDemo extends DefaultWizard
{
    /** header type none */
    private static final String HEADER_NONE    = "none";
 
    /** header type classic */
    private static final String HEADER_CLASSIC = "classic";
 
    /** header type eclipse */
    private static final String HEADER_ECLIPSE = "eclipse";
    
    /** first page */
    private WizardPageDescriptor firstPage  = null;
    
    /** second page */
    private WizardPageDescriptor secondPage = null;
    
    /** third page */
    private WizardPageDescriptor thirdPage  = null;
    
    /** forth page */
    private WizardPageDescriptor fourthPage = null;
    
    /** Creates a new instance of AwlDemo */
    public AwlDemo(JFrame frame)
    {	
	super(frame);

	AbstractWizardHeader header = new EclipseWizardHeader();
	header.getIconLabel().setIcon(new ImageIcon(AwlDemo.class.getResource("/org/awl/rc/install.png")));
	this.setHeader(header);

	final ResourceBundle rb = ResourceBundle.getBundle(AwlDemo.class.getName());

	this.setTitle(rb.getString("title"));

	/** first page */
	this.firstPage = new DefaultWizardPageDescriptor();
	firstPage.setTitle(rb.getString("first.title"));
	firstPage.setDescription(rb.getString("first.description"));

	JLabel firstLabel = new JLabel(rb.getString("first.label.text"));
	firstLabel.setVerticalAlignment(SwingConstants.TOP);
	firstLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	firstPage.setComponent(firstLabel);

	firstPage.setNextDescriptorId("2");

	/** second page */
	this.secondPage = new DefaultWizardPageDescriptor();
	secondPage.setTitle(rb.getString("second.title"));
	secondPage.setDescription(rb.getString("second.description"));
	
	secondPage.setMessage(rb.getString("second.messageContent"), MessageLevel.INFO);

	URL licenseUrl = null;
	try
	{
	    licenseUrl = new URL("http://www.gnu.org/licenses/lgpl-2.1.txt");
	}
	catch(MalformedURLException e)
	{
	    e.printStackTrace();
	}

	JEditorPane licensePane = null;
	if ( licenseUrl == null )
	{
	    licensePane = new JEditorPane();
	    licensePane.setText("license LGPL 2.1");
	}
	else
	{
	    try
	    {
		licensePane = new JEditorPane(licenseUrl);
	    }
	    catch(IOException e)
	    {
		licensePane = new JEditorPane();
		licensePane.setText("license LGPL 2.1");
	    }
	}

	licensePane.setPreferredSize(new Dimension(630, 350));

	final JRadioButton radioAcceptLicense = new JRadioButton(rb.getString("second.licenseAccepted"));
	final JRadioButton radioRefuseLicense = new JRadioButton(rb.getString("second.licenseRefused"));

	ButtonGroup licenseButtonGroup = new ButtonGroup();
	licenseButtonGroup.add(radioAcceptLicense);
	licenseButtonGroup.add(radioRefuseLicense);

	ChangeListener changeListener = new ChangeListener()
	{
	    public void stateChanged(ChangeEvent e)
	    {
		secondPage.setNextPageAuthorization(
			radioAcceptLicense.isSelected() ? NavigationAuthorization.DEFAULT : 
							  NavigationAuthorization.FORBIDDEN);
	    }
	};

	radioAcceptLicense.addChangeListener(changeListener);
	radioRefuseLicense.addChangeListener(changeListener);

	radioRefuseLicense.setSelected(true);

	JPanel secondPageComponent = new JPanel();
	GridBagLayout secondPageLayout = new GridBagLayout();
	secondPageComponent.setLayout(secondPageLayout);

	GridBagConstraints gbc = new GridBagConstraints();

	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.fill = GridBagConstraints.BOTH;
	gbc.weightx = 1.0f;
	gbc.weighty = 1.0f;
	JScrollPane scrollLicense = new JScrollPane(licensePane);
	scrollLicense.setBorder(BorderFactory.createLoweredBevelBorder());
	secondPageComponent.add(scrollLicense, gbc);

	gbc.gridx = 1;
	gbc.gridy = 2;
	gbc.fill = GridBagConstraints.NONE;
	gbc.weightx = 0.0f;
	gbc.weighty = 0.0f;
	secondPageComponent.add(new JToolBar.Separator(new Dimension(10, 10)), gbc);

	gbc.gridx = 1;
	gbc.gridy = 3;
	gbc.anchor = GridBagConstraints.WEST;
	secondPageComponent.add(radioAcceptLicense, gbc);

	gbc.gridx = 1;
	gbc.gridy = 4;
	secondPageComponent.add(radioRefuseLicense, gbc);

	secondPageComponent.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	secondPage.setComponent(secondPageComponent);

	secondPage.setPreviousDescriptorId("1");
	secondPage.setNextDescriptorId("3");

	/** third page */
	final JTextField locationField = new JTextField(20);
	final Action chooseLocation = new AbstractAction()
	{
	    public void actionPerformed(ActionEvent e)
	    {
		File current = null;
		String text = locationField.getText();
		if ( text != null && text.trim().length() > 0 )
		{
		    try
		    {
			current = new File(text);
			if ( ! current.exists() )
			{
			    current = null;
			}
		    }
		    catch(Exception ex)
		    {	}
		}

		JFileChooser chooser = new JFileChooser(current);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter()
		{
		    public boolean accept(File f)
		    {
			boolean result = false;

			if ( f != null && f.isDirectory() )
			{
			    result = true;
			}

			return result;
		    }

		    public String getDescription()
		    {
			return rb.getString("third.fileChooser.description");
		    }
		});
		chooser.setMultiSelectionEnabled(false);
		int answer = chooser.showDialog(AwlDemo.this, rb.getString("third.fileChooser.selectLabel"));

		if ( answer == JFileChooser.CANCEL_OPTION )
		{
		    locationField.setText("");
		    thirdPage.setMessage(rb.getString("third.messageContent"), MessageLevel.WARN);
		}
		else // aprove
		{
		    locationField.setText(chooser.getSelectedFile().getPath());
		}
	    }
	};
	this.thirdPage = new DefaultWizardPageDescriptor()
	{
	    public void displayingPanel(Wizard wizard)
	    {
		/** open file dialog box */
		if ( locationField.getText().trim().length() == 0 )
		{
		    chooseLocation.actionPerformed(null);
		}
	    }
	};
	chooseLocation.putValue(Action.SMALL_ICON, new ImageIcon(AwlDemo.class.getResource("/org/awl/rc/folder.png")));
	thirdPage.setNextPageAuthorization(NavigationAuthorization.FORBIDDEN);

	locationField.getDocument().addDocumentListener(new DocumentListener()
	{
	    public void changedUpdate(DocumentEvent e)
	    {	}

	    public void insertUpdate(DocumentEvent e)
	    {
		this.changeNavigation();
	    }

	    public void removeUpdate(DocumentEvent e)
	    {
		this.changeNavigation();
	    }

	    private void changeNavigation()
	    {
		thirdPage.setNextPageAuthorization( locationField.getText().trim().length() > 0 ? NavigationAuthorization.DEFAULT :
												  NavigationAuthorization.FORBIDDEN );
	    }
	});

	thirdPage.setTitle(rb.getString("third.title"));
	thirdPage.setDescription(rb.getString("third.description"));

	JPanel thirdSubPanel = new JPanel();

	JPanel thirdPanel = new JPanel();
	JLabel installLocationLabel = new JLabel(rb.getString("third.label.text"));
	installLocationLabel.setHorizontalAlignment(SwingConstants.LEFT);
	thirdSubPanel.add(installLocationLabel);
	locationField.setEnabled(false);
	locationField.setHorizontalAlignment(SwingConstants.LEFT);
	thirdSubPanel.add(locationField);
	JButton locationButton = new JButton(chooseLocation);
	locationButton.setHorizontalAlignment(SwingConstants.LEFT);
	Dimension buttonPrefSize = new Dimension(locationButton.getPreferredSize());
	buttonPrefSize.height = locationField.getPreferredSize().height;
//		buttonPrefSize.width = ((Icon)chooseLocation.getValue(Action.SMALL_ICON)).getIconWidth() + 4;
	locationButton.setPreferredSize(buttonPrefSize);
	thirdSubPanel.add(locationButton);

	thirdSubPanel.setAlignmentX(0.0f);
	thirdPanel.setAlignmentX(0.0f);

	thirdPanel.setLayout(new GridBagLayout());

	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.0f;
	gbc.weighty = 0.0f;
	gbc.fill    = GridBagConstraints.NONE;
	thirdPanel.add(thirdSubPanel, gbc);

	gbc.gridx = 2;
	gbc.gridy = 2;
	gbc.weightx = 1.0f;
	gbc.weighty = 1.0f;
	gbc.fill    = GridBagConstraints.BOTH;
	thirdPanel.add(new JToolBar.Separator(new Dimension(5, 5)), gbc);

	thirdPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	thirdPage.setComponent(thirdPanel);

	thirdPage.setPreviousDescriptorId("2");
	thirdPage.setNextDescriptorId("4");

	/** fourth page */
	final JProgressBar installProgressBar = new JProgressBar();
	final JLabel       progressLabel      = new JLabel(rb.getString("fourth.inProgress.text"));

	this.fourthPage = new DefaultWizardPageDescriptor()
	{
	    public void displayingPanel(Wizard wizard)
	    {
		/* begin installation */
		this.setPreviousPageAuthorization(NavigationAuthorization.FORBIDDEN);
		this.setFinishAuthorization(NavigationAuthorization.FORBIDDEN);

		/* begin new thread */
		Runnable runnable = new Runnable()
		{
		    public void run()
		    {
			while(installProgressBar.getValue() < installProgressBar.getMaximum())
			{
			    Runnable r = new Runnable()
			    {
				public void run()
				{   installProgressBar.setValue( installProgressBar.getValue() + 1); }
			    };
			    SwingUtilities.invokeLater(r);

			    try
			    {
				Thread.sleep(50);
			    }
			    catch(InterruptedException e)
			    {
				Runnable r1 = new Runnable()
				{
				    public void run()
				    {	installProgressBar.setValue(installProgressBar.getMaximum()); }
				};
				SwingUtilities.invokeLater(r1);

				break;
			    }
			}

			Runnable r2 = new Runnable()
			{
			    public void run()
			    {	progressLabel.setText(rb.getString("fourth.finished.text")); }
			};
			SwingUtilities.invokeLater(r2);

			setFinishAuthorization(NavigationAuthorization.DEFAULT);
			setCancelAuthorization(NavigationAuthorization.FORBIDDEN);
		    }
		};

		new Thread(runnable).start();
	    }
	};
	fourthPage.setTitle(rb.getString("fourth.title"));
	fourthPage.setDescription(rb.getString("fourth.description"));

	JPanel fourthPanel = new JPanel();
	BoxLayout fourthLayout = new BoxLayout(fourthPanel, BoxLayout.PAGE_AXIS);
	fourthPanel.setLayout(fourthLayout);

	fourthPanel.add(installProgressBar);
	fourthPanel.add(progressLabel);

	fourthPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	fourthPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	fourthPage.setComponent(fourthPanel);

	fourthPage.setPreviousDescriptorId("3");

	/** register */
	this.addPage(firstPage, "1");
	this.addPage(secondPage, "2");
	this.addPage(thirdPage, "3");
	this.addPage(fourthPage, "4");
    }
    
    public static void main(String[] args)
    {
	Runnable runnable = new Runnable()
	{
	    public void run()
	    {
		Wizard wizard = new AwlDemo((JFrame)null);
		
		wizard.pack();
		wizard.setVisibleOnCenterOfScreen();
	    }
	};
	try
	{
	    SwingUtilities.invokeLater(runnable);
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }
    
}
