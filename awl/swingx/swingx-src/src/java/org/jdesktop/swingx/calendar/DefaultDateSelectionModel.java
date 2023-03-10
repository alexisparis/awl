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
package org.jdesktop.swingx.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jdesktop.swingx.event.DateSelectionEvent;
import org.jdesktop.swingx.event.DateSelectionListener;
import org.jdesktop.swingx.event.EventListenerMap;
import org.jdesktop.swingx.event.DateSelectionEvent.EventType;

/**
 * @author Joshua Outwater
 */
public class DefaultDateSelectionModel implements DateSelectionModel {
    private EventListenerMap listenerMap;
    private SelectionMode selectionMode;
    private SortedSet<Date> selectedDates;
    private SortedSet<Date> unselectableDates;
    private Calendar cal;
    private int firstDayOfWeek;
    private Date upperBound;
    private Date lowerBound;
    private boolean adjusting;

    public DefaultDateSelectionModel() {
        this.listenerMap = new EventListenerMap();
        this.selectionMode = SelectionMode.SINGLE_SELECTION;
        this.selectedDates = new TreeSet<Date>();
        this.unselectableDates = new TreeSet<Date>();
        this.firstDayOfWeek = Calendar.SUNDAY;
        cal = Calendar.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionMode(final SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        clearSelection();
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(final int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    /**
     * {@inheritDoc}
     */
    public void addSelectionInterval(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            return;
        }
        boolean added = false;
        switch (selectionMode) {
            case SINGLE_SELECTION:
                if (isSelected(startDate)) return;
                clearSelectionImpl();
                added = addSelectionImpl(startDate, startDate);
                break;
            case SINGLE_INTERVAL_SELECTION:
                if (isIntervalSelected(startDate, endDate)) return;
                clearSelectionImpl();
                added = addSelectionImpl(startDate, endDate);
                break;
            case MULTIPLE_INTERVAL_SELECTION:
                if (isIntervalSelected(startDate, endDate)) return;
                added = addSelectionImpl(startDate, endDate);
                break;
            default:
                break;
        }
        if (added) {
            fireValueChanged(EventType.DATES_ADDED);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionInterval(final Date startDate, Date endDate) {
        if (SelectionMode.SINGLE_SELECTION.equals(selectionMode)) {
           if (isSelected(startDate)) return;
           endDate = startDate;
        } else {
            if (isIntervalSelected(startDate, endDate)) return;
        }
        clearSelectionImpl();
        if (addSelectionImpl(startDate, endDate)) {
            fireValueChanged(EventType.DATES_SET);
        }
//        boolean added = false;
//        switch (selectionMode) {
//            case SINGLE_SELECTION:
//                if (isSelected(startDate)) return;
//                clearSelectionImpl();
//                added = addSelectionImpl(startDate, startDate);
//                break;
//            case SINGLE_INTERVAL_SELECTION:
//                if (isIntervalSelected(startDate, endDate)) return;
//                clearSelectionImpl();
//                added = addSelectionImpl(startDate, endDate);
//                break;
//            case MULTIPLE_INTERVAL_SELECTION:
//                if (isIntervalSelected(startDate, endDate)) return;
//                clearSelectionImpl();
//                added =addSelectionImpl(startDate, endDate);
//                break;
//            default:
//                break;
//        }
//        if (added) {
//            fireValueChanged(EventType.DATES_SET);
//        }
    }

    /**
     * Checks and returns if the single date interval bounded by startDate and endDate
     * is selected. This is useful only for SingleInterval mode.
     * 
     * @param startDate the start of the interval
     * @param endDate the end of the interval, must be >= startDate
     * @return true the interval is selected, false otherwise.
     */
    private boolean isIntervalSelected(Date startDate, Date endDate) {
        if (isSelectionEmpty()) return false;
        return selectedDates.first().equals(startDate) 
           && selectedDates.last().equals(endDate);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSelectionInterval(final Date startDate, final Date endDate) {
        if (startDate.after(endDate)) {
            return;
        }

        long startDateMs = startDate.getTime();
        long endDateMs = endDate.getTime();
        ArrayList<Date> datesToRemove = new ArrayList<Date>();
        for (Date selectedDate : selectedDates) {
            long selectedDateMs = selectedDate.getTime();
            if (selectedDateMs >= startDateMs && selectedDateMs <= endDateMs) {
                datesToRemove.add(selectedDate);
            }
        }

        if (!datesToRemove.isEmpty()) {
            selectedDates.removeAll(datesToRemove);
            fireValueChanged(EventType.DATES_REMOVED);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clearSelection() {
        if (isSelectionEmpty()) return;
        clearSelectionImpl();
        fireValueChanged(EventType.SELECTION_CLEARED);
    }

    private void clearSelectionImpl() {
        selectedDates.clear();
    }

    /**
     * {@inheritDoc}
     */
    public SortedSet<Date> getSelection() {
        return new TreeSet<Date>(selectedDates);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected(final Date date) {
        return selectedDates.contains(date);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelectionEmpty() {
        return selectedDates.size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    public void addDateSelectionListener(DateSelectionListener l) {
        listenerMap.add(DateSelectionListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeDateSelectionListener(DateSelectionListener l) {
        listenerMap.remove(DateSelectionListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    public SortedSet<Date> getUnselectableDates() {
        return new TreeSet<Date>(unselectableDates);
    }

    /**
     * {@inheritDoc}
     */
    public void setUnselectableDates(SortedSet<Date> unselectableDates) {
        this.unselectableDates = unselectableDates;
        for (Date unselectableDate : this.unselectableDates) {
            removeSelectionInterval(unselectableDate, unselectableDate);
        }
        fireValueChanged(EventType.UNSELECTED_DATES_CHANGED);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isUnselectableDate(Date date) {
        return upperBound != null && upperBound.getTime() < date.getTime() ||
                lowerBound != null && lowerBound.getTime() > date.getTime() ||
                unselectableDates != null && unselectableDates.contains(date);
    }

    /**
     * {@inheritDoc}
     */
    public Date getUpperBound() {
        return upperBound;
    }

    /**
     * {@inheritDoc}
     */
    public void setUpperBound(Date upperBound) {
        if ((upperBound != null && !upperBound.equals(this.upperBound)) ||
                (upperBound == null && upperBound != this.upperBound)) {
            this.upperBound = upperBound;
            if (!selectedDates.isEmpty() && selectedDates.last().after(this.upperBound)) {
                if (this.upperBound != null) {
                    // Remove anything above the upper bound
                    long justAboveUpperBoundMs = this.upperBound.getTime() + 1;
                    if (!selectedDates.isEmpty() && selectedDates.last().before(this.upperBound))
                        removeSelectionInterval(this.upperBound, new Date(justAboveUpperBoundMs));
                }
            }
            fireValueChanged(EventType.UPPER_BOUND_CHANGED);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Date getLowerBound() {
        return lowerBound;
    }

    /**
     * {@inheritDoc}
     */
    public void setLowerBound(Date lowerBound) {
        if ((lowerBound != null && !lowerBound.equals(this.lowerBound)) ||
                (lowerBound == null && lowerBound != this.lowerBound)) {
            this.lowerBound = lowerBound;
            if (this.lowerBound != null) {
                // Remove anything below the lower bound
                long justBelowLowerBoundMs = this.lowerBound.getTime() - 1;
                if (!selectedDates.isEmpty() && selectedDates.first().before(this.lowerBound)) {
                    removeSelectionInterval(selectedDates.first(), new Date(justBelowLowerBoundMs));
                }
            }
            fireValueChanged(EventType.LOWER_BOUND_CHANGED);
        }
    }

    public List<DateSelectionListener> getDateSelectionListeners() {
        return listenerMap.getListeners(DateSelectionListener.class);
    }

    protected void fireValueChanged(DateSelectionEvent.EventType eventType) {
        List<DateSelectionListener> listeners = getDateSelectionListeners();
        DateSelectionEvent e = null;

        for (DateSelectionListener listener : listeners) {
            if (e == null) {
                e = new DateSelectionEvent(this, eventType, isAdjusting());
            }
            listener.valueChanged(e);
        }
    }

    private boolean addSelectionImpl(final Date startDate, final Date endDate) {
        boolean hasAdded = false;
        cal.setTime(startDate);
        Date date = cal.getTime();
        while (date.before(endDate) || date.equals(endDate)) {
            if (!isUnselectableDate(date)) {
                hasAdded = true;
                selectedDates.add(date);
            }
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
        return hasAdded;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAdjusting() {
        return adjusting;
    }

    /**
     * {@inheritDoc}
     */
    public void setAdjusting(boolean adjusting) {
        if (adjusting == isAdjusting()) return;
        this.adjusting = adjusting;
       fireValueChanged(adjusting ? EventType.ADJUSTING_STARTED : EventType.ADJUSTING_STOPPED);
        
    }
}