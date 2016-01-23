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
package org.awl.test.test6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author alexis
 */
public class LocaleComboBox extends JComboBox
{
    /** Creates a new instance of LocaleComboBox */
    public LocaleComboBox()
    {	
	super();
	
	Locale[] locales = Locale.getAvailableLocales();
	
	List list = new ArrayList(locales.length);
	
	for(int i = 0; i < locales.length; i++)
	{
	    list.add(locales[i]);
	}
	
	/** sort */
	Collections.sort(list, new Comparator()
	{
	    public int compare(Object o1, Object o2)
	    {
		return ((Locale)o1).toString().compareTo(((Locale)o2).toString());
	    }
	});
	
	this.setModel(new DefaultComboBoxModel( list.toArray(new Locale[list.size()]) ));
    }
    
}
