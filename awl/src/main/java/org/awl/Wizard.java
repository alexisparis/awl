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
package org.awl;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.event.WizardModelEvent;
import org.awl.event.WizardModelListener;
import org.awl.exception.PageDescriptorChangingException;
import org.awl.message.Message;
import org.awl.message.MessageHandler;
import org.awl.message.MessageLevel;
import org.awl.model.DefaultWizardModel;
import org.awl.model.WizardModel;
import org.awl.util.WizardButton;

/**
 * 
 * Abstract representation of a Wizard which
 * has no preferred components disposition
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public abstract class Wizard extends JDialog implements WizardModelListener
{
    /** property Wizard model */
    public static final String PROPERTY_MODEL       = "wizardModel";
    
    /** property message handler */
    public static final String PROPERTY_MSG_HANDLER = "messageHandler";
    
    /** wizard model */
    private WizardModel            model           = null;
    
    /** wizard controller */
    private WizardController       controller      = null;
    
    /** custom panel added to buttonPanel to allow developper to add its own components */
    private JPanel                 additionalPanel = null;
    
    /** help button */
    private JButton                help            = null;
    
    /** back button */
    private JButton                back            = null;
    
    /** next button */
    private JButton                next            = null;
    
    /** finish button */
    private JButton                finish          = null;
    
    /** cancel button */
    private JButton                cancel          = null;
    
    /** page panel */
    private JPanel                 pagesPanel      = null;
    
    /** layout of the page panel */
    private CardLayout             cardLayout      = null;
    
    /** buttons panel */
    private JPanel                 buttonPanel     = null;
    
    /** actionListener */
    private ActionListener         actionListener  = null;
    
    /** model propertyChangeListener */
    private PropertyChangeListener modelListener   = null;
    
    /** map linking descriptor Component and descriptor id */
    private Map                    componentMap    = null;
    
    /** message handler */
    private MessageHandler         msgHandler      = null;
    
    /** initialization independent from constructor */
    {
        this.initComponents();
        
        this.setModel(new DefaultWizardModel());
    }
    
    /**
     * Creates a new instance of Wizard
     * 
     *  @param owner a Frame
     *	@param modal true to set the dialog modal
     */
    public Wizard(Frame owner, boolean modal)
    {   super(owner, modal); }
    
    /**
     * Creates a new instance of Wizard
     * 
     * @param dialog a Dialog
     *	@param modal true to set the dialog modal
     */
    public Wizard(Dialog dialog, boolean modal)
    {   super(dialog, modal); }
    
    /** get the MessageHandler
     *	@return a MessageHandler
     */
    public MessageHandler getMessageHandler()
    {
	return this.msgHandler;
    }
    
    /** initialize the MessageHandler
     *	@param handler a MessageHandler
     */
    public void setMessageHandler(MessageHandler handler)
    {
	if ( handler != this.getMessageHandler() )
	{
	    MessageHandler oldHandler = this.getMessageHandler();
	    
	    this.msgHandler = handler;
	    
	    this.firePropertyChange(PROPERTY_MSG_HANDLER, oldHandler, this.getMessageHandler());
	}
    }
    
    /** called to resize wizard if needed
     *	this method only called pack if the preferred size of the wizard is greater (in width or height)
     *	as the actual size
     */
    protected void resizeIfNeeded()
    {
	if ( false )
	{
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    Dimension currentSize = getSize();
		    Dimension prefSize    = getPreferredSize();

		    if ( prefSize.width > currentSize.width || prefSize.height > currentSize.height )
		    {
			pack();
		    }
		}
	    };

	    if ( SwingUtilities.isEventDispatchThread() )
	    {
		runnable.run();
	    }
	    else
	    {
		SwingUtilities.invokeLater(runnable);
	    }
	}
    }

    
    /* called on initialization after components initailization
     *	to add them to the content pane of the dialog
     */
    protected abstract void placeComponents();
    
    /** initialize the components inside the dialog
     *	this methods does not add the components to the content pane. it only initializes components
     *	create the button container and add buttons to it
     */
    protected void initComponents()
    {   
        this.cardLayout = new CardLayout();
        this.pagesPanel = new JPanel(this.cardLayout);
	
        GridBagConstraints gbc = new GridBagConstraints();
        
        /* build buttons panel */
        this.buttonPanel = new JPanel();
	GridBagLayout buttonPanelLayout = new GridBagLayout();
	this.buttonPanel.setLayout(buttonPanelLayout);
        
	gbc.gridy = 1;
	
	gbc.gridx = 1;
	this.help = new JButton(new ImageIcon(this.getClass().getResource("/org/awl/rc/help.png")));
	this.help.setFocusPainted(false);
	this.help.setOpaque(false);
	this.help.setContentAreaFilled(false);
	this.help.setBorder(null);
	this.help.setVisible(false); // disabled for the moment
	this.buttonPanel.add(this.help, gbc);
	
	gbc.gridx = 2;
	gbc.fill  = gbc.HORIZONTAL;
	gbc.weightx = 1.0f;
        this.buttonPanel.add(Box.createHorizontalGlue(), gbc);
        
	gbc.gridx = 3;
	gbc.fill  = gbc.NONE;
	gbc.weightx = 0.0f;
        this.back = new WizardButton();
        this.back.setName("back");
        this.back.addActionListener(this.getActionListener());
        this.buttonPanel.add(this.back, gbc);
        
	gbc.gridx = 4;
        this.buttonPanel.add(Box.createHorizontalStrut(4), gbc);
        
	gbc.gridx = 5;
        this.next = new WizardButton();
        this.next.setName("next");
        this.next.addActionListener(this.getActionListener());
        this.buttonPanel.add(this.next, gbc);
        
	gbc.gridx = 6;
        this.buttonPanel.add(Box.createHorizontalStrut(16), gbc);
        
	gbc.gridx = 7;
        this.finish = new WizardButton();
        this.finish.setName("finish");
        this.finish.addActionListener(this.getActionListener());
        this.buttonPanel.add(this.finish, gbc);
        
	gbc.gridx = 8;
        this.buttonPanel.add(Box.createHorizontalStrut(8), gbc);
        
	gbc.gridx = 9;
        this.cancel = new WizardButton();
        this.cancel.setName("cancel");
        this.cancel.addActionListener(this.getActionListener());
        this.buttonPanel.add(this.cancel, gbc);
        
        this.buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        Font f = this.getBackButton().getFont().deriveFont(8);
        this.setButtonsFont(f);
    }
    
    /** method called when the dialog have to display a message from controller or anything else
     *  @param message the message to display
     *
     *	@deprecated
     */
    public void displayMessage(String message)
    {
	if ( message == null )
	{
	    this.hideCurrentMessage();
	}
	else
	{
	    Message msg = new Message(message, MessageLevel.ERROR);
	    this.displayMessage(msg);
	}
    }
    
    /** update the message according to the current descriptor */
    public void refreshMessages()
    {
	Message message = null;
	
	WizardPageDescriptor desc = this.getModel().getCurrentDescriptor();
	
	if ( desc != null )
	{
	    /* search for the message with the higher priority */
	    for(int i = MessageLevel.LEVELS.length - 1; i >= 0; i--)
	    {
		MessageLevel level = MessageLevel.LEVELS[i];
		
		if ( level != null )
		{
		    String msg = desc.getMessage(level);
		    
		    if ( msg != null )
		    {
			message = new Message(msg, level);
			break;
		    }
		}
	    }
	}
	
	this.displayMessage(message);
    }
    
    /** ask wizard to show this message
     *	@param message a Message or null to hide current message
     */
    public void displayMessage(Message message)
    {
	MessageHandler handler = this.getMessageHandler();
	
	if ( handler != null )
	{
	    handler.processMessage(message);
	}
	else
	{
	    /** use default wizard behaviour */
	    this._displayMessage(message);
	}
    }
    
    /** ask wizard to show this message by its own
     *	@param message a Message or null to hide current message
     */
    protected abstract void _displayMessage(Message message);
    
    /** ask wizard to hide the current message if it exists */
    public void hideCurrentMessage()
    {
	this.displayMessage((Message)null);
    }
    
    /** return a code representing the manner that wizard disappear
     *  @return an integer defined in WizardConstants : <br>
     *          WIZARD_VALID_OPTION if finish has been pressed and accepted 
     *          WIZARD_CLOSED_OPTION if close button has been pressed and accepted 
     *          WIZARD_CANCEL_OPTION if cancel has been pressed and accepted 
     */
    public int getReturnCode()
    {   return this.getController().getReturnCode(); }
    
    /** set the wizard model
     *  @param model a WizardModel
     */
    public void setModel(WizardModel model)
    {   if ( model == null )
            throw new IllegalArgumentException("model must be non null");
	
	if ( model != this.getModel() )
	{
	    WizardModel oldModel = this.getModel();
	    
	    if ( this.getModel() != null && this.modelListener != null )
	    {   this.getModel().removeWizardModelListener(this);
		if ( this.getController() != null )
		    this.getModel().removeWizardModelListener(this.getController());
	    }

	    this.model = model;

	    this.getModel().addWizardModelListener(this);
	    if ( this.getController() != null )
	    {   this.getModel().addWizardModelListener(this.getController()); }
	    
	    this.firePropertyChange(PROPERTY_MODEL, oldModel, this.getModel());
	}
    }
    
    /** add a new WizardPageDescriptor
     *	@param descriptor a WizardPageDescriptor
     *	@param id the id of the page
     */
    public void addPage(WizardPageDescriptor descriptor, String id)
    {
	this.addPage(descriptor, id, (descriptor == null ? null : descriptor.getPreviousDescriptorId()),
				     (descriptor == null ? null : descriptor.getNextDescriptorId()));
    }
    
    /** add a new WizardPageDescriptor
     *	@param descriptor a WizardPageDescriptor
     *	@param id the id of the page
     *	@param previousPageId the id of the previous page
     *	@param nextPageId the id of the next page
     */
    public void addPage(WizardPageDescriptor descriptor, String id, String previousPageId, String nextPageId)
    {
	if ( descriptor == null )
	{
	    throw new IllegalArgumentException("descriptor could not be null");
	}
	if ( id == null )
	{
	    throw new IllegalArgumentException("id could not be null");
	}
	if ( previousPageId == null )
	{
	    throw new IllegalArgumentException("'previous page id' could not be null");
	}
	if ( nextPageId == null )
	{
	    throw new IllegalArgumentException("'next page id' could not be null");
	}
	
	/** apply previous and next id */
	descriptor.setPreviousDescriptorId(previousPageId);
	descriptor.setNextDescriptorId(nextPageId);
	
	/** register page */
	this.getModel().registerWizardPanel(id, descriptor);
    }
    
    /** return the wizard model
     *  @return a WizardModel
     */
    public WizardModel getModel()
    {   return this.model; }
    
    /** return the WizardController
     *  @return a WizardController
     */
    public WizardController getController()
    {   
	/* lazy instantiation */
	if ( this.controller == null )
	{
	    this.setController(new DefaultWizardController());
	}
	return this.controller;
    }
    
    /** initialize the WizardController
     *  @param controller a WizardController
     */
    public void setController(WizardController controller)
    {   if ( controller == null )
            throw new IllegalArgumentException("model must be non null");
        
        if ( this.controller != null )
        {   if ( this.getModel() != null )
                this.getModel().removeWizardModelListener(this.controller);
        }
        
        this.controller = controller;
        
        this.controller.setWizard(this);
        if ( this.getModel() != null )
            this.getModel().addWizardModelListener(this.controller);
    }
    
    /** return the actionListener used for buttons
     *  @return an ActionListener
     */
    protected synchronized ActionListener getActionListener()
    {   if ( this.actionListener == null )
        {   this.actionListener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {   WizardController control = getController();
                    if ( control != null )
                    {   if ( e.getSource() == getBackButton() )
                        {   control.backButtonPressed(); }
                        else if ( e.getSource() == getNextButton() )
                        {   control.nextButtonPressed(); }
                        else if ( e.getSource() == getFinishButton() )
                        {   control.finishButtonPressed(); }
                        else if ( e.getSource() == getCancelButton() )
                        {   control.cancelButtonPressed(); }
                    }
                }
            };
        }
        return this.actionListener;
    }
    
    /** set the dialog visible and display it on the center of the owner
     *	this method has no effect if the wizard is already visible
     */
    public void setVisibleOnCenterOfOwner()
    {   if ( ! this.isVisible() )
        {   this.centerOnOwner();
            this.setVisible(true);
        }
    }
    
    /** change wizard location to display it at the center of the owner */
    public void centerOnOwner()
    {
        Window window = this.getOwner();
        if ( window != null )//&& this.isVisible() )
        {   Point p = window.getLocationOnScreen();
            this.setLocationRelativeTo(window);
            Point point = new Point((window.getSize().width - this.getSize().width) / 2 + p.x,
                                    (window.getSize().height - this.getSize().height) / 2 + p.y);
            if ( point.x < 0 )
                point.x = 0;
            if ( point.y < 0 )
                point.y = 0;
            
            this.setLocation(point);
        }
    }
    
    /** set the dialog visible and display it on the center of the screen
     *	this method has no effect if the wizard is already visible
     */
    public void setVisibleOnCenterOfScreen()
    {
	if ( ! this.isVisible() )
        {   this.centerOnScreen();
            this.setVisible(true);
        }
    }
    
    /** change wizard location to display it at the center of the screen */
    public void centerOnScreen()
    {
	Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hauteur = (int)tailleEcran.getHeight();
	int largeur = (int)tailleEcran.getWidth();
	
	this.setLocation( (largeur - this.getSize().width) / 2,
			  (hauteur - this.getSize().height) / 2 );
    }
    
    /** override setVisible to be able to warn controller */
    public void setVisible(boolean b)
    {
	final boolean wasVisible = this.isVisible();
	
        if ( b != wasVisible )
        {   if ( b )
            {   this.getController().aboutToDisplayWizard();
		
		/** ensure to call aboutToDisplayPanel on the current page */
		WizardPageDescriptor current = this.getModel().getCurrentDescriptor();
		if ( current != null )
		{
		    current.aboutToDisplayPanel(this);
		}
	    }
            else
            {   this.getController().aboutToHideWizard();
		
		/** ensure to call aboutToHidePanel on the current page */
		WizardPageDescriptor current = this.getModel().getCurrentDescriptor();
		if ( current != null )
		{
		    current.aboutToHidePanel(this);
		}
	    }
        }
	
	/** create a runnable that call displayingPanel or hidingPanel on current descriptor */
	Runnable runnable = null;
	if ( wasVisible != b )
	{
	    runnable = new Runnable()
	    {
		public void run()
		{
		    if ( wasVisible )
		    {   
			/** ensure to call aboutToHidePanel on the current page */
			WizardPageDescriptor current = getModel().getCurrentDescriptor();
			if ( current != null )
			{
			    current.hidingPanel(Wizard.this);
			}
		    }
		    else
		    {   
			/** ensure to call aboutToDisplayPanel on the current page */
			WizardPageDescriptor current = getModel().getCurrentDescriptor();
			if ( current != null )
			{
			    current.displayingPanel(Wizard.this);
			}
		    }
		}
	    };
	}
	
	/** if the wizard is modal, then we must call runnable before calling super.setVisible(true);
	 *  if b = true
	 */
	if ( runnable != null && b && this.isModal() )
	{
	    runnable.run();
	    runnable = null;
	}
        
        super.setVisible(b);
	
	/** if we must run the runnable, ... go */
	if ( runnable != null )
	{
	    runnable.run();
	    runnable = null;
	}
        
        if ( ! b )
        {   /** reinitialize the wizard :
             *  set as current descriptor the first descriptor which previous id is STARTING_ID
             */
            this.getModel().reinit();
        }
    }
    
    /** set the font to use for button
     *  @param font the font to apply to buttons
     */
    public void setButtonsFont(Font font)
    {   if ( font == null )
            throw new IllegalArgumentException("font have to be non null");
        if ( this.getBackButton() != null )
            this.getBackButton().setFont(font);
        if ( this.getNextButton() != null )
            this.getNextButton().setFont(font);
        if ( this.getFinishButton() != null )
            this.getFinishButton().setFont(font);
        if ( this.getCancelButton() != null )
            this.getCancelButton().setFont(font);
    }
    
    /** add a new WizardPanelDescriptor related to an id
     *  @param id the id of the WizardPanelDescriptor. id have to be non null and must be different from <br>
     *      WizardConstants.TERMINAL_DESCRIPTOR_ID and from WizardConstants.STARTING_DESCRIPTOR_ID.
     *  @param descriptor an instance of WizardPanelDescriptor
     *
     *	@deprecated : use addPage
     */
    public void registerWizardPanel(String id, WizardPageDescriptor descriptor)
    {   this.getModel().registerWizardPanel(id, descriptor); }
    
    /** return the back bouton
     *  @return a JButton
     */
    public JButton getBackButton()
    {   return this.back; }
    
    /** return the next bouton
     *  @return a JButton
     */
    public JButton getNextButton()
    {   return this.next; }
    
    /** return the cancel bouton
     *  @return a JButton
     */
    public JButton getCancelButton()
    {   return this.cancel; }
    
    /** return the finish bouton
     *  @return a JButton
     */
    public JButton getFinishButton()
    {   return this.finish; }
    
    /** return the panel containing the page components
     *	@return a Container
     */
    protected Container getPagesPanel()
    {
	return this.pagesPanel;
    }
    
    /** return the container where navigation buttons have to be added
     *	@return a Container
     */
    protected Container getButtonsContainer()
    {
	return this.buttonPanel;
    }
    
//    @Override
    public void dispose()
    {
        /* remove ActionListener */
        if ( this.actionListener != null )
        {   
//	    if ( this.getBackButton() != null )
//	    {
//                this.getBackButton().removeActionListener(this.getActionListener());
//	    }
//            if ( this.getNextButton() != null )
//	    {
//                this.getNextButton().removeActionListener(this.getActionListener());
//	    }
//            if ( this.getFinishButton() != null )
//	    {
//                this.getFinishButton().removeActionListener(this.getActionListener());
//	    }
//            if ( this.getCancelButton() != null )
//	    {
//                this.getCancelButton().removeActionListener(this.getActionListener());
//	    }
        }
        
        super.dispose();
    }
    
    /** indicate to the wizard that a property change on a descriptor
     *	@param descriptor the descritor that changed
     *	@param evt a PropertyChangeEvent reflecting the modification
     */
    public void descriptorChanged(WizardPageDescriptor descriptor, PropertyChangeEvent evt)
    {
	/* to override */
    }
    
    /* #########################################################################
     * ################### WizardModelListener implementation ##################
     * ######################################################################### */
    
    /**
     * a new descriptor has been set as current descriptor
     *
     * @param evt a CurrentDescriptorChangedEvent
     */
    public void currentDescriptorChanged(final CurrentDescriptorChangedEvent evt)
    {   
	if ( evt.getModel() == this.getModel() )
        {   
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    refreshMessages();
		}
	    };
		
	    if ( SwingUtilities.isEventDispatchThread() )
	    {
		runnable.run();
	    }
	    else
	    {
		SwingUtilities.invokeLater(runnable);
	    }
	    
	    if ( cardLayout != null )
            {
		Runnable runnable2 = new Runnable()
		{
		    public void run()
		    {
			/** must call aboutToHidePanel and aboutToDisplayPanel
			 *  only if the wizard is visible
			 */
			if ( isVisible() )
			{
			    if ( evt.getOldDesc() != null )
				evt.getOldDesc().aboutToHidePanel(Wizard.this);

			    if (evt.getNewDesc() != null )
				evt.getNewDesc().aboutToDisplayPanel(Wizard.this);
			}

			String id = null;
			if ( componentMap != null && evt.getNewDesc() != null )
			{   id = (String)componentMap.get(evt.getNewDesc().getComponent()); }

			cardLayout.show(pagesPanel, id);

			/** must call hidingPanel and displayingPanel
			 *  only if the wizard is visible
			 */
			if ( isVisible() )
			{
			    if ( evt.getOldDesc() != null )
				evt.getOldDesc().hidingPanel(Wizard.this);

			    if ( evt.getNewDesc() != null )
				evt.getNewDesc().displayingPanel(Wizard.this);
			}
		    }
		};
		
		if ( SwingUtilities.isEventDispatchThread() )
		{
		    runnable2.run();
		}
		else
		{
		    SwingUtilities.invokeLater(runnable2);
		}
            }
        }
    }
    
    /** method that is called to verify that all WizardModelListener agree to change the current descriptor
     *  @param currentDescriptor the current descriptor
     *  @param candidate the descriptor we try to set as current descriptor
     *
     *  @exception PageDescriptorChangingException if the listener refused the change
     */
    public void checkCurrentDescriptorChanging(WizardPageDescriptor currentDescriptor, WizardPageDescriptor candidate) throws PageDescriptorChangingException
    {   /* do nothing */ }
    
    /**
     * a new descriptor has been added
     *
     * @param event a WizardModelEvent
     */
    public void descriptorAdded(final WizardModelEvent event)
    {
        if ( event != null && event.getDescriptor() != null && event.getModel() == this.getModel() )
        {   
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    pagesPanel.add(event.getDescriptor().getComponent(), event.getId());
		}
	    };
	    
	    if ( SwingUtilities.isEventDispatchThread() )
	    {
		runnable.run();
	    }
	    else
	    {
		SwingUtilities.invokeLater(runnable);
	    }
            
            if ( this.componentMap == null )
                this.componentMap = new HashMap();
            this.componentMap.put(event.getDescriptor().getComponent(), event.getId());
        }
    }
    
    /** a new descriptor has been removed
     *  @param event a WizardModelEvent
     */
    public void descriptorRemoved(final WizardModelEvent event)
    {
        if ( event != null && event.getDescriptor() != null && event.getModel() == this.getModel() )
        {   
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    pagesPanel.remove(event.getDescriptor().getComponent());
		}
	    };
	    
	    if ( SwingUtilities.isEventDispatchThread() )
	    {
		runnable.run();
	    }
	    else
	    {
		SwingUtilities.invokeLater(runnable);
	    }
            
            if ( this.componentMap != null )
	    {
		this.componentMap.remove(event.getDescriptor().getComponent());
	    }
        }
    }
    
    /** replace the component of a descriptor when it changes its component
     *	@param descriptor the descriptor which component has changed
     *	@param oldComponent the old component of the descriptor
     *	@param newComponent the new component of the descriptor
     */
    public void updateDescriptorComponent(WizardPageDescriptor descriptor, Component oldComponent, Component newComponent)
    {
	/* update map */
	if ( this.componentMap != null )
	{
	    String id = (String)this.componentMap.get(oldComponent);
	    
	    boolean oldComponentShowing = false;
	    if ( oldComponent != null )
	    {
		oldComponentShowing = oldComponent.isShowing();
	    }
	
	    this.componentMap.put(newComponent, id);

	    /** update page panel */
	    if ( oldComponent != null )
	    {
		this.pagesPanel.remove(oldComponent);
	    }
	    this.pagesPanel.add(newComponent, id);
	    
	    /** if the component modified is the current component being displayed, then 
	     *	force to repaint wizard
	     */
	    if ( oldComponentShowing )
	    {
		this.cardLayout.show(this.pagesPanel, id);
	    }
	}
    }
    
}
