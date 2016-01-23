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
package org.awl.test.test8;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.NavigationAuthorization;
import org.awl.DefaultWizard;
import org.awl.WizardPageDescriptor;

/**
 *
 * @author alexis
 */
public class WizardAuthorization extends DefaultWizard
{   
    /**
     * Creates a new instance of WizardAuthorization
     */
    public WizardAuthorization(Frame frame)
    {
	super(frame);
	
	WizardPageDescriptor page1 = new NavigationableWizardPage("page 1");
	page1.setNextDescriptorId("2");
	WizardPageDescriptor page2 = new NavigationableWizardPage("page 2");
	page2.setPreviousDescriptorId("1");
	page2.setNextDescriptorId("3");
	WizardPageDescriptor page3 = new NavigationableWizardPage("page 3");
	page3.setPreviousDescriptorId("2");
        
        this.registerWizardPanel("1", page1);
        this.registerWizardPanel("2", page2);
        this.registerWizardPanel("3", page3);
        
        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("Navigation book...");
        this.setSize(new Dimension(430, 300));
    }
    
    /** navigable page */
    private class NavigationableWizardPage extends DefaultWizardPageDescriptor implements ItemListener
    {
	/** JComboBoxs */
	private JComboBox authorizationNextBox     = null;
	private JComboBox authorizationPreviousBox = null;
	private JComboBox authorizationCancelBox   = null;
	private JComboBox authorizationFinishBox   = null;
	
	public NavigationableWizardPage(String desc)
	{
	    super(desc);
	    
	    ComboBoxModel model = null;
	    
	    NavigationAuthorization[] auths = new NavigationAuthorization[3];
	    auths[0] = NavigationAuthorization.ALLOWED;
	    auths[1] = NavigationAuthorization.DEFAULT;
	    auths[2] = NavigationAuthorization.FORBIDDEN;
	    
	    this.authorizationNextBox = new JComboBox();
	    model = new DefaultComboBoxModel(auths);
	    this.authorizationNextBox.setModel(model);
	    this.authorizationNextBox.addItemListener(this);
	    this.authorizationNextBox.setSelectedItem(this.getNextPageAuthorization());
	    
	    this.authorizationPreviousBox = new JComboBox();
	    model = new DefaultComboBoxModel(auths);
	    this.authorizationPreviousBox.setModel(model);
	    this.authorizationPreviousBox.addItemListener(this);
	    this.authorizationPreviousBox.setSelectedItem(this.getPreviousPageAuthorization());
	    
	    this.authorizationCancelBox = new JComboBox();
	    model = new DefaultComboBoxModel(auths);
	    this.authorizationCancelBox.setModel(model);
	    this.authorizationCancelBox.addItemListener(this);
	    this.authorizationCancelBox.setSelectedItem(this.getCancelAuthorization());
	    
	    this.authorizationFinishBox = new JComboBox();
	    model = new DefaultComboBoxModel(auths);
	    this.authorizationFinishBox.setModel(model);
	    this.authorizationFinishBox.addItemListener(this);
	    this.authorizationFinishBox.setSelectedItem(this.getFinishAuthorization());
	    
	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    int y = 1;
	    
	    gbc.gridx = 1;
	    gbc.gridy = y;
	    panel.add(new JLabel("previous"), gbc);
	    gbc.gridx = 3;
	    gbc.gridy = y;
	    panel.add(this.authorizationPreviousBox, gbc);
	    y += 2;
	    
	    gbc.gridx = 1;
	    gbc.gridy = y;
	    panel.add(new JLabel("next"), gbc);
	    gbc.gridx = 3;
	    gbc.gridy = y;
	    panel.add(this.authorizationNextBox, gbc);
	    y += 2;
	    
	    gbc.gridx = 1;
	    gbc.gridy = y;
	    panel.add(new JLabel("cancel"), gbc);
	    gbc.gridx = 3;
	    gbc.gridy = y;
	    panel.add(this.authorizationCancelBox, gbc);
	    y += 2;
	    
	    gbc.gridx = 1;
	    gbc.gridy = y;
	    panel.add(new JLabel("finish"), gbc);
	    gbc.gridx = 3;
	    gbc.gridy = y;
	    panel.add(this.authorizationFinishBox, gbc);
	    y += 2;
	    
	    this.setComponent(panel);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
	    if ( e.getSource() == this.authorizationNextBox )
	    {
		this.setNextPageAuthorization( (NavigationAuthorization)this.authorizationNextBox.getSelectedItem() );
	    }
	    else if ( e.getSource() == this.authorizationPreviousBox )
	    {
		this.setPreviousPageAuthorization( (NavigationAuthorization)this.authorizationPreviousBox.getSelectedItem() );
	    }
	    else if ( e.getSource() == this.authorizationCancelBox )
	    {
		this.setCancelAuthorization( (NavigationAuthorization)this.authorizationCancelBox.getSelectedItem() );
	    }
	    else if ( e.getSource() == this.authorizationFinishBox )
	    {
		this.setFinishAuthorization( (NavigationAuthorization)this.authorizationFinishBox.getSelectedItem() );
	    }
	}
    }
}
