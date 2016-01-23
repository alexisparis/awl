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
package org.awl.test.test9;

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
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.NavigationAuthorization;
import org.awl.DefaultWizard;
import org.awl.WizardPageDescriptor;
import org.awl.header.DefaultWizardHeader;

/**
 *
 * @author alexis
 */
public class PageDescChangeWizard extends DefaultWizard
{   
    /**
     * Creates a new instance of PageDescChangeWizard
     */
    public PageDescChangeWizard(Frame frame)
    {
	super(frame);
	
	this.setHeaderPanel(new DefaultWizardHeader());
	
	WizardPageDescriptor page1 = new WizardPage("a", "desc a");
	page1.setNextDescriptorId("2");
	WizardPageDescriptor page2 = new WizardPage("b", "desc b");
	page2.setPreviousDescriptorId("1");
	page2.setNextDescriptorId("3");
	WizardPageDescriptor page3 = new WizardPage("c", "desc c");
	page3.setPreviousDescriptorId("2");
        
        this.registerWizardPanel("1", page1);
        this.registerWizardPanel("2", page2);
        this.registerWizardPanel("3", page3);
        
        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("Mutable page book...");
        this.setSize(new Dimension(430, 300));
    }
    
    /** navigable page */
    private class WizardPage extends DefaultWizardPageDescriptor implements ItemListener
    {
	/** JComboBoxs */
	private JComboBox descriptionBox = null;
	private JComboBox titleBox       = null;
	
	public WizardPage(String title, String desc)
	{
	    super(title, desc);
	    
	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    /** title */
	    
	    JLabel labelTitle = new JLabel("title");
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(labelTitle, gbc);
	    
	    gbc.gridx = 2;
	    gbc.gridy = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(new JToolBar.Separator(new Dimension(10, 5)), gbc);
	    
	    this.titleBox = new JComboBox();
	    ComboBoxModel titleModel = new DefaultComboBoxModel(new String[]{"a", "b", "c", "d"});
	    this.titleBox.setModel(titleModel);
	    this.titleBox.setSelectedItem(desc);
	    this.titleBox.addItemListener(this);
	    gbc.gridx = 3;
	    gbc.gridy = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(this.titleBox, gbc);
	    
	    /** description */
	    
	    JLabel labelDescription = new JLabel("description");
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(labelDescription, gbc);
	    
	    gbc.gridx = 2;
	    gbc.gridy = 3;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(new JToolBar.Separator(new Dimension(10, 5)), gbc);
	    
	    this.descriptionBox = new JComboBox();
	    ComboBoxModel descriptionModel = new DefaultComboBoxModel(new String[]{"desc a", "desc b", "desc c", "desc d"});
	    this.descriptionBox.setModel(descriptionModel);
	    this.descriptionBox.setSelectedItem(title);
	    this.descriptionBox.addItemListener(this);
	    gbc.gridx = 3;
	    gbc.gridy = 3;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(this.descriptionBox, gbc);
	    
	    gbc.gridx = 4;
	    gbc.gridy = 4;
	    gbc.weightx = 1.0f;
	    gbc.weighty = 1.0f;
	    gbc.fill = GridBagConstraints.BOTH;
	    panel.add(new JToolBar.Separator(new Dimension(5, 5)), gbc);
	    
	    this.setComponent(panel);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
	    if ( e.getSource() == this.titleBox )
	    {
		this.setTitle( (String)this.titleBox.getSelectedItem() );
	    }
	    else if ( e.getSource() == this.descriptionBox )
	    {
		this.setDescription( (String)this.descriptionBox.getSelectedItem() );
	    }
	}
    }
}
