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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.awl.event.CurrentDescriptorChangedEvent;
import org.awl.event.WizardModelEvent;
import org.awl.event.WizardModelListener;
import org.awl.exception.PageDescriptorChangingException;
import org.awl.message.MessageLevel;
import org.awl.util.BeanImpl;

/**
 *
 * Abstract implementation of WizardController
 * which manage internationalization
 * and result code
 *
 * @author alexis
 */
public abstract class AbstractWizardController extends BeanImpl implements WizardController,
									   WizardModelListener,
									   PropertyChangeListener
{
    /** wizard to control */
    private Wizard  wizard     = null;
    
    /** result */
    private int     result     = WizardConstants.WIZARD_CLOSED_OPTION;
    
    /** indicates if the internationalization of the wizard is enabled */
    private boolean manageI18n = true;
    
    /** Creates a new instance of AbstractWizardController */
    public AbstractWizardController()
    {	this(true); }
    
    /** Creates a new instance of AbstractWizardController
     *	@param manageI18n true if the controller should manage internationalization
     */
    public AbstractWizardController(boolean manageI18n)
    {   
	this.setManageI18n(manageI18n);
    }
    
    /** indicate if the controller should manage internationalization
     *	@param enabled a boolean
     */
    public void setManageI18n(boolean enabled)
    {
	if ( enabled != this.isI18nManaged() )
	{
	    this.manageI18n = enabled;
	    
	    this.firePropertyChange(PROPERTY_MANAGE_I18N, new Boolean(! enabled), new Boolean(enabled));
	    
	    if ( this.manageI18n )
	    {
		/** force the configuration of all i18n dependencies */
		internationalize();
	    }
	}
    }
    
    /** indicate if the controller should manage internationalization
     *	@return true if the controller manage internationalization
     */
    public boolean isI18nManaged()
    {	return this.manageI18n; }

    /**
     * set the wizard attached to this Controller
     * 
     * @param wizard a Wizard
     */
    public void setWizard(Wizard wizard)
    {   if ( wizard == null )
            throw new IllegalArgumentException("Wizard must be non null");
        
	if ( this.getWizard() != wizard )
	{
	    if ( this.getWizard() != null )
	    {
		this.getWizard().removePropertyChangeListener("locale", this);
	    }
	    
	    this.wizard = wizard;
	    
	    if ( this.getWizard() != null )
	    {
		this.getWizard().addPropertyChangeListener("locale", this);
	    }
	}
    }

    /**
     * return the wizard attached to this Controller
     * 
     * @return a Wizard
     */
    public Wizard getWizard()
    {   return this.wizard; }
    
    /** set the code representing the manner that wizard disappear
     *  @param result an integer defined in WizardConstants : <br>
     *          WIZARD_VALID_OPTION if finish has been pressed and accepted 
     *          WIZARD_CLOSED_OPTION if close button has been pressed and accepted 
     *          WIZARD_CANCEL_OPTION if cancel has been pressed and accepted 
     */
    protected void setReturnCode(int result)
    {   
        if ( result != WizardConstants.WIZARD_CANCEL_OPTION && 
             result != WizardConstants.WIZARD_CLOSED_OPTION && 
             result != WizardConstants.WIZARD_VALID_OPTION )
        {   throw new IllegalArgumentException("result have to be in {" + WizardConstants.WIZARD_CANCEL_OPTION + ", " +
                                                                         WizardConstants.WIZARD_CLOSED_OPTION + ", " +  
                                                                         WizardConstants.WIZARD_VALID_OPTION + "}");
        }
        
        this.result = result;
    }
    
    /** return a code representing the manner that wizard disappear
     *  @return an integer defined in WizardConstants : <br>
     *          WIZARD_VALID_OPTION if finish has been pressed and accepted 
     *          WIZARD_CLOSED_OPTION if close button has been pressed and accepted 
     *          WIZARD_CANCEL_OPTION if cancel has been pressed and accepted 
     */
    public int getReturnCode()
    {   return this.result; }
    
    /** method called when the wizard is about to be shown on screen */
    public void aboutToDisplayWizard()
    {   }
    
    /** method called when the wizard is about to be hidden */
    public void aboutToHideWizard()
    {   }
    
    /** apply internationalization */
    protected final void internationalize()
    {
	this.internationalize( (this.getWizard() == null ? Locale.getDefault() : this.getWizard().getLocale()) );
    }
    
    /** apply internationalization
     *	@param locale the Locale to use
     */
    protected final void internationalize(Locale locale)
    {
	if ( this.isI18nManaged() )
	{
	    this.internationalizeImpl(locale);
	}
    }
    
    /** apply internationalization
     *	@param locale the Locale to use
     */
    protected abstract void internationalizeImpl(Locale locale);
    
    /* #########################################################################
     * ################### WizardModelListener implementation ##################
     * ######################################################################### */
    /** a new descriptor has been added
     *  @param event a WizardModelEvent
     */
    public void descriptorAdded(WizardModelEvent event)
    {	}
    
    /** a new descriptor has been removed
     *  @param event a WizardModelEvent
     */
    public void descriptorRemoved(WizardModelEvent event)
    {	}
    
    /** method that is called to verify that all WizardModelListener agree to change the current descriptor
     *  @param currentDescriptor the current descriptor
     *  @param candidate the descriptor we try to set as current descriptor
     *
     *  @exception PageDescriptorChangingException if the listener refused the change
     */
    public void checkCurrentDescriptorChanging(WizardPageDescriptor currentDescriptor, WizardPageDescriptor candidate)
            throws PageDescriptorChangingException
    {	}
    
    /** a new descriptor has been set as current descriptor
     *  @param event a CurrentDescriptorChangedEvent
     */
    public void currentDescriptorChanged(CurrentDescriptorChangedEvent event)
    {	}
    
    /* #########################################################################
     * ################ PropertyChangeListener implementation ##################
     * ######################################################################### */
    
    /**
     * This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source 
     *   	and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt)
    {   
        if ( evt.getSource() == this.getWizard() && evt.getPropertyName().equals("locale") )
	{
	    /** apply the correct ResourceBundle according to the new locale */
	    if ( evt.getNewValue() instanceof Locale )
	    {
		this.internationalize( (Locale)evt.getNewValue() );
	    }
	}
    }
    
}
