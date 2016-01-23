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
package org.awl.header;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.awl.ImagePanel;
import org.awl.message.Message;
import org.awl.message.MessageHandler;
import org.awl.message.MessageUtilities;

/**
 *
 * Header panel that could display a description,
 * an icon and a message
 *
 * @author alexis
 */
public abstract class AbstractWizardHeader extends JPanel implements WizardHeader,
							                 MessageHandler
{
    /** title label */
    private JLabel titleLabel       = null;
    
    /** description label */
    private JLabel descriptionLabel = null;
    
    /** icon label */
    private JLabel iconLabel        = null;
    
    /** message label */
    private JLabel messageLabel     = null;
    
    /**
     * Creates a new instance of AbstractWizardHeader
     */
    public AbstractWizardHeader()
    {
//	this.setBackgroundImage(null);
	
	this.titleLabel       = new JLabel();
	this.descriptionLabel = new JLabel();
	this.iconLabel        = new JLabel();
	this.messageLabel     = new JLabel();
	
	Font font = this.descriptionLabel.getFont();
	this.titleLabel.setFont(font.deriveFont((float)(font.getSize() + 2)));
	this.descriptionLabel.setFont(font.deriveFont(Font.ITALIC, (float)(font.getSize() + 0)));
	
	this.titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
	this.descriptionLabel.setHorizontalTextPosition(SwingConstants.LEFT);
	this.descriptionLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0), this.descriptionLabel.getBorder()));
	this.messageLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
	
	boolean opaque = false;
	this.descriptionLabel.setOpaque(opaque);
	this.iconLabel.setOpaque(opaque);
	this.messageLabel.setOpaque(opaque);
	
	Color color = Color.WHITE;
	this.setBackground(color);
	this.setLayout(new GridBagLayout());
	
	GridBagConstraints gbc = new GridBagConstraints();
	
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 0.0f;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	this.add(this.getTitleLabel(), gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 2;
	gbc.weightx = 0.0f;
	gbc.fill = GridBagConstraints.NONE;
	this.add(Box.createHorizontalStrut(3), gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 3;
	gbc.weightx = 0.0f;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	this.add(this.getDescriptionLabel(), gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 4;
	gbc.weightx = 0.0f;
	gbc.fill = GridBagConstraints.NONE;
	this.add(Box.createHorizontalStrut(4), gbc);
	
	gbc.gridx = 1;
	gbc.gridy = 5;
	gbc.weightx = 0.0f;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	this.add(this.getMessageLabel(), gbc);
	
	gbc.gridx = 2;
	gbc.gridy = 2;
	gbc.gridheight = 5;
	gbc.weightx = 1.0f;
	this.add(Box.createHorizontalBox(), gbc);
	
	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.gridheight = 5;
	gbc.weightx = 0.0f;
	gbc.fill = GridBagConstraints.BOTH;
	this.add(this.getIconLabel(), gbc);
	
	this.setPreferredSize(new java.awt.Dimension(70, 70));
	
	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
    }
    
    /** return the component that represents the header
     *	@return a Component
     */
    public Component getComponent()
    {
	return this;
    }
    
    public JLabel getTitleLabel()
    {
	return this.titleLabel;
    }

    public JLabel getDescriptionLabel()
    {
	return descriptionLabel;
    }

    public JLabel getIconLabel()
    {
	return iconLabel;
    }

    public JLabel getMessageLabel()
    {
	return messageLabel;
    }
    
    /** ########################################################################
     *  #################### MessageHandler implementation #####################
     *  ######################################################################## */
    
    /** process a message
     *	@param message a Message
     */
    public void processMessage(Message message)
    {
	if ( message == null )
	{
	    if ( this.getMessageLabel() != null )
	    {
		this.getMessageLabel().setIcon(null);
		this.getMessageLabel().setText("");
	    }
	}
	else
	{
	    if ( this.getMessageLabel() != null )
	    {
		this.getMessageLabel().setIcon(MessageUtilities.getIconForLevel(message.getLevel()));
		this.getMessageLabel().setText(message.getContent());
	    }
	}
    }
    
}
