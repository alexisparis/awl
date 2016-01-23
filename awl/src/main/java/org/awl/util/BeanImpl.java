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
package org.awl.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * Class that support some bean operations
 *
 * @author alexis
 */
public class BeanImpl implements Bean
{
    /** PropertyChangeSupport */
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    /** Creates a new instance of Bean */
    public BeanImpl()
    {   }
    
    /** fire a PropertyEvent to listener
     *  @param propertyName the name of the property
     *  @param oldValue the old value
     *  @param newValue the new value
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {   this.support.firePropertyChange(propertyName, oldValue, newValue); }
    
    /** fire a PropertyEvent to listener
     *	@param evt a PropertyChangeEvent
     */
    protected void firePropertyChange(PropertyChangeEvent evt)
    {   this.support.firePropertyChange(evt); }
    
    /** add a new PropertychangeListener
     *  @param listener a PropertychangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {   this.support.addPropertyChangeListener(listener); }
    
    /** add a new PropertychangeListener
     *  @param propertyName the name of the property
     *  @param listener a PropertychangeListener
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {   this.support.addPropertyChangeListener(propertyName, listener); }
    
    /** remove a new PropertychangeListener
     *  @param listener a PropertychangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {   this.support.removePropertyChangeListener(listener); }
    
    /** remove a new PropertychangeListener
     *  @param propertyName the name of the property
     *  @param listener a PropertychangeListener
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
    {   this.support.removePropertyChangeListener(propertyName, listener); }
    
}
