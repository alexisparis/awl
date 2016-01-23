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
package org.awl.test.testB;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;
import org.awl.WizardPageDescriptor;

/**
 *
 * @author alexis
 */
public class DynamicPathWizard extends DefaultWizard
{   
    /**
     * Creates a new instance of DynamicPathWizard
     */
    public DynamicPathWizard(Frame frame)
    {
	super(frame);
	
	WizardPageDescriptor page1 = new WizardPage("a", "1");
	page1.setNextDescriptorId("2");
	WizardPageDescriptor page2 = new WizardPage("b", "2");
	page2.setPreviousDescriptorId("1");
	page2.setNextDescriptorId("3");
	WizardPageDescriptor page3 = new WizardPage("c", "3");
	page3.setPreviousDescriptorId("2");
        
        this.registerWizardPanel("1", page1);
        this.registerWizardPanel("2", page2);
        this.registerWizardPanel("3", page3);
        
        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("dynamic path...");
        this.setSize(new Dimension(430, 300));
    }
    
    /** navigable page */
    private class WizardPage extends DefaultWizardPageDescriptor
    {
	/** combo */
	private JComboBox combo = null;
	
	public WizardPage(String desc, final String id)
	{
	    super(desc);
	    
	    JPanel panel = new JPanel();
	    GridBagLayout layout = new GridBagLayout();
	    
	    panel.setLayout(layout);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    
	    JLabel label = new JLabel("choose next page");
	    label.setAlignmentX(0.0f);
	    label.setAlignmentY(0.0f);
	    panel.add(label, gbc);
	    
	    gbc.gridx = 2;
	    gbc.gridy = 1;
	    gbc.fill = GridBagConstraints.NONE;
	    
	    this.combo = new JComboBox();
	    DefaultComboBoxModel model = new DefaultComboBoxModel();
	    model.addElement(WizardConstants.STARTING_DESCRIPTOR_ID);
	    model.addElement("1");
	    model.addElement("2");
	    model.addElement("3");
	    model.addElement(WizardConstants.TERMINAL_DESCRIPTOR_ID);
	    this.combo.setModel(model);
	    this.combo.setAlignmentX(0.0f);
	    this.combo.setAlignmentY(0.0f);
	    
	    this.combo.addItemListener(new ItemListener()
	    {
		public void itemStateChanged(ItemEvent e)
		{
		    /** selection changed
		     *	reapply next page
		     */
		    setNextDescriptorId((String)combo.getSelectedItem());
		    WizardPageDescriptor desc = getModel().getDescriptor((String)combo.getSelectedItem());
		    if ( desc != null )
		    {
			desc.setPreviousDescriptorId(id);
		    }
		}
	    });
	    
	    panel.add(this.combo, gbc);
	    
	    this.setComponent(panel);
	}

	public void aboutToDisplayPanel(DefaultWizard wizard)
	{
	    super.aboutToDisplayPanel(wizard);
	    
	    /** refresh combo */
	    this.combo.setSelectedItem(this.getNextDescriptorId());
	}
    }
}
