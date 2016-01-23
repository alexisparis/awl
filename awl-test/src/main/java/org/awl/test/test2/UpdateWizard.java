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
package org.awl.test.test2;

import java.awt.Frame;
import java.net.URL;
import javax.swing.ImageIcon;
import org.awl.DefaultWizard;
import org.awl.WizardConstants;
import org.awl.WizardPageDescriptor;

/**
 *
 * @author alexis
 */
public class UpdateWizard extends DefaultWizard
{
    
    /** Creates a new instance of BookWizardTest */
    public UpdateWizard(Frame frame)
    {   super(frame);
        
        WizardPageDescriptor desc1 = new SelectLocationModuleDescriptor();
        desc1.setPreviousDescriptorId(WizardConstants.STARTING_DESCRIPTOR_ID);
        desc1.setNextDescriptorId("2");
        
        WizardPageDescriptor desc2 = new ModulesToInstallDescriptor();
        desc2.setPreviousDescriptorId("1");
        desc2.setNextDescriptorId(WizardConstants.TERMINAL_DESCRIPTOR_ID);
        
        this.registerWizardPanel("1", desc1);
        this.registerWizardPanel("2", desc2);
        
        this.setTitle("Update Center Wizard");
//        this.setMinimumSize(new Dimension(300, 200));
//        this.setPreferredSize(new Dimension(600, 350));
        this.pack();
    }
    
    /* called on initialization after components initailization
     *	to add them to the content pane of the dialog
     */
    protected void placeComponents()
    {
	super.placeComponents();
	
//        String imgPath = "/org/awl/rc/sunset.png";
        String imgPath = "/org/awl/rc/siberia-vuduciel.jpg";
        
        try
        {   /* initialize image */
            URL url = this.getClass().getResource(imgPath);
            if ( url != null )
            {   this.getSummaryPanel().setBackgroundImage(new ImageIcon(url).getImage()); }
        } catch (Exception ex)
        {   ex.printStackTrace(); }
	
    }
                        
    
}
