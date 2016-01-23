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

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.event.DescriptorModificationEvent;
import org.awl.event.DescriptorModificationListener;
import org.awl.event.WizardModelEvent;
import org.awl.exception.PageDescriptorChangingException;
import org.awl.exception.PagesCycleDetectedException;
import org.awl.exception.UnregisteredDescriptorException;
import org.awl.model.PageContainer;
import org.awl.model.WizardModel;

/**
 *
 * Basic implementation of WizardController which listen to all WizardPageDescriptors contained by the model
 *
 * @author alexis
 */
public class DefaultWizardController extends AbstractWizardController implements PropertyChangeListener,
										 DescriptorModificationListener
{   
    /** Creates a new instance of DefaultWizardController */
    public DefaultWizardController()
    {	this(true); }
    
    /** Creates a new instance of DefaultWizardController
     *	@param manageI18n true if the controller should manage internationalization
     */
    public DefaultWizardController(boolean manageI18n)
    {   
	super(manageI18n);
    }

    /**
     * method called when next is pressed
     */
    public void nextButtonPressed()
    {   /* search for the next descriptor to visualize */
        WizardModel model = this.getWizard().getModel();
        WizardPageDescriptor descriptor = model.getCurrentDescriptor();
        if ( descriptor != null )
        {   try
            {   model.setCurrentDescriptor(descriptor.getNextDescriptorId()); }
            catch(UnregisteredDescriptorException e)
            {   JOptionPane.showMessageDialog(this.getWizard(), "unable to go to next descriptor",
                                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(PageDescriptorChangingException e)
            {   /* display the message to warn the user */
                this.getWizard().displayMessage(e.getMessage());
            }
        }
    }

    /**
     * method called when finish is pressed
     */
    public void finishButtonPressed()
    {   
        /** check that the last page has no problem ... */
        WizardPageDescriptor descriptor = this.getWizard().getModel().getCurrentDescriptor();
        if ( descriptor != null )
        {   try
            {   this.getWizard().getModel().setCurrentDescriptor(WizardConstants.TERMINAL_DESCRIPTOR_ID);
                
                this.getWizard().setVisible(false);

                this.setReturnCode(WizardConstants.WIZARD_VALID_OPTION);
            }
            catch(UnregisteredDescriptorException e)
            {   JOptionPane.showMessageDialog(this.getWizard(), "unable to go to next descriptor",
                                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(PageDescriptorChangingException e)
            {   /* display the message to warn the user */
                this.getWizard().displayMessage(e.getMessage());
            }
        }
    }

    /**
     * method called when cancel is pressed
     */
    public void cancelButtonPressed()
    {   this.getWizard().setVisible(false);
        
        this.setReturnCode(WizardConstants.WIZARD_CANCEL_OPTION);
    }

    /**
     * method called when back is pressed
     */
    public void backButtonPressed()
    {   /* search for the previous descriptor to visualize */
        WizardModel model = this.getWizard().getModel();
        WizardPageDescriptor descriptor = model.getCurrentDescriptor();
        if ( descriptor != null )
        {   try
            {   model.setCurrentDescriptor(descriptor.getPreviousDescriptorId()); }
            catch(UnregisteredDescriptorException e)
            {   JOptionPane.showMessageDialog(this.getWizard(), "unable to go to previous descriptor",
                                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(PageDescriptorChangingException e)
            {   /* display the message to warn the user */
                this.getWizard().displayMessage(e.getMessage());
            }
        }
    }

    /**
     * set the wizard attached to this Controller
     * 
     * @param wizard a Wizard
     */
    public void setWizard(Wizard wizard)
    {   
	Wizard oldWizard = this.getWizard();
	
        if ( oldWizard != wizard && oldWizard != null)
        {   
	    oldWizard.removePropertyChangeListener(Wizard.PROPERTY_MODEL, this);
	    
	    WizardModel model = oldWizard.getModel();
	    if ( model != null )
	    {
		model.removeDescriptorModificationListener(this);
	    }
        }
        
        super.setWizard(wizard);
	
	this.updateNavigationButtonsIconAndText();
        
        if ( oldWizard != wizard )
        {   
	    if ( wizard != null )
	    {
		wizard.addPropertyChangeListener(Wizard.PROPERTY_MODEL, this);

		WizardModel model = wizard.getModel();
		if ( model != null )
		{
		    model.addDescriptorModificationListener(this);
		}
	    }
        }
    }
    
    /** set the icon and text of the buttons */
    protected void updateNavigationButtonsIconAndText()
    {
	if ( this.getWizard() != null )
	{
	    /* initialize button caracteristics icons ... */
	    try
	    {   this.getWizard().getBackButton().setIcon(new ImageIcon(this.getClass().getResource("/org/awl/rc/left.png"))); }
	    catch(Exception e)
	    {   /*e.printStackTrace();*/ }
	    try
	    {   this.getWizard().getNextButton().setIcon(new ImageIcon(this.getClass().getResource("/org/awl/rc/right.png"))); }
	    catch(Exception e)
	    {   /*e.printStackTrace();*/ }
	    try
	    {   this.getWizard().getCancelButton().setIcon(new ImageIcon(this.getClass().getResource("/org/awl/rc/cancel.png"))); }
	    catch(Exception e)
	    {   /*e.printStackTrace();*/ }
	    try
	    {   this.getWizard().getFinishButton().setIcon(new ImageIcon(this.getClass().getResource("/org/awl/rc/finish.png"))); }
	    catch(Exception e)
	    {   /*e.printStackTrace();*/ }
	}

	this.internationalize( (this.getWizard() == null ? Locale.getDefault() : this.getWizard().getLocale()) );
    }
    
    /** method called when the wizard is about to be shown on screen */
//    @Override
    public void aboutToDisplayWizard()
    {   
	super.aboutToDisplayWizard();
	
	this.initializeButtonsStates();
	
	this.tryToRefreshWizardSummary();
    }
    
    /** try to refresh the summary of a wizard if it accept */
    protected void tryToRefreshWizardSummary()
    {
	if ( this.getWizard() instanceof SummarizedWizard )
	{
	    ((SummarizedWizard)this.getWizard()).refreshSummary();
	}
    }
    
    /** method called when the wizard is about to be hidden */
//    @Override
    public void aboutToHideWizard()
    {   
	super.aboutToHideWizard();
    }
    
    /** method that initialize button states */
    protected void initializeButtonsStates()
    {   
        /** ... change editability .. */
        WizardPageDescriptor current = this.getWizard().getModel().getCurrentDescriptor();
        if ( current != null && ! current.TERMINAL_DESCRIPTOR_ID.equals(current.getNextDescriptorId()) )
        {   this.getWizard().getFinishButton().setEnabled(false); }
        
        /** and finally, alignment */
        this.getWizard().getNextButton().setHorizontalTextPosition(SwingConstants.LEADING);
    }
    
    /** apply internationalization
     *	@param locale the Locale to use
     */
    protected void internationalizeImpl(Locale locale)
    {
	/* ... and text according to i18n ... */
	ResourceBundle rc = null;
	try
	{   rc = ResourceBundle.getBundle("org.awl.rc.wizard", locale, DefaultWizardController.class.getClassLoader()); }
	catch(Exception e)
	{   e.printStackTrace(); }

	if ( rc != null )
	{   this.getWizard().getBackButton().setText(rc.getString("back"));
	    this.getWizard().getNextButton().setText(rc.getString("next"));
	    this.getWizard().getCancelButton().setText(rc.getString("cancel"));
	    this.getWizard().getFinishButton().setText(rc.getString("finish"));
	}
	else
	{   this.getWizard().getBackButton().setText("back");
	    this.getWizard().getNextButton().setText("next");
	    this.getWizard().getCancelButton().setText("cancel");
	    this.getWizard().getFinishButton().setText("finish");
	}
	
	/** needs to resize wizard because preferred size of the button changed */
	if ( this.getWizard() != null )
	{
	    this.getWizard().resizeIfNeeded();
	}
    }
    
    /* #########################################################################
     * ################## WizardModelListener implementation ###################
     * ######################################################################### */
    
    /** a new descriptor has been added
     *  @param event a WizardModelEvent
     */
//    @Override
    public void descriptorAdded(WizardModelEvent event)
    {   if ( event != null )
        {   if ( event.getDescriptor() != null )
            {   event.getDescriptor().addPropertyChangeListener(this); }
        }
    }
    
    /** a new descriptor has been removed
     *  @param event a WizardModelEvent
     */
//    @Override
    public void descriptorRemoved(WizardModelEvent event)
    {   if ( event != null )
        {   if ( event.getDescriptor() != null )
            {   event.getDescriptor().removePropertyChangeListener(this); }
        }
    }
    
    /** a new descriptor has been set as current descriptor
     *  @param event a CurrentDescriptorChangedEvent
     */
//    @Override
    public void currentDescriptorChanged(CurrentDescriptorChangedEvent event)
    {       
        if ( event.getNewDesc() == null )
	{   throw new RuntimeException("new descriptor could not be null"); }
	
	this.updateNavigationEnabledStates(event.getNewDesc());
	
	this.tryToRefreshWizardSummary();
    }
    
    /** refresh enabled state of navigation buttons
     *	@param descriptor the current descriptor
     */
    protected void updateNavigationEnabledStates(WizardPageDescriptor descriptor)
    {
        if ( descriptor == null )
	{
	    this.getWizard().getFinishButton().setEnabled(false);
	    this.getWizard().getCancelButton().setEnabled(true);
	    this.getWizard().getNextButton().setEnabled(false);
	    this.getWizard().getBackButton().setEnabled(false);
	}
	else
	{
	    /* if the descriptor is the last, then, update finish button
	     *
	     * warning : the descriptor could be incomplete and so next id could be null
	     */
	    boolean isTerminal = WizardConstants.TERMINAL_DESCRIPTOR_ID.equals(descriptor.getNextDescriptorId());
	    boolean isStarting = WizardConstants.STARTING_DESCRIPTOR_ID.equals(descriptor.getPreviousDescriptorId());
	    
	    NavigationAuthorization nextAuth     = descriptor.getNextPageAuthorization();
	    NavigationAuthorization previousAuth = descriptor.getPreviousPageAuthorization();
	    NavigationAuthorization cancelAuth   = descriptor.getCancelAuthorization();
	    NavigationAuthorization finishAuth   = descriptor.getFinishAuthorization();
	    
	    Boolean bnextAuth     = descriptor.getNextPageAuthorization().getEnableState();
	    Boolean bpreviousAuth = descriptor.getPreviousPageAuthorization().getEnableState();
	    Boolean bcancelAuth   = descriptor.getCancelAuthorization().getEnableState();
	    Boolean bfinishAuth   = descriptor.getFinishAuthorization().getEnableState();
	    
	    if ( bnextAuth == null )
	    {
		bnextAuth = new Boolean(! isTerminal);
	    }
	    if ( bpreviousAuth == null )
	    {
		bpreviousAuth = new Boolean(! isStarting);
	    }
	    if ( bfinishAuth == null )
	    {
		bfinishAuth = new Boolean(isTerminal);
	    }
	    if ( bcancelAuth == null )
	    {
		bcancelAuth = Boolean.TRUE;
	    }
	    
	    if ( isTerminal )
	    {
		bnextAuth = Boolean.FALSE;
	    }
	    if ( isStarting )
	    {
		bpreviousAuth = Boolean.FALSE;
	    }

	    this.getWizard().getNextButton().setEnabled(bnextAuth.booleanValue());
	    this.getWizard().getBackButton().setEnabled(bpreviousAuth.booleanValue());
	    this.getWizard().getFinishButton().setEnabled(bfinishAuth.booleanValue());
	    this.getWizard().getCancelButton().setEnabled(bcancelAuth.booleanValue());
	}
    }
    
    /* #########################################################################
     * ################ PropertyChangeListener implementation ##################
     * ######################################################################### */
    
    /**
     * This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source 
     *   	and the property that has changed.
     */
//    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {   
	super.propertyChange(evt);
	
	if ( evt.getSource() == this.getWizard() )
	{
	    if ( evt.getPropertyName().equals(Wizard.PROPERTY_MODEL) )
	    {
		if ( evt.getOldValue() instanceof PageContainer )
		{
		    ((PageContainer)evt.getOldValue()).removeDescriptorModificationListener(this);
		}
		if ( evt.getNewValue() instanceof PageContainer )
		{
		    ((PageContainer)evt.getNewValue()).addDescriptorModificationListener(this);
		}
	    }
	}
    }
    
    /* #########################################################################
     * ############ DescriptorModificationListener implementation ##############
     * ######################################################################### */
    
    /** method called when a modification is detected on one of its pages
     *	@param container a PageContainer
     *	@param evtModif a DescriptorModificationEvent
     */
    public void descriptorChanged(PageContainer container, final DescriptorModificationEvent evtModif)
    {
	final PropertyChangeEvent evt = evtModif.getChangeEvent();
	
	boolean summaryModified = false;
	
	if ( this.getWizard() != null )
	{
	    this.getWizard().descriptorChanged(evtModif.getPageDescriptor(), evtModif.getChangeEvent());
	}
	
        if ( evt.getSource() == this.getWizard().getModel().getCurrentDescriptor() )
        {   
            if ( evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_NEXT_ID) )
            {   boolean isTerminal = evt.getNewValue().equals(WizardConstants.TERMINAL_DESCRIPTOR_ID);
                this.getWizard().getFinishButton().setEnabled(isTerminal);
                this.getWizard().getNextButton().setEnabled(!isTerminal);
                summaryModified = true;
            }
            else if ( evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_ANCESTOR_ID) )
            {   boolean isStarting = evt.getNewValue().equals(WizardConstants.STARTING_DESCRIPTOR_ID);
                
                this.getWizard().getBackButton().setEnabled(!isStarting);
                summaryModified = true;
            }
	    else if ( evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_NEXT_ALLOWED) ||
		      evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_PREVIOUS_ALLOWED) ||
		      evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_CANCEL_ALLOWED) ||
		      evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_FINISH_ALLOWED) )
	    {
		this.updateNavigationEnabledStates(this.getWizard().getModel().getCurrentDescriptor());
	    }
	    else if ( evt.getPropertyName().equals(WizardPageDescriptor.PROPERTY_MESSAGE) )
	    {
		/* indicate to the wizard to control the message for the current descriptor */
		this.getWizard().refreshMessages();
	    }
        }
	
	/** if the description of a descriptor changed, then upate summary */
	if ( WizardPageDescriptor.PROPERTY_DESCRIPTION.equals(evt.getPropertyName()) )
	{
	    summaryModified = true;
	}
	if ( WizardPageDescriptor.PROPERTY_TITLE.equals(evt.getPropertyName()) )
	{
	    summaryModified = true;
	}
	if ( WizardPageDescriptor.PROPERTY_COMPONENT.equals(evt.getPropertyName()) )
	{
	    if ( evt.getOldValue() == null || evt.getOldValue() instanceof Component )
	    {
		if ( evt.getNewValue() == null || evt.getNewValue() instanceof Component )
		{
		    Runnable runnable = new Runnable()
		    {
			public void run()
			{
			    getWizard().updateDescriptorComponent(evtModif.getPageDescriptor(), 
								  (Component)evt.getOldValue(),
								  (Component)evt.getNewValue());
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
	}
            
	if ( summaryModified )
	{   
	    Runnable runnable = new Runnable()
	    {
		public void run()
		{
		    tryToRefreshWizardSummary();
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
    
}
