/*
 * $Id: DateSelectionReport.java,v 1.4 2007/11/17 18:56:45 kschaefe Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */


package org.jdesktop.swingx.test;

import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.event.DateSelectionEvent;
import org.jdesktop.swingx.event.DateSelectionListener;
import org.jdesktop.swingx.event.DateSelectionEvent.EventType;

/**
 * A ChangeListener that stores the received ChangeEvents.
 * 
 */
public class DateSelectionReport implements DateSelectionListener {
    
    /**
     * Holds a list of all received DateSelectionEvents.
     */
    protected List<DateSelectionEvent> events = new LinkedList<DateSelectionEvent>();
    
//------------------------ implement DateSelectionListener
    
    public void valueChanged(DateSelectionEvent evt) {
        events.add(0, evt);
    }

// ------------------- accessors
    
    public int getEventCount() {
        return events.size();
    }
 
    public void clear() {
        events.clear();
    }
    
    public boolean hasEvents() {
        return !events.isEmpty();
    }
 
     public DateSelectionEvent getLastEvent() {
        return hasEvents() ? events.get(0) : null;
    }

    /**
     * @return the EventType of the last event or null if no event received. 
     */
    public EventType getLastEventType() {
        return hasEvents() ? getLastEvent().getEventType() : null;
    }

}
