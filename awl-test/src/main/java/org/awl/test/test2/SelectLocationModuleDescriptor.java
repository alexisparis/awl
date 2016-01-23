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
package org.awl.test.test2;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.awl.DefaultWizardPageDescriptor;

/**
 *
 * @author alexis
 */
public class SelectLocationModuleDescriptor extends DefaultWizardPageDescriptor
{
    
    /** Creates a new instance of SelectLocationModuleDescriptor */
    public SelectLocationModuleDescriptor()
    {   
        super("Select location of modules");
        
        /* build content */
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);
        
        ButtonGroup group = new ButtonGroup();
        
        JRadioButton search = new JRadioButton(
                            new AbstractAction("Check the web for available updates and new modules")
        {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(ActionEvent e)
            {
                
            }
            
        });
        JRadioButton manually = new JRadioButton(
                            new AbstractAction("Install manually downloaded modules (.sbp)")
        {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(ActionEvent e)
            {
                
            }
            
        });
        
        group.add(search);
        group.add(manually);
        
        group.setSelected(search.getModel(), true);
        
        panel.add(search);
        panel.add(manually);
        
        this.setComponent(panel);
    }
    
}
