/*
 * EclipseWizard.java
 *
 * Created on 6 janvier 2008, 18:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.awl.test.testG;

import java.awt.Frame;
import org.awl.test.testE.WizardWithMessage;
import org.awl.header.EclipseWizardHeader;

/**
 *
 * @author alexis
 */
public class EclipseWizard extends WizardWithMessage
{
    
    /** Creates a new instance of EclipseWizard */
    public EclipseWizard(Frame frame)
    {
	super(frame);
	
	this.setTitle("like Eclipse");
	
	this.setHeaderPanel(new EclipseWizardHeader());
    }
    
}
