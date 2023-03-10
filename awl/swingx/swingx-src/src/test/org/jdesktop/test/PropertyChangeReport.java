/*
 * $Id: PropertyChangeReport.java,v 1.3 2007/07/29 13:33:57 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */


package org.jdesktop.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A PropertyChangeListener that stores the received PropertyChangeEvents.
 * 
 * modified ("beanified") from JGoodies PropertyChangeReport.
 * 
 */
public class PropertyChangeReport implements PropertyChangeListener {
    
    /**
     * Holds a list of all received PropertyChangeEvents.
     */
    protected List<PropertyChangeEvent> events = new LinkedList<PropertyChangeEvent>();
    protected Map<String, PropertyChangeEvent> eventMap = new HashMap<String, PropertyChangeEvent>();
    
//------------------------ implement PropertyChangeListener
    
    public void propertyChange(PropertyChangeEvent evt) {
        events.add(0, evt);
        if (evt.getPropertyName() != null) {
            eventMap.put(evt.getPropertyName(), evt);
        }
    }
    
    public int getEventCount() {
        return events.size();
    }
 
    public void clear() {
        events.clear();
        eventMap.clear();
    }
    
    public boolean hasEvents() {
        return !events.isEmpty();
    }
 
    public int getEventCount(String property) {
        if (property == null) return getMultiCastEventCount();
        int count = 0;
        for (Iterator iter = events.iterator(); iter.hasNext();) {
            PropertyChangeEvent event = (PropertyChangeEvent) iter.next();
            if (property.equals(event.getPropertyName())) {
                count++;
            }
        }
        return count;
    }

    public boolean hasEvents(String property) {
        return eventMap.get(property) != null;
    }
    
    public int getMultiCastEventCount() {
        int count = 0;
        for (Iterator i = events.iterator(); i.hasNext();) {
            PropertyChangeEvent event = (PropertyChangeEvent) i.next();
            if (event.getPropertyName() == null)
                count++;
        }
        return 0;
    }
    
    public int getNamedEventCount() {
        return getEventCount() - getMultiCastEventCount();
    }
    
    public PropertyChangeEvent getLastEvent() {
        return events.isEmpty()
            ? null
            : events.get(0);
    }

    public PropertyChangeEvent getLastEvent(String property) {
        return eventMap.get(property);
    }
    
    public Object getLastOldValue() {
        PropertyChangeEvent last = getLastEvent();
        return last != null ? last.getOldValue() : null;
    }
    
    public Object getLastNewValue() {
        PropertyChangeEvent last = getLastEvent();
        return last != null ? last.getNewValue() : null;
    }
 
    /**
     * @return the propertyName of the last event or 
     *    null if !hasEvents().
     */
    public String getLastProperty() {
        PropertyChangeEvent last = getLastEvent();
        return last != null ? last.getPropertyName() : null;
    }
    /**
     * @return
     */
    public Object getLastSource() {
        PropertyChangeEvent last = getLastEvent();
        return last != null ? last.getSource() : null;
    }
    
    public Object getLastOldValue(String property) {
        PropertyChangeEvent last = getLastEvent(property);
        return last != null ? last.getOldValue() : null;
    }
    
    public Object getLastNewValue(String property) {
        PropertyChangeEvent last = getLastEvent(property);
        return last != null ? last.getNewValue() : null;
    }
    
    /**
     * PRE: hasEvents()
     * @return
     */
    public boolean getLastOldBooleanValue() {
        return ((Boolean) getLastOldValue()).booleanValue();
    }

    /**
     * PRE: hasEvents()
     * @return
     */
    public boolean getLastNewBooleanValue() {
        return ((Boolean) getLastNewValue()).booleanValue();
    }

    /**
     * @return
     */
    public String getEventNames() {
        StringBuffer buffer = new StringBuffer();
        for (PropertyChangeEvent event : events) {
            buffer.append(event.getPropertyName());
            buffer.append(" :: ");
        }
        return buffer.toString();
    }



}
