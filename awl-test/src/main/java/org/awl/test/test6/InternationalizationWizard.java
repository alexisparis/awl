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
package org.awl.test.test6;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.awl.DefaultWizardPageDescriptor;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;

/**
 *
 * Test internationalization changes into the wizard
 *
 * @author alexis
 */
public class InternationalizationWizard extends DefaultWizard
{
    /**
     * Creates a new instance of InternationalizationWizard
     */
    public InternationalizationWizard(Frame frame)
    {
	super(frame);
	
        DefaultWizardPageDescriptor page1 = new DefaultWizardPageDescriptor();
	
	this.setLocale(Locale.ENGLISH);
	
	JPanel panel = new JPanel();
	panel.add(new JLabel("Change locale"));
	final LocaleComboBox comboBox = new LocaleComboBox();
	panel.add(comboBox);
	
	comboBox.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent e)
	    {
		/** apply the new Locale to the wizard */
		Object item = comboBox.getSelectedItem();
		if ( item instanceof Locale )
		{
		    setLocale( (Locale)item );
		}
	    }
	});
	
	comboBox.setSelectedItem(this.getLocale());
	
        page1.setComponent(panel);
        page1.setDescription("Choose Locale");
        
        this.registerWizardPanel("1", page1);
        
        this.setDefaultCloseOperation(DefaultWizard.DISPOSE_ON_CLOSE);
        this.setTitle("Test i18n changes on the wizard ...");
//        this.setSize(new Dimension(430, 300));
//        this.setPreferredSize(new Dimension(430, 300));
	this.pack();
    }
    
}
