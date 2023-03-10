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
package org.jdesktop.swingx;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DefaultDateSelectionModel;
import org.jdesktop.swingx.event.DateSelectionEvent;
import org.jdesktop.swingx.test.DateSelectionReport;
import org.jdesktop.swingx.test.XTestUtils;

/**
 * Tests for the DefaultDateSelectionModel
 */
public class DefaultDateSelectionModelTest extends TestCase {
    private static final Logger LOG = Logger
            .getLogger(DefaultDateSelectionModelTest.class.getName());
    private DateSelectionModel model;
    private Calendar calendar;

    

    /**
     * Issue #625-swingx: DateSelectionModel must not fire if unchanged.
     * Here: test setSelection with single mode
     */
    public void testSelectionSetNotFireIfSameSingle() {
        final Date date = calendar.getTime();
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_SELECTION);
        model.setSelectionInterval(date, date);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(date, date);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }

    /**
     * Issue #625-swingx: DateSelectionModel must not fire if unchanged.
     * Here: test addSelection with single mode
     */
    public void testSelectionAddNotFireIfSameSingle() {
        final Date date = calendar.getTime();
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_SELECTION);
        model.setSelectionInterval(date, date);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.addSelectionInterval(date, date);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }

 
    /**
     * Issue #625-swingx: DateSelectionModel must not fire if unchanged.
     * Here: test setSelection with single interval mode
     */
    public void testSelectionSetNotFireIfSameSingleInterval() {
        final Date date = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        final Date end = calendar.getTime();
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_INTERVAL_SELECTION);
        model.setSelectionInterval(date, end);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(date, end);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }

    /**
     * Issue #625-swingx: DateSelectionModel must not fire if unchanged.
     * Here: test addSelection with single interval mode
     */
    public void testSelectionAddNotFireIfSameSingleInterval() {
        final Date date = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        final Date end = calendar.getTime();
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_INTERVAL_SELECTION);
        model.setSelectionInterval(date, end);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.addSelectionInterval(date, end);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }

    /**
     * Issue #625-swingx: DateSelectionModel must not fire if unchanged.
     * Here: test setSelection with multiple interval mode
     */
    public void testSelectionSetNotFireIfSameMultipleInterval() {
        final Date date = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        final Date end = calendar.getTime();
        model.setSelectionMode(DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        model.setSelectionInterval(date, end);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(date, end);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }

    /**
     * Issue #625-swingx: DateSelectionModel must not fire if unchanged.
     * Here: test addSelection with multiple interval mode
     */
    public void testSelectionAddNotFireIfSameMultipeInterval() {
        final Date date = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        final Date end = calendar.getTime();
        model.setSelectionMode(DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        model.setSelectionInterval(date, end);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.addSelectionInterval(date, end);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }
    /**
     * related to #625-swingx: DateSelectionModel must not fire on clearing empty selection.
     */
    public void testDateSelectionClearSelectionNotFireIfUnselected() {
        // sanity
        assertTrue(model.isSelectionEmpty());
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.clearSelection();
        assertEquals("selection must not fire on clearing empty selection",
                0,
                report.getEventCount());
    }
    
    /**
     * related to #625-swingx: DateSelectionModel must fire SELECTION_CLEARED if 
     * had selection.
     * Testing here for sanity reasons ... be sure we didn't prevent the firing
     * altogether while changing.
     */
    public void testDateSelectionClearSelectionFireIfSelected() {
        Date date = new Date();
        model.setSelectionInterval(date, date);
        // sanity
        assertFalse(model.isSelectionEmpty());
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.clearSelection();
        assertEquals("selection must fire on clearing selection",
                1,
                report.getEventCount());
        assertEquals("event type must be SELECTION_CLEARED",
                DateSelectionEvent.EventType.SELECTION_CLEARED,
                report.getLastEventType());
    }
    
    /**
     * related to #625-swingx: DateSelectionModel must not fire on clearing empty selection.
     */
    public void testDateSelectionSetSelectionNotFireIfSelected() {
        Date date = new Date();
        model.setSelectionInterval(date, date);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(date, date);
        assertEquals("selection must not fire on selecting already selected date",
                0,
                report.getEventCount());
    }
    
    /**
     * related to #625-swingx: DateSelectionModel must fire SELECTION_CLEARED if 
     * had selection.
     * Testing here for sanity reasons ... be sure we didn't prevent the firing
     * altogether while changing.
     */
    public void testDateSelectionSetSelectionFire() {
        Date date = new Date();
        model.setSelectionInterval(date, date);
        // sanity
        assertTrue(model.isSelected(date));
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        // modify the date
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 1);
        model.setSelectionInterval(calendar.getTime(), calendar.getTime());
        assertEquals("selection must fire on selection",
                1,
                report.getEventCount());
        assertEquals("event type must be DATES_SET",
                DateSelectionEvent.EventType.DATES_SET,
                report.getLastEventType());
    }
    


    /**
     * adding api: adjusting
     *
     */
    public void testEventsCarryAdjustingFlagTrue() {
        Date date = calendar.getTime();
        model.setAdjusting(true);
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(date, date);
        assertEquals(model.isAdjusting(), report.getLastEvent().isAdjusting());
        // sanity: revert 
        model.setAdjusting(false);
        report.clear();
        model.removeSelectionInterval(date, date);
        assertEquals(model.isAdjusting(), report.getLastEvent().isAdjusting());
        
    }

    /**
     * adding api: adjusting
     *
     */
    public void testEventsCarryAdjustingFlagFalse() {
        Date date = calendar.getTime();
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(date, date);
        assertEquals(model.isAdjusting(), report.getLastEvent().isAdjusting());
    }
    
    /**
     * adding api: adjusting.
     *
     */
    public void testAdjusting() {
        // default value
        assertFalse(model.isAdjusting());
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        // set adjusting
        model.setAdjusting(true);
        assertTrue("model must be adjusting", model.isAdjusting());
        assertEquals(1, report.getEventCount());
        assertEquals(DateSelectionEvent.EventType.ADJUSTING_STARTED, 
                report.getLastEventType());
        // next round - reset to default adjusting
        report.clear();
        model.setAdjusting(false);
        assertFalse("model must not be adjusting", model.isAdjusting());
        assertEquals(1, report.getEventCount());
        assertEquals(DateSelectionEvent.EventType.ADJUSTING_STOPPED, 
                report.getLastEventType());
        
    }
    

    /**
     * respect both bounds - 
     *
     * Both bounds same --> bound allowed.
     */
    public void testBothBoundsSame() {
        Date today = XTestUtils.getStartOfToday();
        model.setLowerBound(today);
        model.setUpperBound(today);
        model.setSelectionInterval(today, today);
        assertEquals("selected bounds", today, 
                model.getSelection().first());
    }

    /**
     * respect both bounds - 
     *
     * Both bounds same --> bound allowed.
     */
    public void testBothBoundsOverlap() {
        Date today = XTestUtils.getStartOfToday();
        model.setLowerBound(today);
        Date yesterday = XTestUtils.getStartOfToday(-1);
        model.setUpperBound(yesterday);
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(today, today);
        assertEquals("selection must be empty", 0, model.getSelection().size());
        assertEquals("no event fired", 0, report.getEventCount());
    }
    
   /**
     *  respect lower bound - the day before is
     *  an invalid selection.
     *
     */
    public void testLowerBoundPast() {
        Date today = XTestUtils.getStartOfToday();
        model.setLowerBound(today);
        Date yesterday = XTestUtils.getStartOfToday(-1);
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(yesterday, yesterday);
        assertEquals("selection must be empty", 0, model.getSelection().size());
        assertEquals("no event fired", 0, report.getEventCount());
    }
    
    /**
     *  respect lower bound - the bound itself 
     *  a valid selection.
     *
     */
    public void testLowerBound() {
        Date today = XTestUtils.getStartOfToday();
        model.setLowerBound(today);
        // the bound itself is allowed
        model.setSelectionInterval(today, today);
        assertEquals("selected upper bound", model.getLowerBound(), 
                model.getSelection().first());
    }
    /**
     *  respect upper bound - the day after is
     *  an invalid selection.
     *
     */
    public void testUpperBoundFuture() {
        Date today = XTestUtils.getStartOfToday();
        model.setUpperBound(today);
        Date tomorrow = XTestUtils.getStartOfToday(1);
        DateSelectionReport report = new DateSelectionReport();
        model.addDateSelectionListener(report);
        model.setSelectionInterval(tomorrow, tomorrow);
        assertEquals("selection must be empty", 0, model.getSelection().size());
        assertEquals("no event fired", 0, report.getEventCount());
    }
 
    /**
     * Remove the upper bound constraint
     */
    public void testUpperBoundRemove() {
        Date today = XTestUtils.getStartOfToday();
        model.setUpperBound(today);
        Date tomorrow = XTestUtils.getStartOfToday(1);
        model.setUpperBound(null);
        model.setSelectionInterval(tomorrow, tomorrow);
        assertTrue("selection must be empty", model.isSelected(tomorrow));
    }

    /**
     *  respect upper bound - the bound itself 
     *  a valid selection.
     *
     */
    public void testUpperBound() {
        Date today = XTestUtils.getStartOfToday();
        model.setUpperBound(today);
        // the bound itself is allowed
        model.setSelectionInterval(today, today);
        assertEquals("selected upper bound", model.getUpperBound(), 
                model.getSelection().first());
    }
    
    /**
     * first set the unselectables then set the selection to an unselectable.
     */
    public void testUnselectableDates() {
        Date today = XTestUtils.getStartOfToday();
        SortedSet<Date> unselectableDates = new TreeSet<Date>();
        unselectableDates.add(today);
        model.setUnselectableDates(unselectableDates);
        // sanity: 
        assertTrue(model.isUnselectableDate(today));
        model.setSelectionInterval(today, today);
        assertEquals("selection must be empty", 0, model.getSelection().size());
    }
    
    /**
     * null unselectables not allowed.
     */
    public void testUnselectableDatesNull() {
        try {
            model.setUnselectableDates(null);
            fail("must fail with null set of unselectables");
        } catch (RuntimeException e) {
            // expected
            LOG.info("got NPE as expected - how to test fail-fast?");
        }
    }

    public void testSingleSelection() {
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_SELECTION);
        Date today = new Date();
        model.setSelectionInterval(today, today);
        SortedSet<Date> selection = model.getSelection();
        assertTrue(!selection.isEmpty());
        assertTrue(1 == selection.size());
        assertTrue(today.equals(selection.first()));

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.roll(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = cal.getTime();
        model.setSelectionInterval(today, tomorrow);
        selection = model.getSelection();
        assertTrue(!selection.isEmpty());
        assertTrue(1 == selection.size());
        assertTrue(today.equals(selection.first()));

        model.addSelectionInterval(tomorrow, tomorrow);
        selection = model.getSelection();
        assertTrue(!selection.isEmpty());
        assertTrue(1 == selection.size());
        assertTrue(tomorrow.equals(selection.first()));
    }

    public void testSingleIntervalSelection() {
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_INTERVAL_SELECTION);
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date endDate = cal.getTime();
        model.setSelectionInterval(startDate, endDate);
        SortedSet<Date> selection = model.getSelection();
        assertTrue(startDate.equals(selection.first()));
        assertTrue(endDate.equals(selection.last()));

        cal.setTime(startDate);
        cal.roll(Calendar.MONTH, 1);
        Date startDateNextMonth = cal.getTime();
        model.addSelectionInterval(startDateNextMonth, startDateNextMonth);
        selection = model.getSelection();
        assertTrue(startDateNextMonth.equals(selection.first()));
        assertTrue(startDateNextMonth.equals(selection.last()));
    }

    /**
     * characterize behaviour on setSelectionInterval depending on selection mode.
     * Here: compare singleInterval and multipleInterval - must be same.
     */
    public void testSetIntervalSameForSingleAndMultipleIntervalMode() {
        Date date = new Date();
        // pre-select 
        model.setSelectionMode(DateSelectionModel.SelectionMode.SINGLE_INTERVAL_SELECTION);
        model.setSelectionInterval(date, date);
        
        // get span in future
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date endDate = cal.getTime();
        // set the interval
        model.setSelectionInterval(startDate, endDate);
        SortedSet<Date> selection = model.getSelection();
        // sanity
        assertEquals(6, selection.size());

        // prepare model for multiple interval selection
        model.clearSelection();
        model.setSelectionMode(DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        model.setSelectionInterval(date, date);
        
        model.setSelectionInterval(startDate, endDate);
        assertEquals(selection.size(), model.getSelection().size());
        assertEquals(selection.first(), model.getSelection().first());
        assertEquals(selection.last(), model.getSelection().last());
    }
    
    public void testUnselctableDates() {
        // Make sure the unselectable dates returns an empty set if it hasn't been
        // used.
        SortedSet<Date> unselectableDates = model.getUnselectableDates();
        assert(unselectableDates.isEmpty());

        model.setSelectionMode(DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tPlus1 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tPlus2 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tPlus3 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tPlus4 = cal.getTime();

        model.setSelectionInterval(today, tPlus4);
        SortedSet<Date> selection = model.getSelection();
        assertTrue(!selection.isEmpty());
        assertTrue(5 == selection.size());
        assertTrue(today.equals(selection.first()));
        assertTrue(tPlus4.equals(selection.last()));

        unselectableDates = new TreeSet<Date>();
        unselectableDates.add(tPlus1);
        unselectableDates.add(tPlus3);
        model.setUnselectableDates(unselectableDates);

        // Make sure setting the unselectable dates to include a selected date removes
        // it from the selected set.
        selection = model.getSelection();
        assertTrue(!selection.isEmpty());
        assertTrue(3 == selection.size());
        assertTrue(selection.contains(today));
        assertTrue(selection.contains(tPlus2));
        assertTrue(selection.contains(tPlus4));

        // Make sure the unselectable dates is the same as what we set.
        SortedSet<Date> result = model.getUnselectableDates();
        assertTrue(unselectableDates.equals(result));
    }
    
    @Override
    public void setUp() {
        model = new DefaultDateSelectionModel();
        calendar = Calendar.getInstance();
    }

}