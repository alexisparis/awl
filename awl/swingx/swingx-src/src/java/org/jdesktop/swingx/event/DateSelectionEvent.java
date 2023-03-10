/**
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
 */
package org.jdesktop.swingx.event;

import java.util.Date;
import java.util.EventObject;
import java.util.SortedSet;

import org.jdesktop.swingx.calendar.DateSelectionModel;

/**
 * @author Joshua Outwater
 */
public class DateSelectionEvent extends EventObject {
    public static enum EventType {
        DATES_ADDED,
        DATES_REMOVED,
        DATES_SET,
        SELECTION_CLEARED,
        SELECTABLE_DATES_CHANGED,
        SELECTABLE_RANGE_CHANGED,
        UNSELECTED_DATES_CHANGED,
        LOWER_BOUND_CHANGED,
        UPPER_BOUND_CHANGED, 
        ADJUSTING_STARTED, ADJUSTING_STOPPED
    }

    private EventType eventType;
    private boolean adjusting;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @param eventType the type of the event
     * @param adjusting the adjusting property of the source
     * @throws IllegalArgumentException if source is null.
     */
    public DateSelectionEvent(Object source, EventType eventType, boolean adjusting) {
        super(source);
        this.eventType = eventType;
        this.adjusting = adjusting;
    }

    public SortedSet<Date> getSelection() {
        return ((DateSelectionModel)source).getSelection();
    }

    public final EventType getEventType() {
        return eventType;
    }

    /**
     * @return
     */
    public boolean isAdjusting() {
        return adjusting;
    }
}
