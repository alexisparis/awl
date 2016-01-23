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

import org.awl.event.WizardModelListener;
import org.awl.util.Bean;

/**
 *
 * Class that control the graphical actions that occured in a wizard
 *
 * based on an <a rel="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">
 * article</a> by Robert Eckstein, February 10, 2005 <br>
 *
 * @author alexis
 */
public interface WizardController extends WizardModelListener, Bean
{
    /** name of the property which indicate if the controller should manage internationalization */
    public static final String PROPERTY_MANAGE_I18N = "manage-i18n";
    
    /**
	 * set the wizard attached to this Controller
	 * 
	 * @param wizard a Wizard
	 */
    public void setWizard(Wizard wizard);
    
    /**
	 * return the wizard attached to this Controller
	 * 
	 * @return a Wizard
	 */
    public Wizard getWizard();
    
    /** method called when cancel is pressed */
    public void cancelButtonPressed();

    /** method called when next is pressed */
    public void nextButtonPressed();

    /** method called when back is pressed */
    public void backButtonPressed();

    /** method called when finish is pressed */
    public void finishButtonPressed();
    
    /** method called when the wizard is about to be shown on screen */
    public void aboutToDisplayWizard();
    
    /** method called when the wizard is about to be hidden */
    public void aboutToHideWizard();
    
    /** return a code representing the manner that wizard disappear
     *  @return an integer defined in WizardConstants : <br>
     *          WIZARD_VALID_OPTION if finish has been pressed and accepted 
     *          WIZARD_CLOSED_OPTION if close button has been pressed and accepted 
     *          WIZARD_CANCEL_OPTION if cancel has been pressed and accepted 
     */
    public int getReturnCode();
    
    /** indicate if the controller should manage internationalization
     *	@param enabled a boolean
     */
    public void setManageI18n(boolean enabled);
    
    /** indicate if the controller should manage internationalization
     *	@return true if the controller manage internationalization
     */
    public boolean isI18nManaged();
    
}
