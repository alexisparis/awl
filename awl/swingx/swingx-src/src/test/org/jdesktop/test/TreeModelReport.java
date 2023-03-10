/*
 * $Id: TreeModelReport.java,v 1.1 2007/08/30 10:12:54 kleopatra Exp $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.test;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * TODO add type doc
 * 
 * @author Jeanette Winzenburg
 */
public class TreeModelReport implements TreeModelListener {

    List<TreeModelEvent> allEvents = new LinkedList<TreeModelEvent>();
    List<TreeModelEvent> updateEvents = new LinkedList<TreeModelEvent>();
    List<TreeModelEvent> insertEvents = new LinkedList<TreeModelEvent>();
    List<TreeModelEvent> deleteEvents = new LinkedList<TreeModelEvent>();
    List<TreeModelEvent> structureEvents = new LinkedList<TreeModelEvent>();
    
//------------------- TableModelListener    
    public void treeNodesChanged(TreeModelEvent e) {
        allEvents.add(0, e);
        updateEvents.add(0, e);
        
    }

    public void treeNodesInserted(TreeModelEvent e) {
        allEvents.add(0, e);
        insertEvents.add(0, e);
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        allEvents.add(0, e);
        deleteEvents.add(0, e);
    }

    public void treeStructureChanged(TreeModelEvent e) {
        allEvents.add(0, e);
        structureEvents.add(0, e);
    }

    
    
//-------------------- all events access
    
    public void clear() {
        updateEvents.clear();
        deleteEvents.clear();
        insertEvents.clear();
        structureEvents.clear();
        allEvents.clear();
    }
    
    public boolean hasEvents() {
        return !allEvents.isEmpty();
    }

    /**
     * @return
     */
    public int getEventCount() {
        return allEvents.size();
    }
    
    public TreeModelEvent getLastEvent() {
        return allEvents.isEmpty() ? null : allEvents.get(0);
    }

//---------------- update events    
    public boolean hasUpdateEvents() {
        return !updateEvents.isEmpty();
    }

    /**
     * @return
     */
    public int getUpdateEventCount() {
        return updateEvents.size();
    }
    
    public TreeModelEvent getLastUpdateEvent() {
        return updateEvents.isEmpty() ? null : updateEvents.get(0);
    }

//  ---------------- insert events    
    public boolean hasInsertEvents() {
        return !insertEvents.isEmpty();
    }

    /**
     * @return
     */
    public int getInsertEventCount() {
        return insertEvents.size();
    }
    
    public TreeModelEvent getLastInsertEvent() {
        return insertEvents.isEmpty() ? null : insertEvents.get(0);
    }

//  ---------------- delete events    
    public boolean hasDeleteEvents() {
        return !deleteEvents.isEmpty();
    }

    /**
     * @return
     */
    public int getDeleteEventCount() {
        return deleteEvents.size();
    }
    
    public TreeModelEvent getLastDeleteEvent() {
        return deleteEvents.isEmpty() ? null : deleteEvents.get(0);
    }

//---------------- structure events
    
    public boolean hasStructureEvents() {
        return !structureEvents.isEmpty();
    }

    /**
     * @return
     */
    public int getStructureEventCount() {
        return structureEvents.size();
    }
    
    public TreeModelEvent getLastStructureEvent() {
        return structureEvents.isEmpty() ? null : structureEvents.get(0);
    }


 
}
