/*
 * $Id: ListDataReport.java,v 1.1 2006/11/01 16:23:06 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */


package org.jdesktop.test;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A <code>ListDataListener</code> that stores the received 
 * <code>ListDataEvents</code>.
 * 
 */
public class ListDataReport implements ListDataListener {
    
    /**
     * Holds a list of all canceled events.
     */
    protected List<ListDataEvent> changedEvents = new LinkedList<ListDataEvent>();
    protected List<ListDataEvent> addedEvents = new LinkedList<ListDataEvent>();
    protected List<ListDataEvent> removedEvents = new LinkedList<ListDataEvent>();
    protected List<ListDataEvent> allEvents = new LinkedList<ListDataEvent>();

//  ------------------ implement ListDataListener    

    public void contentsChanged(ListDataEvent e) {
        changedEvents.add(0, e);
        allEvents.add(0, e);
    }


    public void intervalAdded(ListDataEvent e) {
        addedEvents.add(e);
        allEvents.add(0, e);
    }


    public void intervalRemoved(ListDataEvent e) {
        removedEvents.add(e);
        allEvents.add(0, e);
    }
    
    
    
//----------------------- utility methods to access all events    

    public void clear() {
        changedEvents.clear();
        addedEvents.clear();
        removedEvents.clear();
        allEvents.clear();
    }

    public int getEventCount() {
        return allEvents.size();
    }
     
    public boolean hasEvents() {
        return !allEvents.isEmpty();
    }
 
    public ListDataEvent getLastEvent() {
        return allEvents.isEmpty() ? null : allEvents.get(0);
    }

// ------------------ access changed events
    public int getChangedEventCount() {
        return changedEvents.size();
    }
    
    public boolean hasChangedEvents() {
        return !changedEvents.isEmpty();
    }
    
    public ListDataEvent getLastChangedEvent() {
        return changedEvents.isEmpty() ? null : changedEvents.get(0);
    }

// ----------------- access added events
    
    public int getAddedEventCount() {
        return addedEvents.size();
    }
    
    public boolean hasAddedEvents() {
        return !addedEvents.isEmpty();
    }
    
    public ListDataEvent getLastAddedEvent() {
        return addedEvents.isEmpty() ? null : addedEvents.get(0);
    }


// ----------------- removed added events
    
    public int getRemovedEventCount() {
        return removedEvents.size();
    }
    
    public boolean hasRemovedEvents() {
        return !removedEvents.isEmpty();
    }
    
    public ListDataEvent getLastRemovedEvent() {
        return removedEvents.isEmpty() ? null : removedEvents.get(0);
    }

    
}
