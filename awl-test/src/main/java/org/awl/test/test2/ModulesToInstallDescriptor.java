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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.awl.DefaultWizardPageDescriptor;

/**
 *
 * @author alexis
 */
public class ModulesToInstallDescriptor extends DefaultWizardPageDescriptor
{
    
    /** Creates a new instance of ModulesToInstallDescriptor */
    public ModulesToInstallDescriptor()
    {
        super("Select modules to install");
        
        /** build content */
        JPanel panel = new JPanel();
        
        JList available = new JList();
        
        JList toInstall = new JList();
        
        available.setVisibleRowCount(10);
        toInstall.setVisibleRowCount(10);
        available.setFixedCellWidth(100);
        toInstall.setFixedCellWidth(100);
        
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS);
        buttonPanel.setLayout(layout);
        JButton button = null;
        button = new JButton("Add >");
        button.setAlignmentX(0.5f);
        buttonPanel.add(button);
        button = new JButton("< Remove");
        button.setAlignmentX(0.5f);
        buttonPanel.add(button);
        button = new JButton("Add all >");
        button.setAlignmentX(0.5f);
        buttonPanel.add(button);
        button = new JButton("< Remove all");
        button.setAlignmentX(0.5f);
        buttonPanel.add(button);
        
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = null;
        
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = gbc.NORTHWEST;
        
        panel.add(new JScrollPane(available), gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = gbc.NORTH;
        
        panel.add(buttonPanel, gbc);
        
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = gbc.NORTHEAST;
        
        panel.add(new JScrollPane(toInstall), gbc);
        
        panel.setAlignmentY(1.0f);
        
        
        this.setComponent(panel);
    }
    
}
