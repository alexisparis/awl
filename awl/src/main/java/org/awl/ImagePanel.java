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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * Panel that display basically an image in background
 *
 * @author alexis
 */
public class ImagePanel extends JPanel
{   
    /** default image path */
//        private static String DEFAULT_IMG_PATH = "/org/awl/rc/sunset.png";
//        private static String DEFAULT_IMG_PATH = "/org/awl/rc/siberia-vuduciel.jpg";
    private static String DEFAULT_IMG_PATH = "/org/awl/rc/labyrinth2.jpg";

    /** background image */
    private Image image = null;

    /** constructor */
    public ImagePanel()
    {   super();
	try
	{   /* initialize image */
	    URL url = this.getClass().getResource(DEFAULT_IMG_PATH);
	    if ( url != null )
	    {   this.setBackgroundImage(new ImageIcon(url).getImage()); }
	} catch (Exception ex)
	{   ex.printStackTrace(); }
    }

    /** return the image used as background
     *  @return an Image
     */
    public Image getBackgroundImage()
    {   return image; }

    /** initialize the image used as background
     *  @param image an Image
     */
    public void setBackgroundImage(Image image)
    {   this.image = image; }

    public void paintComponent(Graphics g)
    {   
	super.paintComponent(g);

	if ( this.getBackgroundImage() != null )
	{   Dimension dim = this.getSize();

//                Image t = this.createThumbnailWidth(this.getBackgroundImage(), dim.width);
//                Image t = this.createThumbnailHeight(this.getBackgroundImage(), dim.height);
	    g.drawImage(this.getBackgroundImage(), 0, 0, dim.width, dim.height, this);
	}
    }
}
